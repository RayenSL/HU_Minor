package nl.hu.assignments.term1.week2.functionaltesting.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import nl.hu.assignments.term1.week2.functionaltesting.domain.exception.*;
import nl.hu.assignments.term1.week2.functionaltesting.domain.service.AccountService;
import nl.hu.assignments.term1.week2.functionaltesting.domain.service.HolderService;
import nl.hu.assignments.term1.week2.functionaltesting.web.dto.AccountDto;
import nl.hu.assignments.term1.week2.functionaltesting.web.dto.HolderDto;
import nl.hu.assignments.term1.week2.functionaltesting.web.mapper.AccountMapper;
import nl.hu.assignments.term1.week2.functionaltesting.web.mapper.HolderMapper;
import org.iban4j.Iban;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rekeningen")
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountMapper accountMapper;
    private final HolderMapper holderMapper;
    private final AccountService accountService;
    private final HolderService holderService;

    @Cacheable(cacheNames = {"accounts"})
    @GetMapping
    @ApiOperation(
            value = "Retrieves all accounts",
            httpMethod = "GET",
            response = Page.class,
            code = 200
    )
    public Page<AccountDto> getAll(
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
            @RequestParam(value = "page", required = false, defaultValue = "0") final int page
    ) throws InterruptedException {
        // simulate long lasting operation
        Thread.sleep(3000);
        final var pageRequest = PageRequest.of(page, size);
        final var accounts = accountService.findAll(pageRequest);
        return accounts.map(accountMapper::toDto);
    }

    @Cacheable(cacheNames = {"holders"}, key = "#iban")
    @GetMapping("/{iban}/houder")
    public List<HolderDto> get(@PathVariable String iban) throws InterruptedException {
        // simulate long lasting operation
        Thread.sleep(3000);
        final var accountIban = Iban.valueOf(iban);
        final var holders = accountService.findAllHoldersByIban(accountIban);
        return holders.stream()
                .map(holderMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@Valid @RequestBody final AccountDto dto) {
        final var account = accountMapper.toModel(dto);
        final var savedAccount = accountService.save(account);
        return ResponseEntity.created(URI.create("/" + savedAccount.getIban())).build();
    }

    @PutMapping("/{iban}")
    public AccountDto update(@PathVariable String iban, @Valid @RequestBody final AccountDto dto) throws AccountAlreadyBlockedException, AccountNotFoundException {
        final var accountIban = Iban.valueOf(iban);
        final var updated = accountService.update(accountIban, dto);
        return accountMapper.toDto(updated);
    }

    @PutMapping("/{iban}/houder")
    public ResponseEntity<?> updateHolders(@PathVariable String iban, HolderDto dto) throws AccountNotFoundException, AccountBlockedException {
        final var accountIban = Iban.valueOf(iban);
        final var holder = holderService.findById(dto.getId());
        accountService.addHolder(accountIban, holder);
        return ResponseEntity.accepted().body(iban);
    }

    @DeleteMapping("/{iban}/houder/{id}")
    public ResponseEntity<?> removeHolder(@PathVariable String iban, @PathVariable UUID id) throws AccountNotFoundException {
        final var accountIban = Iban.valueOf(iban);
        final var holder = holderService.findById(id);
        accountService.removeHolder(accountIban, holder);
        return ResponseEntity.accepted().body(iban);
    }

    @Scheduled(fixedRate = 60 * 1000) // 60 seconds cache
    public void clearCaches() {
        System.out.println("Evicting cache");
        evictCaches();
    }

    @CacheEvict(cacheNames = {"accounts"}, allEntries = true)
    public void evictCaches() {
    }
}


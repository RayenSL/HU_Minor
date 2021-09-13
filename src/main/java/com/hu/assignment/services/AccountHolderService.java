package com.hu.assignment.services;

import com.hu.assignment.data.entity.AccountHolder;
import com.hu.assignment.data.repository.AccountHolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountHolderService {
    private final AccountHolderRepository holderRepository;

    public AccountHolder save(AccountHolder holder){
        return holderRepository.save(holder);
    }

    public void delete(String bsn){
        final var holder = holderRepository.findByBsn(bsn);
        if (holder.isPresent())
            holderRepository.delete(holder.get());
    }

    public AccountHolder findByBsn(String bsn) {
        final var optionalHolder = holderRepository.findByBsn(bsn);
        return optionalHolder.orElseThrow(() -> new EntityNotFoundException("Account holder with bsn " + bsn + " does not exist"));
    }

    public AccountHolder findById(UUID id) {
        final var optionalHolder = holderRepository.findById(id);
        return optionalHolder.orElseThrow(() -> new EntityNotFoundException("Account holder with id " + id + " does not exist"));
    }
}
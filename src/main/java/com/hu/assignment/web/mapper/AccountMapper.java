package com.hu.assignment.web.mapper;

import com.hu.assignment.data.entity.Account;
import com.hu.assignment.web.dto.AccountDto;
import com.hu.assignment.web.dto.AccountRequestDto;
import org.iban4j.Iban;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AccountMapper {

    public abstract AccountDto toDto(Account account);

    @Mappings({
            @Mapping(target = "iban", source = "iban")
    })
    public abstract Account toModel(AccountRequestDto dto, Iban iban);

    public abstract void updateModel(@MappingTarget Account account, AccountRequestDto dto);

    public String mapIban(Iban iban) {
        return iban.toString();
    }
}
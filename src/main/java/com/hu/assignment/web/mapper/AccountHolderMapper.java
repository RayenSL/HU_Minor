package com.hu.assignment.web.mapper;

import com.hu.assignment.data.entity.AccountHolder;
import com.hu.assignment.web.dto.AccountHolderDto;
import com.hu.assignment.web.dto.AccountHolderRequestDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountHolderMapper {
    AccountHolder toModel(AccountHolderDto dto);

    @Mappings({
            @Mapping(target = "bsn", source = "bsn")
    })
    AccountHolder toModel(AccountHolderRequestDto dto, String bsn);

    @Mappings({
            @Mapping(target = "name", source = "name")
    })
    void updateModel(@MappingTarget AccountHolder holder, AccountHolderRequestDto dto);

    AccountHolderDto toDto(AccountHolder holder);
}

package nl.hu.assignments.term1.week2.functionaltesting.web.mapper;

import nl.hu.assignments.term1.week2.functionaltesting.data.entity.Account;
import nl.hu.assignments.term1.week2.functionaltesting.web.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    AccountDto toDto(Account account);
    Account toModel(AccountDto dto);
}

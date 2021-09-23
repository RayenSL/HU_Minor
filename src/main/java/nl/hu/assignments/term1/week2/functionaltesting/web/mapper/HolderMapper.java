package nl.hu.assignments.term1.week2.functionaltesting.web.mapper;

import nl.hu.assignments.term1.week2.functionaltesting.data.entity.Holder;
import nl.hu.assignments.term1.week2.functionaltesting.web.dto.HolderDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HolderMapper {
    @Mapping(target = "bsn", ignore = true)
    Holder toModel(HolderDto dto);
    HolderDto toDto(Holder holder);
}

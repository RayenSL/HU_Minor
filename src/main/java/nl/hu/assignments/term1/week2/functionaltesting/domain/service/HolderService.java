package nl.hu.assignments.term1.week2.functionaltesting.domain.service;

import lombok.RequiredArgsConstructor;
import nl.hu.assignments.term1.week2.functionaltesting.data.entity.Holder;
import nl.hu.assignments.term1.week2.functionaltesting.data.repository.HolderRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolderService {
    private final HolderRepository holderRepository;

    public Holder findById(UUID id) {
        final var optionalHolder = holderRepository.findById(id);
        return optionalHolder.orElseThrow(() -> new EntityNotFoundException("Account holder with id " + id + " does not exist"));
    }
}

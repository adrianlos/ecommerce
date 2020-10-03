package com.github.skorczan.ecommerce.application;

import com.github.skorczan.ecommerce.domain.Author;
import com.github.skorczan.ecommerce.domain.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository repository;

    private final AuthorDomainDtoConverter converter;

    public AuthorDto save(String firstName, String lastName) {
        var entity = new Author();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity = repository.save(entity);
        return converter.convert(entity);
    }

    public void remove(long authorId) {
        repository.deleteById(authorId);
    }

    public Page<AuthorDto> list(Pageable page) {
        return repository.findAll(page).map(converter::convert);
    }

    public Optional<AuthorDto> get(Long authorId) {
        return repository.findById(authorId).map(converter::convert);
    }
}

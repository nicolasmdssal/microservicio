package com.example.demo.test;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    @Test
    void getAll_returnsFluxOfPersons() {
        Person p = new Person(1L, "Juan", "juan@example.com");
        when(repository.findAll()).thenReturn(Flux.just(p));

        StepVerifier.create(service.getAll())
                .expectNextMatches(person -> person.getName().equals("Juan") && person.getId().equals(1L))
                .verifyComplete();

        verify(repository).findAll();
    }

    @Test
    void getById_returnsMonoPerson() {
        Person p = new Person(2L, "Ana", "ana@example.com");
        when(repository.findById(2L)).thenReturn(Mono.just(p));

        StepVerifier.create(service.getById(2L))
                .expectNextMatches(person -> person.getEmail().equals("ana@example.com"))
                .verifyComplete();

        verify(repository).findById(2L);
    }

    @Test
    void create_savesAndReturnsPerson() {
        Person p = new Person(null, "Luis", "luis@example.com");
        Person saved = new Person(3L, "Luis", "luis@example.com");
        when(repository.save(p)).thenReturn(Mono.just(saved));

        StepVerifier.create(service.create(p))
                .expectNextMatches(person -> person.getId().equals(3L))
                .verifyComplete();

        verify(repository).save(p);
    }

    @Test
    void update_findsSavesAndReturnsUpdated() {
        Person existing = new Person(4L, "Old", "old@example.com");
        Person updateData = new Person(null, "New", "new@example.com");
        Person saved = new Person(4L, "New", "new@example.com");

        when(repository.findById(4L)).thenReturn(Mono.just(existing));
        when(repository.save(any(Person.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.update(4L, updateData))
                .expectNextMatches(p -> p.getName().equals("New") && p.getEmail().equals("new@example.com"))
                .verifyComplete();

        verify(repository).findById(4L);
        verify(repository).save(any(Person.class));
    }

    @Test
    void delete_delegatesToRepository() {
        when(repository.deleteById(5L)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(5L))
                .verifyComplete();

        verify(repository).deleteById(5L);
    }
}

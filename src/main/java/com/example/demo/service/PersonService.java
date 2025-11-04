package com.example.demo.service;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Flux<Person> getAll() {
        return repository.findAll();
    }

    public Mono<Person> getById(Long id) {
        return repository.findById(id);
    }

    public Mono<Person> create(Person p) {
        return repository.save(p);
    }

    public Mono<Person> update(Long id, Person p) {
        return repository.findById(id)
                .flatMap(existing -> {
                    existing.setName(p.getName());
                    existing.setEmail(p.getEmail());
                    return repository.save(existing);
                });
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}


package com.example.demo.test;

import com.example.demo.controller.PersonController;
import com.example.demo.model.Person;
import com.example.demo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@WebFluxTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private PersonService service;

    @Test
    void getAllEndpoint_returnsList() {
        Person p = new Person(10L, "Carlos", "carlos@example.com");
        when(service.getAll()).thenReturn(Flux.just(p));

        webClient.get().uri("/api/persons")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Person.class)
                .hasSize(1)
                .consumeWith(response -> {
                    List<Person> body = response.getResponseBody();
                    assertNotNull(body, "El cuerpo no debe ser nulo");
                    assertEquals(1, body.size(), "Debe venir exactamente 1 persona");
                    Person first = body.get(0);
                    assertEquals(10L, first.getId());
                    assertEquals("Carlos", first.getName());
                    assertEquals("carlos@example.com", first.getEmail());
                });

        verify(service).getAll();
    }

    @Test
    void getByIdEndpoint_returnsPerson() {
        Person p = new Person(11L, "Maria", "maria@example.com");
        when(service.getById(11L)).thenReturn(Mono.just(p));

        webClient.get().uri("/api/persons/{id}", 11)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Person.class)
                .consumeWith(response -> {
                    Person body = response.getResponseBody();
                    assertNotNull(body, "El cuerpo no debe ser nulo");
                    assertEquals(11L, body.getId());
                    assertEquals("Maria", body.getName());
                    assertEquals("maria@example.com", body.getEmail());
                });

        verify(service).getById(11L);
    }
}



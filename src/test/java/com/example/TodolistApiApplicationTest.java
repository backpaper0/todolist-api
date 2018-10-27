package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.entity.Todo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class TodolistApiApplicationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void test() throws Exception {

        final TodolistClient client = new TodolistClient(testRestTemplate);

        assertIterableEquals(Collections.emptyList(), client.all());

        final Todo entity1 = client.create("foo");
        assertAll(() -> {
            assertNotNull(entity1.getId());
            assertEquals("foo", entity1.getContent());
            assertEquals(false, entity1.isDone());
        });

        final Todo entity2 = client.create("bar");
        final Todo entity3 = client.create("baz");

        assertIterableEquals(Arrays.asList(entity3, entity2, entity1), client.all());

        client.updateDone(entity2.getId(), true);
        assertIterableEquals(
                Arrays.asList(
                        entity3,
                        new Todo(entity2.getId(), entity2.getContent(), true),
                        entity1),
                client.all());

        client.deleteDone();
        assertIterableEquals(
                Arrays.asList(
                        entity3,
                        entity1),
                client.all());
    }

    private static class TodolistClient {

        private final TestRestTemplate client;

        TodolistClient(final TestRestTemplate client) {
            this.client = client;
        }

        List<Todo> all() {
            final RequestEntity<?> request = RequestEntity.get(URI.create("/todolist"))
                    .build();
            final ResponseEntity<List<Todo>> response = client.exchange(request,
                    new ParameterizedTypeReference<List<Todo>>() {
                    });
            return response.getBody();
        }

        Todo create(final String content) {
            final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("content", content);
            final RequestEntity<?> request = RequestEntity.post(URI.create("/todolist"))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body);
            final ResponseEntity<Todo> response = client.exchange(request, Todo.class);
            return response.getBody();
        }

        void updateDone(final Long id, final boolean done) {
            final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("done", String.valueOf(done));
            final RequestEntity<?> request = RequestEntity.post(URI.create("/todolist/" + id))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body);
            client.exchange(request, Void.class);
        }

        void deleteDone() {
            final RequestEntity<?> request = RequestEntity.post(URI.create("/todolist/_delete"))
                    .build();
            client.exchange(request, Void.class);
        }
    }
}

package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        final List<Todo> testData = client.all();
        final int maxId = testData.stream().map(Todo::getId).max(Integer::compare).orElse(0);

        //create
        final Todo entity1 = client.create(maxId + 1, "foo");
        assertEquals(new Todo(maxId + 1, "foo", false), entity1);

        //all
        final List<Todo> entities1 = Stream.concat(Stream.of(entity1), testData.stream())
                .collect(Collectors.toList());
        assertIterableEquals(entities1, client.all());

        //updateDone
        client.updateDone(entity1.getId(), true);
        final List<Todo> entities2 = Stream
                .concat(Stream.of(new Todo(entity1.getId(), entity1.getContent(), true)),
                        testData.stream())
                .collect(Collectors.toList());
        assertIterableEquals(entities2, client.all());

        // deleteDone
        client.deleteDone();
        final List<Todo> entities3 = entities2.stream().filter(a -> a.isDone() == false)
                .collect(Collectors.toList());
        assertIterableEquals(entities3, client.all());
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

        Todo create(final Integer id, final String content) {
            final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("id", String.valueOf(id));
            body.add("content", content);
            final RequestEntity<?> request = RequestEntity.post(URI.create("/todolist"))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body);
            final ResponseEntity<Todo> response = client.exchange(request, Todo.class);
            return response.getBody();
        }

        void updateDone(final Integer id, final boolean done) {
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

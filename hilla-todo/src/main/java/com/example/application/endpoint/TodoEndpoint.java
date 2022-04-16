
package com.example.application.endpoint;

import java.util.List;

import com.example.application.entity.Todo;
import com.example.application.repository.TodoRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import dev.hilla.Endpoint;
import dev.hilla.Nonnull;

@Endpoint
@AnonymousAllowed
public class TodoEndpoint {
    private TodoRepository repository;

    public TodoEndpoint(TodoRepository repository) {
        this.repository = repository;
    }

    public @Nonnull List<com.example.application.entity.Todo> findAll() {
        return repository.findAll();
    }

    public Todo save(Todo todo) {
        return repository.save(todo);
    }
}
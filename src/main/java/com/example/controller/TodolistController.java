package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.TodoDao;
import com.example.entity.Todo;

@RestController
@RequestMapping("/todolist")
public class TodolistController {

    private final TodoDao dao;

    public TodolistController(final TodoDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<Todo> all() {
        return dao.selectAll();
    }

    @PostMapping
    public Todo create(@RequestParam final Integer id, @RequestParam final String content) {
        final Todo entity = Todo.create(id, content);
        return dao.insert(entity).getEntity();
    }

    @PostMapping("/{id}")
    public void updateDone(@PathVariable final Integer id, @RequestParam final boolean done) {
        dao.updateDoneById(id, done);
    }

    @PostMapping("/_delete")
    public void deleteDone() {
        dao.deleteDone();
    }
}

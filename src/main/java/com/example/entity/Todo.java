package com.example.entity;

import java.util.Objects;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

@Entity(immutable = true)
public final class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Integer id;
    private final String content;
    private final boolean done;

    public Todo(final Integer id, final String content, final boolean done) {
        this.id = id;
        this.content = content;
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, done);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final Todo other = (Todo) obj;
        return id.equals(other.id) && content.equals(other.content) && done == other.done;
    }

    public static Todo create(final String content) {
        return new Todo(null, content, false);
    }
}

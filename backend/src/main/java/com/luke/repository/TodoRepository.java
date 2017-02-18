package com.luke.repository;

import com.luke.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
//I use JpaRepository instead of for example CrudRepository to have List<T> instead of Iterable<T> returned by default by findAll() method
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    TodoEntity findByName(String name);
}

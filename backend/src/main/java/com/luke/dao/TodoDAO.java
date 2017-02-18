package com.luke.dao;

import com.luke.entity.TodoEntity;

import java.util.List;

public interface TodoDAO {
    TodoEntity findById(long id);
    TodoEntity findByName(String name);
    List<TodoEntity> findAll();
    TodoEntity saveOrUpdate(TodoEntity entity);
    void delete(TodoEntity entity);
    void delete(long id);
}

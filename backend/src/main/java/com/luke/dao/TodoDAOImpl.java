package com.luke.dao;

import com.luke.entity.TodoEntity;
import com.luke.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TodoDAOImpl implements TodoDAO{

    @Autowired
    TodoRepository todoRepository;

    @Override
    public TodoEntity findById(long id) {
        return todoRepository.findOne(id);
    }

    @Override
    public TodoEntity findByName(String name){
        return todoRepository.findByName(name);
    }

    @Override
    public List<TodoEntity> findAll() {
        return todoRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
    }

    @Override
    public TodoEntity saveOrUpdate(TodoEntity entity) {
        return todoRepository.save(entity);
    }

    @Override
    public void delete(TodoEntity entity) {
        todoRepository.delete(entity);
    }

    @Override
    public void delete(long id) {
        todoRepository.delete(id);
    }

}

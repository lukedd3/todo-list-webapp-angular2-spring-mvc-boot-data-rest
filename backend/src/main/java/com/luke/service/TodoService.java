package com.luke.service;

import com.luke.dao.TodoDAO;
import com.luke.entity.TodoEntity;
import com.luke.type.TodoStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TodoService {

    @Autowired
    TodoDAO todoDAO;

    public List<TodoEntity> getAll() {
        return todoDAO.findAll();
    }

    public boolean add(TodoEntity entity) {
        if(todoDAO.findByName(entity.getName())!=null) return false;

        entity.setStatus(TodoStatusType.OPEN);
        todoDAO.saveOrUpdate(entity);
        return true;
    }

    public boolean edit(TodoEntity entity) {
        if(todoDAO.findById(entity.getId()) == null) return false;

        TodoEntity existingTodoEntity = todoDAO.findByName(entity.getName());
        if(existingTodoEntity!=null && existingTodoEntity.getId()!=entity.getId()) return false;

        if(entity.getStatus()==null) return false;

        todoDAO.saveOrUpdate(entity);
        return true;
    }

    public boolean delete(long id) {
        if(todoDAO.findById(id) == null) return false;

        todoDAO.delete(id);
        return true;
    }

}

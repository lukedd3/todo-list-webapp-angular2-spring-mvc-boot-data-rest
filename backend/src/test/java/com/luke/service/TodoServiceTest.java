package com.luke.service;

import com.luke.dao.TodoDAO;
import com.luke.entity.TodoEntity;
import com.luke.type.TodoStatusType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static  org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoDAO todoDAO;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void GetAllTest_When_MethodCall_ExpectTodoEntityList(){
        TodoEntity todo1 = new TodoEntity();
        todo1.setId(1);
        todo1.setStatus(TodoStatusType.OPEN);
        todo1.setName("Test todo1 name");

        TodoEntity todo2 = new TodoEntity();
        todo1.setId(2);
        todo1.setStatus(TodoStatusType.CLOSED);
        todo1.setName("Test todo2 name");

        List<TodoEntity> existingEntities = Arrays.asList(todo1, todo2);

        when(todoDAO.findAll()).thenReturn(existingEntities);

        assertThat(todoService.getAll()).hasSize(2).contains(todo1, todo2);
    }

    @Test
    public void AddTest_When_GivenNewNotExistingBeforeTodoEntityWithNameOnly_ExpectTrue(){
        TodoEntity todoToAdd = new TodoEntity();
        todoToAdd.setName("Test todoToAdd name");

        assertThat(todoService.add(todoToAdd)).isTrue();
    }

    @Test
    public void AddTest_When_GivenTodoEntityWithNameThatExistedBefore_ExpectFalse(){
        TodoEntity alreadyExistingTodo = new TodoEntity();
        alreadyExistingTodo.setId(1);
        alreadyExistingTodo.setStatus(TodoStatusType.OPEN);
        alreadyExistingTodo.setName("Test already existing name");

        TodoEntity todoToAdd = new TodoEntity();
        todoToAdd.setName(alreadyExistingTodo.getName());

        when(todoDAO.findByName(alreadyExistingTodo.getName())).thenReturn(alreadyExistingTodo);

        assertThat(todoService.add(todoToAdd)).isFalse();
    }

    @Test
    public void EditTest_When_GivenAlreadyExistingTodoEntityWithChangedName_ExpectTrue(){
        TodoEntity alreadyExistingTodo = new TodoEntity();
        alreadyExistingTodo.setId(1);
        alreadyExistingTodo.setStatus(TodoStatusType.OPEN);
        alreadyExistingTodo.setName("Test already existing name");

        TodoEntity todoToEdit = new TodoEntity();
        todoToEdit.setId(1);
        todoToEdit.setStatus(TodoStatusType.OPEN);
        todoToEdit.setName("Test new name for already existing todo");

        when(todoDAO.findById(alreadyExistingTodo.getId())).thenReturn(alreadyExistingTodo);

        assertThat(todoService.edit(todoToEdit)).isTrue();
    }

    @Test
    public void EditTest_When_GivenTodoEntityThatNotExistedBefore_ExpectFalse() {
        TodoEntity todoToEdit = new TodoEntity();
        todoToEdit.setId(1);
        todoToEdit.setStatus(TodoStatusType.OPEN);
        todoToEdit.setName("Test todoToEdit name todo");

        when(todoDAO.findById(todoToEdit.getId())).thenReturn(null);

        assertThat(todoService.edit(todoToEdit)).isFalse();
    }

    @Test
    public void EditTest_When_GivenTodoEntityWithTheSameNameAsAlreadyExistingTodoEntity_ExpectFalse() {
        TodoEntity alreadyExistingTodo = new TodoEntity();
        alreadyExistingTodo.setId(1);
        alreadyExistingTodo.setStatus(TodoStatusType.OPEN);
        alreadyExistingTodo.setName("Test already existing name");

        TodoEntity todoToEdit = new TodoEntity();
        todoToEdit.setId(2);
        todoToEdit.setStatus(TodoStatusType.OPEN);
        todoToEdit.setName(alreadyExistingTodo.getName());

        when(todoDAO.findById(todoToEdit.getId())).thenReturn(todoToEdit);
        when(todoDAO.findByName(alreadyExistingTodo.getName())).thenReturn(alreadyExistingTodo);

        assertThat(todoService.edit(todoToEdit)).isFalse();
    }

    @Test
    public void EditTest_When_GivenTodoEntityWithNullStatus_ExpectFalse() {
        TodoEntity alreadyExistingTodo = new TodoEntity();
        alreadyExistingTodo.setId(1);
        alreadyExistingTodo.setStatus(TodoStatusType.OPEN);
        alreadyExistingTodo.setName("Test already existing name");

        TodoEntity todoToEdit = new TodoEntity();
        todoToEdit.setId(1);
        todoToEdit.setStatus(null);
        todoToEdit.setName("Test new name for already existing todo");

        when(todoDAO.findById(todoToEdit.getId())).thenReturn(alreadyExistingTodo);
        when(todoDAO.findByName(alreadyExistingTodo.getName())).thenReturn(alreadyExistingTodo);

        assertThat(todoService.edit(todoToEdit)).isFalse();
    }

    @Test
    public void DeleteTest_When_GivenExistingTodoEntityId_ExpectTrue(){
        TodoEntity alreadyExistingTodo = new TodoEntity();
        alreadyExistingTodo.setId(1);
        alreadyExistingTodo.setStatus(TodoStatusType.OPEN);
        alreadyExistingTodo.setName("Test already existing name");

        when(todoDAO.findById(alreadyExistingTodo.getId())).thenReturn(alreadyExistingTodo);

        assertThat(todoService.delete(alreadyExistingTodo.getId())).isTrue();
    }

    @Test
    public void DeleteTest_When_GivenNonExistingTodoEntityId_ExpectFalse(){
        long nonExistingId = 1;

        when(todoDAO.findById(nonExistingId)).thenReturn(null);

        assertThat(todoService.delete(nonExistingId)).isFalse();
    }

}

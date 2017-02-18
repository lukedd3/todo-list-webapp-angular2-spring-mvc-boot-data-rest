package com.luke.controller;

import com.luke.entity.TodoEntity;
import com.luke.type.TodoStatusType;
import com.luke.util.IntegrationTestUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-integtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TodoControllerIntegrationTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

    List<TodoEntity> improperNameTodoList;
    List<TodoEntity> properNameTodoList;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Before
	public void prepareData(){
        TodoEntity nullNameTodo = new TodoEntity();

        TodoEntity emptyNameTodo = new TodoEntity();
        emptyNameTodo.setName("");

        TodoEntity emptyNameButWithSpacesTodo = new TodoEntity();
        emptyNameButWithSpacesTodo.setName("    ");

        TodoEntity name101charsTodo = new TodoEntity();
        name101charsTodo.setName(RandomStringUtils.randomAlphanumeric(101));

        TodoEntity name500CharsTodo = new TodoEntity();
        name500CharsTodo.setName(RandomStringUtils.randomAlphanumeric(500));

        improperNameTodoList = Arrays.asList(nullNameTodo, emptyNameTodo, emptyNameButWithSpacesTodo, name101charsTodo, name500CharsTodo);

        TodoEntity name100charsTodo = new TodoEntity();
        name100charsTodo.setName(RandomStringUtils.randomAlphanumeric(100));

        TodoEntity nameOneCharTodo = new TodoEntity();
        nameOneCharTodo.setName("a");

        properNameTodoList = Arrays.asList(name100charsTodo, nameOneCharTodo);
    }

	@Test
	public void isAppAliveTest() throws Exception{
		this.mockMvc.perform(get("/todo").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	public void basicRestServicesTest() throws Exception {
        TodoEntity basicTodoEntity = new TodoEntity();
        basicTodoEntity.setName("todo task description 1");

        TodoEntity modifiedTodoEntity = new TodoEntity();
        modifiedTodoEntity.setName("todo task description 1 mod");
        modifiedTodoEntity.setStatus(TodoStatusType.CLOSED);

        //add test
        this.mockMvc.perform(
                post("/new")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(IntegrationTestUtil.convertObjectToJsonString(basicTodoEntity))
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        MvcResult result = this.mockMvc.perform(get("/todo").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(basicTodoEntity.getName())))
                .andReturn();

        //obtaining added _todo id which is necessary for modify and delete test
        List<TodoEntity> todoList = IntegrationTestUtil.jsonToTodoListObject(result.getResponse().getContentAsString());
        modifiedTodoEntity.setId(todoList.get(0).getId());

        //modify test
        this.mockMvc.perform(
                put("/edit")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(IntegrationTestUtil.convertObjectToJsonString(modifiedTodoEntity))
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        this.mockMvc.perform(get("/todo").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(modifiedTodoEntity.getName())))
                .andExpect(jsonPath("$[0].status", is(modifiedTodoEntity.getStatus().toString())));

        //delete test
        this.mockMvc.perform(
                delete("/delete/{id}", modifiedTodoEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        this.mockMvc.perform(get("/todo").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    public void addImproperNameTest() throws Exception{
        for(TodoEntity improperNameTodoEntity: improperNameTodoList){
            this.mockMvc.perform(
                    post("/new")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(IntegrationTestUtil.convertObjectToJsonString(improperNameTodoEntity))
            )
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"));
        }
    }

    public void addProperBoundaryNameTest() throws Exception{
        for(TodoEntity properNameTodoEntity: properNameTodoList){
            this.mockMvc.perform(
                    post("/new")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(IntegrationTestUtil.convertObjectToJsonString(properNameTodoEntity))
            )
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }
    }

    @Test
    public void addPolishCharsTest() throws Exception{
        TodoEntity polishNameTodoEntity = new TodoEntity();
        polishNameTodoEntity.setName("ĄąĆćĘęŁłŃńÓóŚśŹźŻż");

        this.mockMvc.perform(
                post("/new")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(IntegrationTestUtil.convertObjectToJsonString(polishNameTodoEntity))
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        this.mockMvc.perform(get("/todo").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(polishNameTodoEntity.getName())));
    }

    @Test
    public void editImproperIdTest() throws Exception {
        TodoEntity notPreviouslyAddedTodo = new TodoEntity();
        notPreviouslyAddedTodo.setId(1);
        notPreviouslyAddedTodo.setStatus(TodoStatusType.OPEN);
        notPreviouslyAddedTodo.setName("not previously added todo description");

        this.mockMvc.perform(
                put("/edit")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(IntegrationTestUtil.convertObjectToJsonString(notPreviouslyAddedTodo))
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

	@Test
	public void editImproperNameTest() throws Exception {

        TodoEntity basicTodoEntity = new TodoEntity();
        basicTodoEntity.setName("todo task description 1");

        TodoEntity modifiedTodoEntity = new TodoEntity();
        modifiedTodoEntity.setStatus(TodoStatusType.OPEN);

        //add
        this.mockMvc.perform(
                post("/new")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(IntegrationTestUtil.convertObjectToJsonString(basicTodoEntity))
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        MvcResult result = this.mockMvc.perform(get("/todo").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(basicTodoEntity.getName())))
                .andReturn();

        //obtaining added _todo id which is necessary for modify test
        List<TodoEntity> todoList = IntegrationTestUtil.jsonToTodoListObject(result.getResponse().getContentAsString());
        modifiedTodoEntity.setId(todoList.get(0).getId());

        //modify
        for(TodoEntity improperNameTodoEntity: improperNameTodoList){
            modifiedTodoEntity.setName(improperNameTodoEntity.getName());
            this.mockMvc.perform(
                    post("/new")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(IntegrationTestUtil.convertObjectToJsonString(modifiedTodoEntity))
            )
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"));
        }
	}

    @Test
    public void deleteImproperIdTest() throws Exception {
        long notPreviouslyAddedTodoId = 1;

        this.mockMvc.perform(
                delete("/delete/{id}", notPreviouslyAddedTodoId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

}

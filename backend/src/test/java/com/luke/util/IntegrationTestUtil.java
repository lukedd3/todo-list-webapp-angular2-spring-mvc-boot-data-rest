package com.luke.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.luke.entity.TodoEntity;

import java.io.IOException;
import java.util.List;

public class IntegrationTestUtil {

    public static String convertObjectToJsonString (Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        return mapper.writeValueAsString(object);
    }

    public static List<TodoEntity> jsonToTodoListObject(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(jsonString, TypeFactory.defaultInstance().constructCollectionType(List.class, TodoEntity.class));
    }
}

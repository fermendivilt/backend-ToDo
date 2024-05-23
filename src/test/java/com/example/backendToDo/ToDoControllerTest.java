package com.example.backendToDo;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.backendToDo.todo.ToDo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.backendToDo.todo.Priority;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ToDoControllerTest {

	@Autowired
	private MockMvc mvc;


	@Test @Order(1)
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Backend is active")));
	}

	@Test @Order(2)
	public void postToDos() throws Exception {
		ToDo postTest = new ToDo();
		postTest.name = "Post test";
		postTest.priority = Priority.MEDIUM;
		String creationDateComparison = postTest.creationDate;
		String postTestJSON = new ObjectMapper().writeValueAsString(postTest);

		mvc.perform(MockMvcRequestBuilders.post("/todos")
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.content(postTestJSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Post test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("MEDIUM"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value(creationDateComparison));
	}

	@Test @Order(3)
	public void populateToDos() throws Exception {
		ToDo postTest = null;

		for (int index = 1; index <= 15; index++) {
			postTest = new ToDo();
			postTest.name = "Post test " + (index + 1);
			postTest.priority = Priority.MEDIUM;
			String postTestJSON1 = new ObjectMapper().writeValueAsString(postTest);
	
			mvc.perform(MockMvcRequestBuilders.post("/todos")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(postTestJSON1))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(index))
					.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Post test " + (index + 1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("MEDIUM"));
		}
	}
}
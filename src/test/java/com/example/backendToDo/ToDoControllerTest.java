package com.example.backendToDo;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.backendToDo.todo.ToDo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.backendToDo.todo.GetAllOptions;
import com.example.backendToDo.todo.GetAllResponseDTO;
import com.example.backendToDo.todo.Priority;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ToDoControllerTest {

	@Autowired
	private MockMvc mvc;

	static List<ToDo> localList = new ArrayList<>();

	@Test
	@Order(1)
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("Backend is active")));
	}

	@Test
	@Order(2)
	public void postToDo() throws Exception {
		ToDo postTest = new ToDo(
				"Post test",
				Priority.MEDIUM,
				LocalDateTime.now(),
				null);

		String postTestJSON = new ObjectMapper().writeValueAsString(postTest);

		mvc.perform(MockMvcRequestBuilders.post("/todos")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(postTestJSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Post test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("MEDIUM"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value(postTest.creationDate));

		localList.add(postTest);
	}

	@Test
	@Order(3)
	public void populateToDos() throws Exception {
		ToDo postTest = null;

		for (int index = 1; index <= 15; index++) {
			postTest = new ToDo(
					"Post test " + (index + 1),
					Priority.LOW,
					LocalDateTime.now(),
					null);

			String postTestJSON1 = new ObjectMapper().writeValueAsString(postTest);

			mvc.perform(MockMvcRequestBuilders.post("/todos")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(postTestJSON1))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(index))
					.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Post test " + (index + 1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("LOW"));

			localList.add(postTest);
		}
	}

	@Test
	@Order(4)
	public void getAll() throws Exception {
		GetAllOptions options = new GetAllOptions();
		String optionsJSON = new ObjectMapper().writeValueAsString(options);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/todos")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(optionsJSON))
				.andExpect(status().isOk())
				.andReturn();

		GetAllResponseDTO content = new ObjectMapper().readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<GetAllResponseDTO>() {
				});

		if (content.toDos.isEmpty() || content.toDos.size() != 10)
			throw new Exception("List size unexpected, should be 10");

		if (content.pages != 2)
			throw new Exception("2 pages expected");
	}

	@Test
	@Order(5)
	public void getAllMedium() throws Exception {
		GetAllOptions options = new GetAllOptions();
		options.priorityFilter = Priority.MEDIUM;

		String optionsJSON = new ObjectMapper().writeValueAsString(options);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/todos")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(optionsJSON))
				.andExpect(status().isOk())
				.andReturn();

		GetAllResponseDTO content = new ObjectMapper().readValue(
				result.getResponse().getContentAsString(),
				new TypeReference<GetAllResponseDTO>() {
				});

		if (content.toDos.isEmpty() || content.toDos.size() != 1)
			throw new Exception("List size unexpected, should be 1");

		if (content.pages != 1)
			throw new Exception("1 page expected");

		ToDo fetchedToDo = content.toDos.get(0);
		ToDo localToDo = localList.get(0);
		if (fetchedToDo.id != 0 || // id asigned from backend
				!fetchedToDo.name.equals(localToDo.name) ||
				!fetchedToDo.priority.equals(localToDo.priority) ||
				!(fetchedToDo.isDone == localToDo.isDone) ||
				!fetchedToDo.creationDate.equals(localToDo.creationDate) ||
				fetchedToDo.dueDate != localToDo.dueDate || // Value can be null, so equals() can crash
				fetchedToDo.doneDate != localToDo.doneDate)
			throw new Exception("Object fetched is not the same as created");
	}

	@Test
	@Order(6)
	public void setToDoAsDone() throws Exception {
		int id = 11;

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/todos/" + id + "/done")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		ToDo updatedToDo = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ToDo.class);

		GetAllOptions options = new GetAllOptions();
		options.nameFilter = updatedToDo.name;
		String optionsJSON = new ObjectMapper().writeValueAsString(options);

		MvcResult checkInList = mvc.perform(MockMvcRequestBuilders.get("/todos")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(optionsJSON))
				.andExpect(status().isOk())
				.andReturn();

		GetAllResponseDTO content = new ObjectMapper().readValue(
				checkInList.getResponse().getContentAsString(),
				new TypeReference<GetAllResponseDTO>() {
				});
		ToDo toDoFromGet = content.toDos.get(0);

		if(!toDoFromGet.isDone)
			throw new Exception("ToDo 11 was not set to done");
		
		if(toDoFromGet.doneDate == null || !toDoFromGet.doneDate.equals(updatedToDo.doneDate))
			throw new Exception("ToDo 11 doneDate was not updated. \n|"+toDoFromGet.doneDate+"| vs |"+updatedToDo.doneDate + "|");
	}
}
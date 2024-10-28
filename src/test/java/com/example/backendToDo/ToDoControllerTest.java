package com.example.backendToDo;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.backendToDo.todo.*;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ToDoControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	ToDoController controller;

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
	}

	@Test
	@Order(3)
	public void populateToDos() throws Exception {
		for (int index = 1; index <= 15; index++) {
			ToDo postTest = new ToDo(
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
		}
	}

	@Test
	@Order(4)
	public void getAll() throws Exception {
		GetAllResponseDTO content = controller.getToDos(1, GetAllStateFilter.NONE, null, GetAllSortingDirection.NONE, GetAllSortingDirection.NONE);

		if (content.toDos.size() != 10)
			throw new Exception("List size unexpected, should be 10. Size is " + content.toDos.size());

		if (content.pages != 2)
			throw new Exception("2 pages expected. Pages are " + content.pages);
	}

	@Test
	@Order(5)
	public void setToDoAsDone() throws Exception {
		int id = 11;
		ToDo updatedToDo = controller.postDone(String.valueOf(id));

		GetAllOptions options = new GetAllOptions();
		options.nameFilter = updatedToDo.name;
		GetAllResponseDTO content = controller.getToDos(options.pageNumber, options.stateFilter, options.nameFilter, options.sortingDueDate, options.sortingPriority);

		ToDo toDoFromGet = content.toDos.getFirst();

		if (!toDoFromGet.isDone)
			throw new Exception("ToDo 11 was not set to done");

		if (toDoFromGet.doneDate == null || !toDoFromGet.doneDate.equals(updatedToDo.doneDate))
			throw new Exception("ToDo 11 doneDate was not updated. \n|" + toDoFromGet.doneDate + "| vs |"
					+ updatedToDo.doneDate + "|");
	}

	@Test
	@Order(6)
	public void putToDo() throws Exception {
		ToDo modified = new ToDo();
		modified.id = 8;
		modified.name = "Put ToDo with validations";
		modified.priority = Priority.HIGH;
		modified.dueDate = LocalDateTime.of(2024, 5, 25, 0, 0, 0).toString();

		ToDo  updatedToDo = controller.putToDo(String.valueOf(modified.id), modified);

		GetAllOptions options = new GetAllOptions();
		options.nameFilter = modified.name;
		GetAllResponseDTO content = controller.getToDos(options.pageNumber, options.stateFilter, options.nameFilter, options.sortingDueDate, options.sortingPriority);

		ToDo toDoFromGet = content.toDos.getFirst();

		if (!toDoFromGet.name.equals(modified.name))
			throw new Exception("ToDo 8 name not modified");
		if (!toDoFromGet.dueDate.equals(modified.dueDate))
			throw new Exception("ToDo 8 dueDate not modified");
		if (!toDoFromGet.priority.equals(modified.priority))
			throw new Exception("ToDo 8 priority not modified");
	}

	@Test
	@Order(7)
	public void setToDoAsUndone() throws Exception {
		int id = 11;
		ToDo updatedToDo = controller.putUndone(String.valueOf(id));

		GetAllOptions options = new GetAllOptions();
		options.nameFilter = updatedToDo.name;
		GetAllResponseDTO content = controller.getToDos(options.pageNumber, options.stateFilter, options.nameFilter, options.sortingDueDate, options.sortingPriority);

		ToDo toDoFromGet = content.toDos.getFirst();

		if (toDoFromGet.isDone)
			throw new Exception("ToDo 11 was not set to undone");
	}

	@Test
	@Order(8)
	public void deleteToDo() throws Exception {
		int id = 5;
		controller.deleteToDo(String.valueOf(id));

		// Verify that the To Do item is deleted
		GetAllOptions options = new GetAllOptions();
		GetAllResponseDTO content = controller.getToDos(options.pageNumber, options.stateFilter, options.nameFilter, options.sortingDueDate, options.sortingPriority);

		if (content.toDos.stream().anyMatch(toDo -> toDo.id == id))
			throw new Exception("ToDo " + id + " was not deleted");
	}
}
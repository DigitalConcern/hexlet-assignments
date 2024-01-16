package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testCreate() throws Exception {
        Task task = generateTask();
        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(task));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        assertThatJson(result.getResponse().getContentAsString()).and(
                a -> a.node("title").isEqualTo(task.getTitle()),
                a -> a.node("description").isEqualTo(task.getDescription())
        );
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        Task task = generateTask();
        taskRepository.save(task);

        var result = mockMvc.perform(get("/tasks/{id}", task.getId()))
               .andExpect(status().isOk())
               .andExpect(content().json(om.writeValueAsString(task)))
               .andReturn();

        assertThatJson(result.getResponse().getContentAsString()).and(
               a -> a.node("title").isEqualTo(task.getTitle()),
               a -> a.node("description").isEqualTo(task.getDescription())
        );
    }

    @Test
    public void testUpdate() throws Exception {
        Task task = generateTask();
        taskRepository.save(task);

        task.setTitle("title");
        task.setDescription("body");

        var request = put("/tasks/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(task));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        assertThatJson(result.getResponse().getContentAsString()).and(
                a -> a.node("title").isEqualTo("title"),
                a -> a.node("description").isEqualTo("body")
        );
    }

    @Test
    public void testDelete() throws Exception {
        var task = generateTask();
        taskRepository.save(task);

        mockMvc.perform(delete("/tasks/{id}", task.getId()))
               .andExpect(status().isOk());

        assertThat(taskRepository.findById(task.getId()).orElse(null)).isEqualTo(null);
    }

    private Task generateTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }
}

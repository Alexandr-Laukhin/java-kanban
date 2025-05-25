package classes.handlers;

import classes.Epic;
import classes.Managers;
import classes.SubTask;
import com.google.gson.Gson;
import main.HttpTaskServer;
import main.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubTaskHandlerTest {

    private HttpTaskServer server;
    private HttpClient httpClient;
    private final String baseUrl = "http://localhost:8080";
    private TaskManager taskManager = Managers.getDefault();
    Gson gson = new Gson();
    Epic epic;
    SubTask subTask;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer(taskManager);
        epic = new Epic("Test epic", "Test epic description");
        taskManager.createEpic(epic);
        subTask = new SubTask("Test subTask", "Test subTask description", 1);
        taskManager.createSubTask(subTask);
        server.start();
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @AfterEach
    void tearDown() {
        server.stop();
        taskManager.deleteEpics();
    }

    @Test
    void testSubTasksEndpointPOST() throws Exception {
        String jsonSubTask = gson.toJson(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/subTasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubTask))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("Test subTask"));
    }

    @Test
    void testSubTasksEndpointGET() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/subTasks"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test subTask"));
    }

    @Test
    void testSubTaskEndpointDELETE() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/subTasks/" + subTask.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(taskManager.getSubTasks().isEmpty());
    }
}

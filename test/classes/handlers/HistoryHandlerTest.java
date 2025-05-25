package classes.handlers;

import classes.Managers;
import classes.Task;
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

public class HistoryHandlerTest {
    private HttpTaskServer server;
    private HttpClient httpClient;
    private final String baseUrl = "http://localhost:8080";
    private TaskManager taskManager = Managers.getDefault();
    Task task;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer(taskManager);
        task = new Task("Test task", "Test task description");
        taskManager.createTask(task);
        server.start();
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @AfterEach
    void tearDown() {
        server.stop();
        taskManager.deleteTasks();
        taskManager.deleteEpics();
    }

    @Test
    void testHistoryEndpointGET() throws Exception {
        taskManager.getTaskByID(task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/history"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test task"));
    }
}

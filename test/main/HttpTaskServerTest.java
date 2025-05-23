package main;

import classes.Epic;
import classes.Managers;
import classes.SubTask;
import classes.Task;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private HttpClient httpClient;
    private final String baseUrl = "http://localhost:8080";
    private TaskManager taskManager = Managers.getDefault();
    Gson gson = new Gson();

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer(taskManager);
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
    void testTasksEndpointGET() throws Exception {
        Task task = new Task("Test task", "Test task description");
        taskManager.createTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test task"));
    }

    @Test
    void testTasksEndpointPOST() throws Exception {

        Task task = new Task("Test task", "Test task description");
        taskManager.createTask(task);
        String jsonTask = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("Test task"));
    }

    @Test
    void testTasksEndpointDELETE() throws Exception {

        Task task = new Task("Test task", "Test task description");
        taskManager.createTask(task);
        int taskId = task.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/tasks/" + taskId))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Task deleted"));

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/tasks"))
                .GET()
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());
        assertTrue(response2.body().contains("[]"));  // тут захотелось проверить, что мне не просто вернули сообщение
        // "Task deleted", а что она реально удалена. Если лишняя проверка, напиши, удалю
    }

    @Test
    void testEpicsEndpointPOST() throws Exception {

        Epic epic = new Epic("Test epic", "Test epic description");
        String jsonEpic = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test epic"));
    }

    @Test
    void testEpicsEndpointGET() throws Exception {

        Epic epic = new Epic("Test epic", "Test epic description");
        taskManager.createEpic(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/epics"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test epic"));
    }

    @Test
    void testEpicsEndpointDELETE() throws Exception {

        Epic epic = new Epic("Test epic", "Test epic description");
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/epics/" + epicId))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Epic deleted"));

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/epics"))
                .GET()
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());
        assertTrue(response2.body().contains("[]"));
    }

    @Test
    void testSubTasksEndpointPOST() throws Exception {

        Epic epic = new Epic("Test epic", "Test epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test subTask", "Test subTask description", epic.getId());
        String jsonSubTask = gson.toJson(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/subTasks"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubTask))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test subTask"));
    }

    @Test
    void testSubTasksEndpointGET() throws Exception {

        Epic epic = new Epic("Test epic", "Test epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test subTask", "Test subTask description", epic.getId());
        taskManager.createSubTask(subTask);

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

        Epic epic = new Epic("Test epic", "Test epic description");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Test subTask", "Test subTask description", epic.getId());
        taskManager.createSubTask(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/subTasks/" + subTask.getId()))
                .DELETE()
                .build();

        HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/epics/" + epic.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("SubTask deleted"));

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/subTasks"))
                .GET()
                .build();

        HttpResponse<String> response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());
        assertTrue(response2.body().contains("[]"));
    }
}
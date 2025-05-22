package main;

import classes.Managers;
import classes.Task;

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

class HttpTaskServerIntegrationTest {
    private HttpTaskServer server;
    private HttpClient httpClient;
    private final String baseUrl = "http://localhost:8080";

    @BeforeEach
    void setUp() throws IOException {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Test task", "Test task description");
        taskManager.createTask(task);
        server = new HttpTaskServer(taskManager);
        server.start();
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

//    @Test
//    void testTasksEndpoint() throws Exception {
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(baseUrl + "/tasks"))
//                .GET()
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(200, response.statusCode());
//        assertTrue(response.body().contains("Test task"));

        // Не понимаю почему в тесте выше код 500 вылезает вместо 200. Через дебаггер прогнал, таск менеджер видит задачу, она
        // там числится. а передать ее он не передает. Помоги, пожалуйста, уже всю голову сломал, что тут не так.
        // Думал, что как-то не так отображается, переопределил toString() для Task, но ничего не поменялось,
        // как было 500, так и осталось
    }
}
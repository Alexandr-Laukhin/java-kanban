package classes.handlers;

import classes.Epic;
import classes.Managers;
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

public class EpicHandlerTest {

    private HttpTaskServer server;
    private HttpClient httpClient;
    private final String baseUrl = "http://localhost:8080";
    private TaskManager taskManager = Managers.getDefault();
    Gson gson = new Gson();
    Epic epic;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer(taskManager);
        epic = new Epic("Test epic", "Test epic description");
        taskManager.createEpic(epic);
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
    void testEpicsEndpointPOST() throws Exception {

        Epic epic2 = new Epic("Test epic2", "Test epic description2");
        String jsonEpic = gson.toJson(epic2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test epic2"));
    }

    @Test
    void testEpicsEndpointGET() throws Exception {
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
        int epicId = epic.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/epics/" + epicId))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(taskManager.getEpics().isEmpty());
    }
}

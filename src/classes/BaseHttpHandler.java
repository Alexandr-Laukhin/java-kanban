package classes;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected final Gson gson = new Gson();
    protected final TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void sendJson(HttpExchange exchange, int statusCode, Object response) throws IOException {
        String json = gson.toJson(response);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    protected void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        ErrorResponse error = new ErrorResponse(statusCode, message);
        sendJson(exchange, statusCode, error);
    }

    protected <T> T parseJson(HttpExchange exchange, Class<T> classOfT) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, classOfT);
        }
    }

    private static class ErrorResponse {
        int code;
        String message;

        ErrorResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
package classes.handlers;

import classes.Task;
import classes.exceptions.*;
import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;

import java.io.IOException;

public class TasksHandler extends BaseHttpHandler {
    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET" -> {
                    if (path.equals("/tasks")) {
                        handleGetAllTasks(exchange);
                    } else if (path.matches("/tasks/\\d+")) {
                        handleGetTaskById(exchange);
                    }
                }
                case "POST" -> handleCreateOrUpdateTask(exchange);
                case "DELETE" -> handleDeleteTask(exchange);
                default -> sendError(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        sendJson(exchange, 200, taskManager.getTasks());
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange);
        try {
            Task task = taskManager.getTaskByID(id);
            sendJson(exchange, 200, task);
        } catch (NotFoundException e) {
            sendError(exchange, 404, "Task not found");
        }
    }

    private void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        Task task = parseJson(exchange, Task.class);
        try {
            if (task.getId() == 0) {
                taskManager.createTask(task);
                sendJson(exchange, 200, task);
            } else {
                taskManager.updateTask(task);
                sendJson(exchange, 201, task);
            }
        } catch (NotAcceptableException e) {
            sendError(exchange, 406, "Not Acceptable");
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        int id = extractId(exchange);
        taskManager.deleteTaskByID(id);
        sendJson(exchange, 200, "Task deleted");
    }

    private int extractId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        return Integer.parseInt(pathParts[2]);
    }
}
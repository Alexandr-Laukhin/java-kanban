package classes.handlers;

import classes.exceptions.*;
import classes.SubTask;
import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;

import java.io.IOException;

public class SubTaskHandler extends BaseHttpHandler {
    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET" -> {
                    if (path.equals("/subTasks")) {
                        handleGetAllSubTasks(exchange);
                    } else if (path.matches("/subTasks/\\d+")) {
                        handleGetSubTaskById(exchange);
                    }
                }
                case "POST" -> handleCreateOrUpdateSubTask(exchange);
                case "DELETE" -> handleDeleteSubTask(exchange);
                default -> sendError(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void handleGetAllSubTasks(HttpExchange exchange) throws IOException {
        sendJson(exchange, 200, taskManager.getSubTasks());
    }

    private void handleGetSubTaskById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange);
        try {
            SubTask subTask = taskManager.getSubTaskByID(id);
            sendJson(exchange, 200, subTask);
        } catch (NotFoundException e) {
            sendError(exchange, 404, "SubTask not found");
        }
    }

    private void handleCreateOrUpdateSubTask(HttpExchange exchange) throws IOException {
        SubTask subTask = parseJson(exchange, SubTask.class);
        try {
            if (subTask.getId() == 0) {
                taskManager.createSubTask(subTask);
                sendJson(exchange, 200, subTask);
            } else {
                taskManager.updateSubTask(subTask);
                sendJson(exchange, 201, subTask);
            }
        } catch (NotAcceptableException e) {
            sendError(exchange, 406, "Not Acceptable");
        }
    }

    private void handleDeleteSubTask(HttpExchange exchange) throws IOException {
        int id = extractId(exchange);
        taskManager.deleteSubTaskByID(id);
        sendJson(exchange, 200, "SubTask deleted");
    }

    private int extractId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        return Integer.parseInt(pathParts[2]);
    }
}
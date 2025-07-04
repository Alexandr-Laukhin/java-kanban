package classes.handlers;

import classes.Epic;
import classes.SubTask;
import classes.exceptions.*;
import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET" -> {
                    if (path.equals("/epics")) {
                        handleGetAllEpics(exchange);
                    } else if (path.matches("/epics/\\d+/epicSubTasks")) {
                        handleGetEpicSubTasks(exchange);
                    } else if (path.matches("/epics/\\d+")) {
                        handleGetEpicById(exchange);
                    }
                }
                case "POST" -> handleCreateOrUpdateEpic(exchange);
                case "DELETE" -> handleDeleteEpic(exchange);
                default -> sendError(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            sendError(exchange, 500, "Internal Server Error");
        }
    }

    private void handleGetAllEpics(HttpExchange exchange) throws IOException {
        sendJson(exchange, 200, taskManager.getEpics());
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        int id = extractId(exchange);
        try {
            Epic epic = taskManager.getEpicByID(id);
            sendJson(exchange, 200, epic);
        } catch (NotFoundException e) {
            sendError(exchange, 404, "Epic not found");
        }
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        Epic epic = parseJson(exchange, Epic.class);
        try {
            if (epic.getId() == 0) {
                taskManager.createEpic(epic);
                sendJson(exchange, 200, epic);
            } else {
                taskManager.updateEpic(epic);
                sendJson(exchange, 201, epic);
            }
        } catch (NotAcceptableException e) {
            sendError(exchange, 406, "Not Acceptable");
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        int id = extractId(exchange);
        taskManager.deleteEpicByID(id);
        sendJson(exchange, 200, "Epic deleted");
    }

    private int extractId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        return Integer.parseInt(pathParts[2]);
    }

    private void handleGetEpicSubTasks(HttpExchange exchange) throws IOException {
        int epicId = extractId(exchange);
        List<SubTask> epicSubTasks = taskManager.getSubTasksFromEpicByID(epicId);
        sendJson(exchange, 200, epicSubTasks);
    }
}
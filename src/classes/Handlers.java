package classes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.TaskManager;

import java.io.IOException;

public class Handlers {

    private TasksHandler tasksHandler;
    private EpicsHandler epicsHandler;
    private SubTaskHandler subtasksHandler;
    private HistoryHandler historyHandler;
    private PrioritizedHandler prioritizedHandler;

    public Handlers(TaskManager taskManager) {
        this.tasksHandler = new TasksHandler(taskManager);
        this.subtasksHandler = new SubTaskHandler(taskManager);
        this.epicsHandler = new EpicsHandler(taskManager);
        this.historyHandler = new HistoryHandler(taskManager);
        this.prioritizedHandler = new PrioritizedHandler(taskManager);
    }

    public EpicsHandler getEpicsHandler() {
        return epicsHandler;
    }

    public HistoryHandler getHistoryHandler() {
        return historyHandler;
    }

    public PrioritizedHandler getPrioritizedHandler() {
        return prioritizedHandler;
    }

    public SubTaskHandler getSubTasksHandler() {
        return subtasksHandler;
    }

    public TasksHandler getTasksHandler() {
        return tasksHandler;
    }

    public class TasksHandler extends BaseHttpHandler implements HttpHandler {
        public TasksHandler(TaskManager taskManager) {
            super(taskManager);
        }

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
            } catch (Exeptions.NotFoundException e) {
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
            } catch (Exeptions.NotAcceptableExeption e) {
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

    public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
        public EpicsHandler(TaskManager taskManager) {
            super(taskManager);
        }

        public void handle(HttpExchange exchange) throws IOException {
            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();

                switch (method) {
                    case "GET" -> {
                        if (path.equals("/epics")) {
                            handleGetAllEpics(exchange);
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
            } catch (Exeptions.NotFoundException e) {
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
            } catch (Exeptions.NotAcceptableExeption e) {
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
    }

    public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
        public SubTaskHandler(TaskManager taskManager) {
            super(taskManager);
        }

        public void handle(HttpExchange exchange) throws IOException {
            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();

                switch (method) {
                    case "GET" -> {
                        if (path.equals("/epics")) {
                            handleGetAllSubTasks(exchange);
                        } else if (path.matches("/epics/\\d+")) {
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
            } catch (Exeptions.NotFoundException e) {
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
            } catch (Exeptions.NotAcceptableExeption e) {
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

    public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
        public HistoryHandler(TaskManager taskManager) {
            super(taskManager);
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equals("GET")) {
                sendError(exchange, 405, "Method Not Allowed");
                return;
            }
            sendJson(exchange, 200, taskManager.getHistory());
        }
    }

    public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
        public PrioritizedHandler(TaskManager taskManager) {
            super(taskManager);
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equals("GET")) {
                sendError(exchange, 405, "Method Not Allowed");
                return;
            }
            sendJson(exchange, 200, taskManager.getPrioritizedTasks());
        }
    }

}
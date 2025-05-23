package main;

import classes.*;

import classes.handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer server;
    private TasksHandler tasksHandler;
    private EpicsHandler epicsHandler;
    private SubTaskHandler subTaskHandler;
    private HistoryHandler historyHandler;
    private PrioritizedHandler prioritizedHandler;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.tasksHandler = new TasksHandler(taskManager);
        this.epicsHandler = new EpicsHandler(taskManager);
        this.subTaskHandler = new SubTaskHandler(taskManager);
        this.historyHandler = new HistoryHandler(taskManager);
        this.prioritizedHandler = new PrioritizedHandler(taskManager);

        createHandlers();
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }

    private void createHandlers() {

        server.createContext("/tasks", tasksHandler);
        server.createContext("/epics", epicsHandler);
        server.createContext("/subTasks", subTaskHandler);
        server.createContext("/history", historyHandler);
        server.createContext("/prioritized", prioritizedHandler);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
package main;

import classes.*;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer server;
    private TaskManager taskManager;
    private Handlers handlers;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.handlers = new Handlers(taskManager);
        createHandlers();
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }

    private void createHandlers() {
        server.createContext("/tasks", handlers.getTasksHandler());
        server.createContext("/subtasks", handlers.getSubTasksHandler());
        server.createContext("/epics", handlers.getEpicsHandler());
        server.createContext("/history", handlers.getHistoryHandler());
        server.createContext("/prioritized", handlers.getPrioritizedHandler());
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}



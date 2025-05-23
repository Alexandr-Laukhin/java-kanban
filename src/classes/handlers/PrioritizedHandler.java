package classes.handlers;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler  {
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

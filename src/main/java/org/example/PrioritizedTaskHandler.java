package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class PrioritizedTaskHandler extends BaseHttpHandler {

    private TaskManager taskManager;
    private Gson gson;

    public PrioritizedTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            getPrioritizedTasks(exchange);
        } else {
            sendNotFound(exchange, "Only GET method is supported for prioritized tasks");
        }
    }

    private void getPrioritizedTasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
    }
}
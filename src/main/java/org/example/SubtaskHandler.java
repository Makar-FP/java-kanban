package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler {

    private TaskManager taskManager;
    private Gson gson = new Gson();

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        try {
            if (method.equals("GET") && pathParts.length == 2) {
                getAllSubtasks(exchange);
            } else if (method.equals("GET") && pathParts.length == 3) {
                getSubtaskById(exchange, Integer.parseInt(pathParts[2]));
            } else if (method.equals("POST") && pathParts.length == 2) {
                createOrUpdateSubtask(exchange);
            } else if (method.equals("DELETE") && pathParts.length == 3) {
                deleteSubtaskById(exchange, Integer.parseInt(pathParts[2]));
            } else {
                sendNotFound(exchange, "Endpoint not found");
            }
        } catch (Exception e) {
            sendServerError(exchange, "Internal Server Error: " + e.getMessage());
        }
    }

    private void getAllSubtasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
    }

    private void getSubtaskById(HttpExchange exchange, int id) throws IOException {
        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask != null) {
            sendText(exchange, gson.toJson(subtask), 200);
        } else {
            sendNotFound(exchange, "Subtask not found");
        }
    }

    private void createOrUpdateSubtask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(body, Subtask.class);

        if (subtask.getId() == 0) {
            taskManager.createSubtask(subtask);
            sendText(exchange, "Subtask created", 201);
        } else {
            taskManager.updateSubtask(subtask);
            sendText(exchange, "Subtask updated", 201);
        }
    }

    private void deleteSubtaskById(HttpExchange exchange, int id) throws IOException {
        taskManager.deleteSubtaskById(id);
        sendText(exchange, "Subtask deleted", 200);
    }
}
package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {

    private TaskManager taskManager;
    private Gson gson;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        try {
            if (method.equals("GET") && pathParts.length == 2) {
                getAllTasks(exchange);
            } else if (method.equals("GET") && pathParts.length == 3) {
                getTaskById(exchange, Integer.parseInt(pathParts[2]));
            } else if (method.equals("POST") && pathParts.length == 2) {
                createOrUpdateTask(exchange);
            } else if (method.equals("DELETE") && pathParts.length == 3) {
                deleteTaskById(exchange, Integer.parseInt(pathParts[2]));
            } else {
                sendNotFound(exchange, "Endpoint not found");
            }
        } catch (Exception e) {
            sendServerError(exchange, "Internal Server Error: " + e.getMessage());
        }
    }

    private void getAllTasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllTasks()), 200);
    }

    private void getTaskById(HttpExchange exchange, int id) throws IOException {
        Task task = taskManager.getTaskById(id);
        if (task != null) {
            sendText(exchange, gson.toJson(task), 200);
        } else {
            sendNotFound(exchange, "Task not found");
        }
    }

    private void createOrUpdateTask(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(body, Task.class);

        if (task.getId() == 0) {
            taskManager.createTask(task);
            sendText(exchange, "Task created", 201);
        } else {
            taskManager.updateTask(task);
            sendText(exchange, "Task updated", 201);
        }
    }

    private void deleteTaskById(HttpExchange exchange, int id) throws IOException {
        taskManager.deleteTaskById(id);
        sendText(exchange, "Task deleted", 200);
    }
}
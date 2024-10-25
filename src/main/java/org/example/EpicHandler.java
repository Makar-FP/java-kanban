package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler {

    private TaskManager taskManager;
    private Gson gson;

    public EpicHandler(TaskManager taskManager) {
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
                getAllEpics(exchange);
            } else if (method.equals("GET") && pathParts.length == 3) {
                getEpicById(exchange, Integer.parseInt(pathParts[2]));
            } else if (method.equals("GET") && pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                getEpicSubtasks(exchange, Integer.parseInt(pathParts[2]));
            } else if (method.equals("POST") && pathParts.length == 2) {
                createEpic(exchange);
            } else if (method.equals("DELETE") && pathParts.length == 3) {
                deleteEpicById(exchange, Integer.parseInt(pathParts[2]));
            } else {
                sendNotFound(exchange, "Endpoint not found");
            }
        } catch (Exception e) {
            sendServerError(exchange, "Internal Server Error: " + e.getMessage());
        }
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
    }

    private void getEpicById(HttpExchange exchange, int id) throws IOException {
        Epic epic = taskManager.getEpicById(id);
        if (epic != null) {
            sendText(exchange, gson.toJson(epic), 200);
        } else {
            sendNotFound(exchange, "Epic not found");
        }
    }

    private void getEpicSubtasks(HttpExchange exchange, int id) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getSubtasksByEpicId(id)), 200);
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(body, Epic.class);

        taskManager.createEpic(epic);
        sendText(exchange, "Epic created", 201);
    }

    private void deleteEpicById(HttpExchange exchange, int id) throws IOException {
        taskManager.deleteEpicById(id);
        sendText(exchange, "Epic deleted", 200);
    }
}
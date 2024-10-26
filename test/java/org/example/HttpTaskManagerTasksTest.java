package org.example;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;
    private HttpClient client;

    public HttpTaskManagerTasksTest() throws IOException {
        HistoryManager historyManager = new InMemoryHistoryManager();
        manager = new InMemoryTaskManager(historyManager);
        taskServer = new HttpTaskServer(manager); // передаем manager в сервер
        client = HttpClient.newHttpClient();
    }

    @BeforeEach
    public void setUp() {
        gson = HttpTaskServer.getGson();
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        taskServer.start();
    }

    @AfterEach
    public void tearDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // Создаём задачу
        Task task = new Task("Test Task", "Testing task creation");
        task.setStatus(Status.NEW);
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(LocalDateTime.now());

        String taskJson = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Задача не создана");

        // Небольшая задержка для обработки запроса сервером
        Thread.sleep(50);

        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Список задач пуст");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Task for GET request");
        task.setStatus(Status.NEW);
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(LocalDateTime.now());
        manager.createTask(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Не удалось получить задачи");
        List<Task> tasksFromResponse = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertNotNull(tasksFromResponse, "Список задач пуст");
    }
}
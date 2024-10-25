package org.example;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerEpicsTest {

    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;
    private HttpClient client;

    public HttpTaskManagerEpicsTest() throws IOException {
        HistoryManager historyManager = new InMemoryHistoryManager();
        manager = new InMemoryTaskManager(historyManager);
        taskServer = new HttpTaskServer(manager);
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
    public void testAddEpic() throws IOException, InterruptedException {
        // Создаем новый эпик
        Epic epic = new Epic("Epic Test", "Epic description");
        String epicJson = gson.toJson(epic);

        // Отправляем запрос на создание эпика
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем успешное создание эпика
        assertEquals(201, response.statusCode(), "Эпик не создан");
        List<Epic> epicsFromManager = manager.getAllEpics();
        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Неверное количество эпиков в менеджере");

        // Проверяем правильность названия и описания эпика
        Epic createdEpic = epicsFromManager.get(0);
        assertEquals("Epic Test", createdEpic.getTitle(), "Неверное имя эпика");
        assertEquals("Epic description", createdEpic.getDescription(), "Неверное описание эпика");
    }

    @Test
    public void testDeleteEpicById() throws IOException, InterruptedException {
        // Создаем и добавляем эпик для удаления
        Epic epic = new Epic("Epic Test", "To be deleted");
        manager.createEpic(epic);

        // Отправляем запрос на удаление эпика по ID
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем успешное удаление эпика
        assertEquals(200, response.statusCode(), "Не удалось удалить эпик");
        assertTrue(manager.getAllEpics().isEmpty(), "Эпик не удален из менеджера");
    }
}
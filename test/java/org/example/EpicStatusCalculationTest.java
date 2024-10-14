package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicStatusCalculationTest {
    private TaskManager taskManager;
    private Epic epic;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        epic = new Epic("Тестовый эпик", "Эпик для тестирования");
        taskManager.createEpic(epic);
    }

    @Test
    void долженУстановитьСтатусЭпикаВNEWКогдаВсеПодзадачиNEW() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epic.getId(), taskManager);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epic.getId(), taskManager);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        taskManager.updateEpicStatus(epic.getId());

        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW, когда все подзадачи NEW");
    }

    @Test
    void долженУстановитьСтатусЭпикаВДONEКогдаВсеПодзадачиDONE() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epic.getId(), taskManager);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epic.getId(), taskManager);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateEpicStatus(epic.getId());

        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика должен быть DONE, когда все подзадачи DONE");
    }

    @Test
    void долженУстановитьСтатусЭпикаВInProgressКогдаПодзадачиNEWИDONE() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epic.getId(), taskManager);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epic.getId(), taskManager);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        taskManager.updateEpicStatus(epic.getId());

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS, когда подзадачи NEW и DONE");
    }

    @Test
    void долженУстановитьСтатусЭпикаВInProgressКогдаВсеПодзадачиInProgress() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", epic.getId(), taskManager);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", epic.getId(), taskManager);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpicStatus(epic.getId());

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть IN_PROGRESS, когда все подзадачи IN_PROGRESS");
    }

    @Test
    void долженУстановитьСтатусЭпикаВNEWКогдаНетПодзадач() {
        taskManager.updateEpicStatus(epic.getId());
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика должен быть NEW, когда нет подзадач");
    }
}
package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    public void testAddAndFindTasks() {

        Task task = new Task("Task", "Description");
        manager.createTask(task);
        assertEquals(task, manager.getTaskById(task.getId()), "Задача должна находиться по id");

        Epic epic = new Epic("Epic", "Epic Description");
        manager.createEpic(epic);
        assertEquals(epic, manager.getEpicById(epic.getId()), "Эпик должен находиться по id");

        Subtask subtask = new Subtask("Subtask", "Subtask Description", epic, manager);
        manager.createSubtask(subtask);
        assertEquals(subtask, manager.getSubtaskById(subtask.getId()), "Подзадача должна находиться по id");
    }

    @Test
    void shouldReturnTaskHistory() {
        Task task1 = manager.createTask(new Task("Task 1", "Description"));
        Task task2 = manager.createTask(new Task("Task 2", "Description"));

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }
}

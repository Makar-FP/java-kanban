package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void testAddTaskToHistory() {
        Task task = new Task("Task", "Description");
        taskManager.createTask(task);

        taskManager.getTaskById(task.getId());  // Добавляем задачу в историю
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "История должна содержать одну задачу");
        assertEquals(task, history.get(0), "Задача в истории должна совпадать с добавленной");
    }

    @Test
    public void testNoDuplicateInHistory() {
        Task task = new Task("Task", "Description");
        taskManager.createTask(task);

        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История не должна содержать дубликатов");
    }

    @Test
    public void testRemoveTaskFromHistory() {
        Task task1 = taskManager.createTask(new Task("Task 1", "Description 1"));
        Task task2 = taskManager.createTask(new Task("Task 2", "Description 2"));

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        taskManager.deleteTaskById(task1.getId());  // Удаляем первую задачу

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу после удаления");
        assertEquals(task2, history.get(0), "Оставшаяся задача должна быть второй");
    }
}
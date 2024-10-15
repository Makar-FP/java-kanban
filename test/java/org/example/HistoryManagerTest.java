package org.example;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        task3 = new Task("Task 3", "Description 3");
        task3.setId(3);
    }

    @Test
    void shouldReturnEmptyHistoryWhenNoTasksAdded() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой, если задачи не добавлялись.");
    }

    @Test
    void shouldAddTasksToHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи.");
        assertEquals(task1, history.get(0), "Первая задача в истории должна быть task1.");
        assertEquals(task2, history.get(1), "Вторая задача в истории должна быть task2.");
    }

    @Test
    void shouldNotAllowDuplicateTasksInHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История не должна содержать дубликатов задач.");
        assertEquals(task2, history.get(0), "Последняя добавленная задача должна быть первой в истории.");
        assertEquals(task1, history.get(1), "Повторно добавленная задача должна быть перемещена в конец.");
    }

    @Test
    void shouldRemoveTaskFromBeginningOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "После удаления первой задачи в истории должно остаться две задачи.");
        assertEquals(task2, history.get(0), "После удаления первой задачи, второй должна стать первой.");
    }

    @Test
    void shouldRemoveTaskFromMiddleOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "После удаления задачи из середины, история должна содержать две задачи.");
        assertEquals(task1, history.get(0), "Первая задача не должна измениться.");
        assertEquals(task3, history.get(1), "Третья задача должна остаться в конце.");
    }

    @Test
    void shouldRemoveTaskFromEndOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "После удаления последней задачи в истории должно остаться две задачи.");
        assertEquals(task1, history.get(0), "Первая задача не должна измениться.");
        assertEquals(task2, history.get(1), "Вторая задача должна оставаться на своем месте.");
    }
}
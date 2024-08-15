package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskIdConflictTest {

    @Test
    public void testIdConflict() {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task 1", "Description 1");
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2");
        task2.setId(task1.getId()); // Устанавливаем id вручную

        manager.createTask(task2);

        assertNotEquals(task1.getId(), task2.getId(), "Задачи с одинаковым id не должны конфликтовать");
    }
}
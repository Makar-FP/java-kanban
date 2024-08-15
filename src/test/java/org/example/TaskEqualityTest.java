package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskEqualityTest {

    @Test
    public void testTasksWithSameIdAreEqual() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1.getId(), task2.getId(), "Задачи с одинаковыми id должны быть равны");
    }
}
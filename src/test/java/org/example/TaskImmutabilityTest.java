package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskImmutabilityTest {

    @Test
    public void testTaskImmutabilityAfterAddingToManager() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("Task", "Description");
        task.setStatus(Status.DONE);

        Task addedTask = manager.createTask(task);

        assertEquals(task.getStatus(), addedTask.getStatus(), "Статус задачи не должен изменяться после добавления");
        assertEquals(task.getTitle(), addedTask.getTitle(), "Название задачи не должно изменяться после добавления");
        assertEquals(task.getDescription(), addedTask.getDescription(), "Описание задачи не должно изменяться после добавления");
    }
}
package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InheritanceTaskEqualityTest {

    @Test
    public void testSubtasksWithSameIdAreEqual() {
        Epic epic = new Epic("Epic", "Epic Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic, Managers.getDefault());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic, Managers.getDefault());

        subtask1.setId(1);
        subtask2.setId(1);

        assertEquals(subtask1.getId(), subtask2.getId(), "Подзадачи с одинаковыми id должны быть равны");
    }
}
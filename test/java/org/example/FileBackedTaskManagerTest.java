package org.example;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    public void shouldThrowExceptionWhenSavingToInvalidFile() {
        File invalidFile = new File("/invalid/path/to/file.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(invalidFile);

        Task task = new Task("Test Task", "Description");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(30));

        assertThrows(ManagerSaveException.class, () -> {
            manager.createTask(task);
        }, "Ожидается исключение ManagerSaveException при сохранении в недоступный файл");
    }

    @Test
    public void shouldNotThrowExceptionWhenSavingToValidFile() {
        File validFile = new File("valid_file.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(validFile);

        Task task = new Task("Test Task", "Description");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(30));

        assertDoesNotThrow(() -> {
            manager.createTask(task);
        }, "Не должно быть исключения при сохранении задачи в корректный файл");
    }

    @Test
    public void shouldThrowExceptionWhenLoadingFromNonExistingFile() {
        File nonExistingFile = new File("non_existing_file.txt");

        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(nonExistingFile);
        }, "Ожидается исключение ManagerSaveException при загрузке из несуществующего файла");
    }

    @Test
    public void shouldNotThrowExceptionWhenLoadingFromValidFile() {
        File validFile = new File("valid_file_to_load.txt");
        FileBackedTaskManager managerToSave = new FileBackedTaskManager(validFile);

        Task task = new Task("Task to save", "Task description");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(60));
        managerToSave.createTask(task);

        assertDoesNotThrow(() -> {
            FileBackedTaskManager.loadFromFile(validFile);
        }, "Не должно быть исключения при загрузке из корректного файла");
    }

    @Test
    public void shouldThrowExceptionWhenCorruptDataInFile() throws IOException {
        File corruptFile = new File("corrupt_file.txt");
        try (FileWriter writer = new FileWriter(corruptFile)) {
            writer.write("Некорректные данные");
        }

        assertThrows(IllegalArgumentException.class, () -> {
            FileBackedTaskManager.loadFromFile(corruptFile);
        }, "Ожидается исключение IllegalArgumentException при загрузке некорректных данных из файла");
    }
}
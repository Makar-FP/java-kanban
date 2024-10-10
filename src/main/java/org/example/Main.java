package org.example;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task task1 = new Task("Task1", "Description Task1");
        Epic epic1 = new Epic("Epic1", "Description Epic1");
        manager.createTask(task1);
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask1", "Description Subtask1", epic1.getId(), manager);
        manager.createSubtask(subtask1);

        System.out.println("Сохраненные задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        System.out.println("Загруженные задачи:");
        System.out.println(loadedManager.getAllTasks());
        System.out.println(loadedManager.getAllEpics());
        System.out.println(loadedManager.getAllSubtasks());
    }
}
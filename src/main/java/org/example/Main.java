package org.example;

public class Main {
    public static void main(String[] args) {
        test();
    }

    static void test() {
        TaskManager taskManager = new TaskManager();

        // Создание двух задач
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Создание эпика с двумя подзадачами
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1, taskManager);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1, taskManager);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // Создание эпика с одной подзадачей
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", epic2, taskManager);
        taskManager.createSubtask(subtask3);

        // Распечатка списков эпиков, задач и подзадач
        System.out.println("Задачи: " + taskManager.getAllTasks());
        System.out.println("Эпики: " + taskManager.getAllEpics());
        System.out.println("Подзадачи: " + taskManager.getAllSubtasks());

        // Изменение статусов задач и подзадач
        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.DONE);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);

        // Обновление задач и подзадач в менеджере
        taskManager.updateTask(task1);
        taskManager.updateTask(task2);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        // Распечатка статусов задач и эпиков
        System.out.println("Обновленные задачи: " + taskManager.getAllTasks());
        System.out.println("Обновленные эпики: " + taskManager.getAllEpics());
        System.out.println("Обновленные подзадачи: " + taskManager.getAllSubtasks());

        // Удаление задачи и эпика
        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteEpicById(epic1.getId());

        // Распечатка списков после удаления
        System.out.println("Задачи после удаления: " + taskManager.getAllTasks());
        System.out.println("Эпики после удаления: " + taskManager.getAllEpics());
        System.out.println("Подзадачи после удаления: " + taskManager.getAllSubtasks());
    }
}

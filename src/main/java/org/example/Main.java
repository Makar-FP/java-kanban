package org.example;

public class Main {
    public static void main(String[] args) {
        test();
    }

    static void test() {
        TaskManager taskManager = TaskManager.getInstance();

        Task task1 = new Task("Задача 1", "Описание задачи 1", 0);
        Task task2 = new Task("Задача 2", "Описание задачи 2", 0);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", 0);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", 0);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1.1", "Описание подзадачи 1.1", 0, epic1);
        Subtask subtask2 = new Subtask("Подзадача 1.2", "Описание подзадачи 1.2", 0, epic1);
        Subtask subtask3 = new Subtask("Подзадача 2.1", "Описание подзадачи 2.1", 0, epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        System.out.println("Эпики:");
        for (Epic epic : taskManager.getAllEpics().values()) {
            System.out.println(epic);
        }

        System.out.println("Задачи:");
        for (Task task : taskManager.getAllTasks().values()) {
            System.out.println(task);
        }

        System.out.println("Подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks().values()) {
            System.out.println(subtask);
        }

        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        System.out.println("\nПосле изменения статусов:");
        System.out.println("Эпики:");
        for (Epic epic : taskManager.getAllEpics().values()) {
            System.out.println(epic);
        }

        System.out.println("Задачи:");
        for (Task task : taskManager.getAllTasks().values()) {
            System.out.println(task);
        }

        System.out.println("Подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks().values()) {
            System.out.println(subtask);
        }

        taskManager.deleteTaskById(task2.getId());
        taskManager.deleteEpicById(epic1.getId());

        System.out.println("\nПосле удаления задачи и эпика:");
        System.out.println("Эпики:");
        for (Epic epic : taskManager.getAllEpics().values()) {
            System.out.println(epic);
        }

        System.out.println("Задачи:");
        for (Task task : taskManager.getAllTasks().values()) {
            System.out.println(task);
        }

        System.out.println("Подзадачи:");
    }
}

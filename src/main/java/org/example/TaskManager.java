package org.example;

import java.util.*;

public class TaskManager {
    private static TaskManager instance;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private int idCounter = 0;

    private TaskManager() {}

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public Task createTask(Task task) {
        task.setId(++idCounter);
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(++idCounter);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(++idCounter);
        subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public Map<Integer, Task> getAllTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getAllEpics() {
        return epics;
    }

    public Map<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
        subtasks.values().removeIf(subtask -> subtask.getEpicId() == id);
    }
}
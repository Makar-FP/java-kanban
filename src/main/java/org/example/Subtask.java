package org.example;

public class Subtask extends Task {
    private int epicId;
    private TaskManager taskManager;

    public Subtask(String title, String description, int epicId, TaskManager taskManager) {
        super(title, description);
        this.epicId = epicId;
        this.taskManager = taskManager;
        Epic epic = taskManager.getEpicById(epicId);
        if (epic == null) {
            throw new IllegalStateException("Эпик с ID " + epicId + " не найден.");
        }
        epic.addSubtask(this);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        taskManager.updateEpicStatus(epicId);
    }
}
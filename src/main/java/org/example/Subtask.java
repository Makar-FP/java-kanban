package org.example;

public class Subtask extends Task {
    private int epicId;
    private TaskManager taskManager;

    public Subtask(String title, String description, Epic epic, TaskManager taskManager) {
        super(title, description);
        this.epicId = epic.getId();
        this.taskManager = taskManager;
        epic.addSubtask(this);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        Epic epic = taskManager.getEpicById(epicId);
        if (epic != null) {
            taskManager.updateEpicStatus(epicId);
        } else {
            throw new IllegalStateException("Эпик не нашелся");
        }
    }
}
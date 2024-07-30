package org.example;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, int id, Epic epic) {
        super(title, description, id);
        this.epicId = epic.getId();
        epic.addSubtask(this);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        TaskManager.getInstance().getAllEpics().get(epicId).updateStatusBasedOnSubtasks();
    }
}
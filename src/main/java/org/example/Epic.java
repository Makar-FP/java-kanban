package org.example;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String title, String description, int id) {
        super(title, description, id);
        this.subtasks = new ArrayList<>();
        updateStatusBasedOnSubtasks();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateStatusBasedOnSubtasks();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void updateStatusBasedOnSubtasks() {
        boolean hasNew = false;
        boolean hasInProgress = false;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == Status.NEW) {
                hasNew = true;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                hasInProgress = true;
            }
        }

        if (hasInProgress) {
            setStatus(Status.IN_PROGRESS);
        } else if (hasNew) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.DONE);
        }
    }

    public List<Integer> getSubtaskIds() {
        List<Integer> subtaskIds = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            subtaskIds.add(subtask.getId());
        }
        return subtaskIds;
    }

    public void addSubtaskId(int id) {
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == id) {
                return;
            }
        }
        subtasks.add(new Subtask("Subtask", "Description", id, this));
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtasks=" + subtasks +
                '}';
    }
}

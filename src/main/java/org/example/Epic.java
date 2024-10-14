package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        this.duration = Duration.ofMinutes(0);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        recalculateEpicFields();
    }

    private void recalculateEpicFields() {
        if (subtasks.isEmpty()) {
            this.duration = Duration.ofMinutes(0);
            this.startTime = null;
            this.endTime = null;
            return;
        }

        this.duration = subtasks.stream()
                .map(Subtask::getDuration)
                .reduce(Duration::plus)
                .orElse(Duration.ofMinutes(0));

        this.startTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        this.endTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", duration=" + duration.toMinutes() + " minutes" +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", subtasks=" + subtasks +
                '}';
    }
}

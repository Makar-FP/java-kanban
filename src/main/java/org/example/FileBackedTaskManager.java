package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(createdTask);
        }
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        recalculatePrioritizedTasks();
        save();
        return createdEpic;
    }

    private void recalculatePrioritizedTasks() {
        prioritizedTasks.clear();
        getAllTasks().stream().filter(t -> t.getStartTime() != null).forEach(prioritizedTasks::add);
        getAllSubtasks().stream().filter(s -> s.getStartTime() != null).forEach(prioritizedTasks::add);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public FileBackedTaskManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toString(task));
                writer.write("\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic));
                writer.write("\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных", e);
        }
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());

            for (String line : lines) {
                if (line.startsWith("id")) {
                    continue;
                }
                Task task = manager.fromString(line);
                if (task instanceof Epic) {
                    manager.createEpic((Epic) task);
                } else if (!(task instanceof Subtask)) {
                    manager.createTask(task);
                }
            }

            for (String line : lines) {
                if (line.startsWith("id")) {
                    continue;
                }
                Task task = manager.fromString(line);
                if (task instanceof Subtask) {
                    manager.createSubtask((Subtask) task);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных", e);
        }
        return manager;
    }

    private String toString(Task task) {
        TaskType type = task.getType();
        String epicId = (task instanceof Subtask) ? String.valueOf(((Subtask) task).getEpicId()) : "";
        return String.join(",",
                String.valueOf(task.getId()),
                type.name(),
                task.getTitle(),
                task.getStatus().name(),
                task.getDescription(),
                String.valueOf(task.getDuration().toMinutes()),
                task.getStartTime() != null ? task.getStartTime().toString() : "",
                epicId
        );
    }

    private Task fromString(String value) {
        String[] fields = value.split(",");
        if (fields.length < 6) {
            throw new IllegalArgumentException("Некорректный формат строки: " + value);
        }

        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String title = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(fields[5]));
        LocalDateTime startTime = (fields.length > 6 && !fields[6].isEmpty()) ? LocalDateTime.parse(fields[6]) : null;
        Task task;

        switch (type) {
            case TASK:
                task = new Task(title, description);
                task.setId(id);
                task.setStatus(status);
                task.setDuration(duration);
                task.setStartTime(startTime);
                break;
            case EPIC:
                task = new Epic(title, description);
                task.setId(id);
                task.setStatus(status);
                break;
            case SUBTASK:
                if (fields.length <= 7) {
                    throw new IllegalArgumentException("Некорректный формат строки для подзадачи: " + value);
                }
                int epicId = Integer.parseInt(fields[7]);
                Epic epic = getEpicById(epicId);
                Subtask subtask = new Subtask(title, description, epic.getId(), this);
                subtask.setId(id);
                subtask.setStatus(status);
                subtask.setDuration(duration);
                subtask.setStartTime(startTime);
                task = subtask;
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
        return task;
    }
}
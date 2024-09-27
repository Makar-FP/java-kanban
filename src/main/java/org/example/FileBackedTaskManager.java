package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

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
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
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

    private String toString(Task task) {
        TaskType type = getType(task);
        String epicId = (task instanceof Subtask) ? String.valueOf(((Subtask) task).getEpicId()) : "";
        return String.join(",",
                String.valueOf(task.getId()),
                type.name(),
                task.getTitle(),
                task.getStatus().name(),
                task.getDescription(),
                epicId
        );
    }

    private TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        } else {
            return TaskType.TASK;
        }
    }

    private Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String title = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        Task task;

        switch (type) {
            case TASK:
                task = new Task(title, description);
                task.setId(id);
                task.setStatus(status);
                break;
            case EPIC:
                task = new Epic(title, description);
                task.setId(id);
                task.setStatus(status);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]);
                Epic epic = getEpicById(epicId);
                Subtask subtask = new Subtask(title, description, epic.getId(), this);
                subtask.setId(id);
                subtask.setStatus(status);
                task = subtask;
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
        return task;
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
}
package org.example;

public class Managers {
    private static TaskManager defaultTaskManager;
    private static HistoryManager defaultHistoryManager;

    public static TaskManager getDefault() {
        if (defaultTaskManager == null) {
            defaultTaskManager = new InMemoryTaskManager(getDefaultHistory());
        }
        return defaultTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (defaultHistoryManager == null) {
            defaultHistoryManager = new InMemoryHistoryManager();
        }
        return defaultHistoryManager;
    }
}

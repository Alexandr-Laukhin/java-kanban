package main;

import classes.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    @Test
    void add() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Task testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);
        testTaskManager.getTaskByID(1);

        assertNotNull(testTaskManager.historyManager.getHistory());
    }

}
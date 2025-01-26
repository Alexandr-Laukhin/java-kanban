package main;

import classes.Managers;
import classes.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    private HistoryManager historyManager;
    private Task testTask;
    private TaskManager testTaskManager;

    @BeforeEach
    void createTestManagers() {
        historyManager = Managers.getDefaultHistory();
        testTaskManager = Managers.getDefault();
        testTask = new Task("Test task", "Test Task Description");
    }

    @Test
    void add() {
        historyManager.add(testTask);

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void remove() {
        historyManager.add(testTask);
        historyManager.remove(0);

        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void inHistoryShouldNotBeCopies() {
        historyManager.add(testTask);
        historyManager.add(testTask);
        historyManager.add(testTask);

        assertEquals(1, historyManager.getHistory().size());
    }
}
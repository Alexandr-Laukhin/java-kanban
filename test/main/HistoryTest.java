package main;

import classes.Managers;
import classes.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    private HistoryManager historyManager;
    private Task testTask;
    private Task testTask2;
    private Task testTask3;

    @BeforeEach
    void createTestManagers() {
        historyManager = Managers.getDefaultHistory();
        testTask = new Task("Test task", "Test Task Description", Duration.ofMinutes(10));
        testTask2 = new Task("Test task2", "Test Task Description2", Duration.ofMinutes(10));
        testTask2.setId(1);
        testTask3 = new Task("Test task3", "Test Task Description3", Duration.ofMinutes(10));
        testTask3.setId(2);
        historyManager.add(testTask);
    }

    @Test
    void add() {
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void remove() {
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

    @Test
    void deleteTheOnlyOneTaskFromHistory() {
        historyManager.add(testTask);

        historyManager.remove(0);

        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void deleteTaskFromTheEndOfHistory() {
        historyManager.add(testTask);
        historyManager.add(testTask2);
        historyManager.add(testTask3);

        historyManager.remove(2);

        assertEquals(List.of(testTask, testTask2), historyManager.getHistory());
    }

    @Test
    void deleteTaskAtTheBeginning() {
        historyManager.add(testTask);
        historyManager.add(testTask2);
        historyManager.add(testTask3);

        historyManager.remove(0);

        assertEquals(List.of(testTask2, testTask3), historyManager.getHistory());
    }

    @Test
    void deleteTaskFromCenter() {

        historyManager.add(testTask);
        historyManager.add(testTask2);
        historyManager.add(testTask3);

        historyManager.remove(1);

        assertEquals(List.of(testTask, testTask3), historyManager.getHistory());
    }
}
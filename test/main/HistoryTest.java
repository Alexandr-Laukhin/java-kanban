package main;

import classes.Managers;
import classes.Task;
import org.junit.jupiter.api.Assertions;
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
        testTaskManager.getTaskByID(0);
        testTaskManager.getTaskByID(0);
        testTaskManager.getTaskByID(0);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void deleteTheOnlyOneTaskFromHistory() {
        testTaskManager.getTaskByID(0);
        historyManager.remove(0);

        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void deleteTaskFromTheEndOfHistory() {
        Task testTask2 = new Task("Test task2", "Test Task Description2");
        Task testTask3 = new Task("Test task3", "Test Task Description3");
        testTaskManager.createTask(testTask);
        testTaskManager.createTask(testTask2);
        testTaskManager.createTask(testTask3);

        testTaskManager.getTaskByID(1);
        testTaskManager.getTaskByID(2);
        testTaskManager.getTaskByID(3);

        testTaskManager.deleteTaskByID(3);

        Assertions.assertEquals(1,testTaskManager.getHistory().getFirst().getId());
        Assertions.assertEquals(2,testTaskManager.getHistory().getLast().getId());

    }

    @Test
    void deleteTaskAtTheBeginning() {
        Task testTask2 = new Task("Test task2", "Test Task Description2");
        Task testTask3 = new Task("Test task3", "Test Task Description3");
        testTaskManager.createTask(testTask);
        testTaskManager.createTask(testTask2);
        testTaskManager.createTask(testTask3);

        testTaskManager.getTaskByID(1);
        testTaskManager.getTaskByID(2);
        testTaskManager.getTaskByID(3);

        testTaskManager.deleteTaskByID(1);

        Assertions.assertEquals(2,testTaskManager.getHistory().getFirst().getId());
        Assertions.assertEquals(3,testTaskManager.getHistory().getLast().getId());
    }

    @Test
    void deleteTaskFromCenter() {
        Task testTask2 = new Task("Test task2", "Test Task Description2");
        Task testTask3 = new Task("Test task3", "Test Task Description3");
        testTaskManager.createTask(testTask);
        testTaskManager.createTask(testTask2);
        testTaskManager.createTask(testTask3);

        testTaskManager.getTaskByID(1);
        testTaskManager.getTaskByID(2);
        testTaskManager.getTaskByID(3);

        testTaskManager.deleteTaskByID(2);

        Assertions.assertEquals(2,testTaskManager.getHistory().size());
        Assertions.assertEquals(1,testTaskManager.getHistory().getFirst().getId());
        Assertions.assertEquals(3,testTaskManager.getHistory().getLast().getId());

    }
}
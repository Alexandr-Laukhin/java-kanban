package main;

import classes.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    private InMemoryTaskManager testTaskManager;

    @BeforeEach
    void createTestTaskManager() {
        testTaskManager = new InMemoryTaskManager();
    }

    @Test
    void add() {
        Task testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);
        testTaskManager.getTaskByID(1);

        assertNotNull(testTaskManager.historyManager.getHistory());
        // assertNotNull(Managers.getDefaultHistory().getHistory());

    }  // Не понял комментарий, поясни, пожалуйста. Ты говоришь обратиться к getHistory() в таск менеджере,
    // а не напрямую к historyManager. Но в таск менеджере нет такого метода. Ты имел ввиду обратиться через Managers, как я выше написал?

    @Test
    void historyArrayListShouldNotBeMoreThanTen() {
        Task testTask1 = new Task("Test task1", "Test Task Description1");

        for (int i = 1; i < 12; i++) {
            testTaskManager.createTask(testTask1);
            testTaskManager.historyManager.add(testTask1);
        }

        assertEquals(10, testTaskManager.historyManager.getHistory().size());
    }

}
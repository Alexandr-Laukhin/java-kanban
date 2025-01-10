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
        testTaskManager.createTask(testTask);
    }

    @Test
    void add() {
        testTaskManager.getTaskByID(1);

        assertNotNull(historyManager.getHistory());
    }

    @Test
    void historyArrayListShouldNotBeMoreThanTen() {
        for (int i = 1; i < 12; i++) {
            testTaskManager.createTask(testTask);
            historyManager.add(testTask);
        }

        assertEquals(10, historyManager.getHistory().size());
    }

    // Честно говоря, не очень понял, что тут имелось ввиду, даже после твоих пояснений в пачке, но все модификаторы
    // доступа выставил private, и постарался убрать testTaskManager откуда смог его убрать. Вроде теперь все должно быть правильно.

}
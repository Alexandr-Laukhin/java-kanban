package main;

import classes.Epic;
import classes.Status;
import classes.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest <InMemoryTaskManager> {

    private TaskManager testTaskManager;
    //  Тут я поправил с конкретного класса на интерфейс TaskManager, чтобы любой мог использоваться.
    //  Но есть подозрение, что я не так тебя понял. Поясни, пожалуйста, что имелось ввиду?
    private Epic testEpic;
    private SubTask testSubTask;
    private SubTask testSubTask2;

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void creation() {
        testTaskManager = new InMemoryTaskManager();
        testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic, testTaskManager);
        testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask, testTaskManager);
        testSubTask2 = new SubTask("Test subTask 2", "Test SubTask Description 2", 1);
        testTaskManager.createSubTask(testSubTask2, testTaskManager);
    }

    @Test
    void checkEpicStatusNew() {
        assertEquals(Status.NEW, testTaskManager.getEpicByID(1).getStatus());
    }

    @Test
    void checkEpicStatusDone() {
        testTaskManager.getSubTaskByID(2).setStatus(Status.DONE);
        testTaskManager.getSubTaskByID(3).setStatus(Status.DONE);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(2), testTaskManager);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3), testTaskManager);

        assertEquals(Status.DONE, testTaskManager.getEpicByID(1).getStatus());
    }

    @Test
    void checkEpicStatusNewAndDone() {
        testTaskManager.getSubTaskByID(2).setStatus(Status.NEW);
        testTaskManager.getSubTaskByID(3).setStatus(Status.DONE);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(2), testTaskManager);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3), testTaskManager);

        assertEquals(Status.IN_PROGRESS, testTaskManager.getEpicByID(1).getStatus());
    }

    @Test
    void checkEpicStatusInProgress() {
        testTaskManager.getSubTaskByID(2).setStatus(Status.IN_PROGRESS);
        testTaskManager.getSubTaskByID(3).setStatus(Status.IN_PROGRESS);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(2), testTaskManager);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3), testTaskManager);

        assertEquals(Status.IN_PROGRESS, testTaskManager.getEpicByID(1).getStatus());
    }

    @Test
    void checkIntersection() {
        testSubTask.setStartTime(LocalDate.now().atTime(13, 0));
        testSubTask2.setStartTime(LocalDate.now().atTime(13, 1));
        testSubTask.setDuration(Duration.ofMinutes(10));
        testSubTask2.setDuration(Duration.ofMinutes(10));
        testTaskManager.updateSubTask(testSubTask, testTaskManager);

        assertThrows(IllegalStateException.class, () -> testTaskManager.updateSubTask(testSubTask2, testTaskManager));
    }

}
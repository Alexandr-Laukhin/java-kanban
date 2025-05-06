package main;

import classes.Epic;
import classes.Status;
import classes.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest <InMemoryTaskManager> {

    private InMemoryTaskManager testTaskManager;
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
        testEpic = new Epic("Test epic", "Test Epic Description", Duration.ofMinutes(10));
        testTaskManager.createEpic(testEpic);
        testSubTask = new SubTask("Test subTask", "Test SubTask Description", Duration.ofMinutes(10), 1);
        testTaskManager.createSubTask(testSubTask);
        testSubTask2 = new SubTask("Test subTask 2", "Test SubTask Description 2", Duration.ofMinutes(10), 1);
        testTaskManager.createSubTask(testSubTask2);
    }

    @Test
    void checkEpicStatusNew() {
        assertEquals(Status.NEW, testTaskManager.getEpicByID(1).getStatus());
    }

    @Test
    void checkEpicStatusDone() {
        testTaskManager.getSubTaskByID(2).setStatus(Status.DONE);
        testTaskManager.getSubTaskByID(3).setStatus(Status.DONE);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(2));
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3));

        assertEquals(Status.DONE, testTaskManager.getEpicByID(1).getStatus());
    }

    @Test
    void checkEpicStatusNewAndDone() {
        testTaskManager.getSubTaskByID(2).setStatus(Status.NEW);
        testTaskManager.getSubTaskByID(3).setStatus(Status.DONE);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(2));
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3));

        assertEquals(Status.IN_PROGRESS, testTaskManager.getEpicByID(1).getStatus());
    }

    @Test
    void checkEpicStatusInProgress() {
        testTaskManager.getSubTaskByID(2).setStatus(Status.IN_PROGRESS);
        testTaskManager.getSubTaskByID(3).setStatus(Status.IN_PROGRESS);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(2));
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3));

        assertEquals(Status.IN_PROGRESS, testTaskManager.getEpicByID(1).getStatus());
    }

//    @Test
//    void getPrioritizedTasks() {
//
//        // создать 3 задачи, проверить порядок
//        //создать еще одну, проверить порядок
//    }
//
//    @Test
//    void segmentIntersection() {
//        // создать 2 пересекающихся метода и 1 не пересекающийся
//        //проверить на true и false
//    }
//
//    @Test
//    void findIntersectingTasks() {
//    }

}
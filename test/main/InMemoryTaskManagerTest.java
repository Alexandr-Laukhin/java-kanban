package main;

import classes.Status;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    // пометка для себя
    // В родителе перед каждым создается:
    // testTaskManager
    // testTaskManager - id 1
    // testEpic - id 2
    // testSubTask - id 3 - parentEpic id 2
    // testTask2 - id 4
    // testSubTask2 - id 5 - parentEpic id 2
    // testEpic2 - id 6

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void checkEpicStatusNew() {
        assertEquals(Status.NEW, testTaskManager.getEpicByID(2).getStatus());
    }

    @Test
    void checkEpicStatusDone() {
        testTaskManager.getSubTaskByID(3).setStatus(Status.DONE);
        testTaskManager.getSubTaskByID(5).setStatus(Status.DONE);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3));
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(5));

        assertEquals(Status.DONE, testTaskManager.getEpicByID(2).getStatus());
    }

    @Test
    void checkEpicStatusNewAndDone() {
        testTaskManager.getSubTaskByID(3).setStatus(Status.NEW);
        testTaskManager.getSubTaskByID(5).setStatus(Status.DONE);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3));
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(5));

        assertEquals(Status.IN_PROGRESS, testTaskManager.getEpicByID(2).getStatus());
    }

    @Test
    void checkEpicStatusInProgress() {
        testTaskManager.getSubTaskByID(3).setStatus(Status.IN_PROGRESS);
        testTaskManager.getSubTaskByID(5).setStatus(Status.IN_PROGRESS);
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(3));
        testTaskManager.updateSubTask(testTaskManager.getSubTaskByID(5));

        assertEquals(Status.IN_PROGRESS, testTaskManager.getEpicByID(2).getStatus());
    }

    @Test
    void checkIntersection() {
        testSubTask.setStartTime(LocalDate.of(2025, 5, 12).atTime(13, 0));
        testSubTask2.setStartTime(LocalDate.of(2025, 5, 12).atTime(13, 1));
        testSubTask.setDuration(Duration.ofMinutes(10));
        testSubTask2.setDuration(Duration.ofMinutes(10));

        assertThrows(IllegalStateException.class, () -> testTaskManager.updateSubTask(testSubTask));
    }
}
package main;

import classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTest {

    @Test
    void deleteTasks() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Task testTask = new Task("Test task", "Test Task Description");
        Task testTask2 = new Task("Test task2", "Test Task Description2");
        testTaskManager.createTask(testTask);
        testTaskManager.createTask(testTask2);
        testTaskManager.deleteTasks();

        Assertions.assertNull(testTaskManager.getTaskByID(1));
    }

    @Test
    void deleteEpics() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask);

        Epic testEpic2 = new Epic("Test epic2", "Test Epic Description2");
        testTaskManager.createEpic(testEpic2);
        SubTask testSubTask2 = new SubTask("Test subTask2", "Test SubTask Description2", 3);
        testTaskManager.createSubTask(testSubTask2);

        testTaskManager.deleteEpics();

        Assertions.assertNull(testTaskManager.getEpicByID(1));
        Assertions.assertNull(testTaskManager.getSubTaskByID(2));
        Assertions.assertNull(testTaskManager.getEpicByID(3));
        Assertions.assertNull(testTaskManager.getSubTaskByID(4));
    }

    @Test
    void deleteSubTasks() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        SubTask testSubTask2 = new SubTask("Test subTask2", "Test SubTask Description2", 1);
        testTaskManager.createSubTask(testSubTask);
        testTaskManager.createSubTask(testSubTask2);
        testTaskManager.deleteSubTasks();

        Assertions.assertNull(testTaskManager.getSubTaskByID(2));
    }

    @Test
    void deleteTaskByID() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Task testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);
        testTaskManager.deleteTaskByID(1);

        Assertions.assertNull(testTaskManager.getTaskByID(1));
    }

    @Test
    void deleteEpicByID() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask);
        testTaskManager.deleteEpicByID(1);

        Assertions.assertNull(testTaskManager.getEpicByID(1));
        Assertions.assertNull(testTaskManager.getSubTaskByID(2));
    }

    @Test
    void deleteSubTaskByID() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask);
        testTaskManager.deleteSubTaskByID(2);

        Assertions.assertNull(testTaskManager.getSubTaskByID(2));
    }
}
package main;

import classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskManagerTest {

    private TaskManager testTaskManager;
    private Task testTask;
    private Epic testEpic;
    private SubTask testSubTask;


    @BeforeEach
    void createTestTaskManager() {
        testTaskManager = Managers.getDefault();
        testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);
        testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        testSubTask = new SubTask("Test subTask", "Test SubTask Description", 2);
        testTaskManager.createSubTask(testSubTask);

    }

    @Test
    void createTaskTest() {
        assertEquals(testTask, testTaskManager.getTaskByID(1));
    }

    @Test
    void createEpic() {
        assertEquals(testEpic, testTaskManager.getEpicByID(2));
    }

    @Test
    void createSubTask() {
        assertEquals(testSubTask, testTaskManager.getSubTaskByID(3));
    }

    @Test
    void deleteTasks() {
        Task testTask2 = new Task("Test task2", "Test Task Description2");
        testTaskManager.createTask(testTask2);
        testTaskManager.deleteTasks();

        Assertions.assertNull(testTaskManager.getTaskByID(1));
    }

    @Test
    void deleteEpics() {
        Epic testEpic2 = new Epic("Test epic2", "Test Epic Description2");
        testTaskManager.createEpic(testEpic2);
        SubTask testSubTask2 = new SubTask("Test subTask2", "Test SubTask Description2", 4);
        testTaskManager.createSubTask(testSubTask2);

        testTaskManager.deleteEpics();

        Assertions.assertNull(testTaskManager.getEpicByID(2));
        Assertions.assertNull(testTaskManager.getSubTaskByID(3));
        Assertions.assertNull(testTaskManager.getEpicByID(4));
        Assertions.assertNull(testTaskManager.getSubTaskByID(5));
    }

    @Test
    void deleteSubTasks() {
        SubTask testSubTask2 = new SubTask("Test subTask2", "Test SubTask Description2", 2);
        testTaskManager.createSubTask(testSubTask2);
        testTaskManager.deleteSubTasks();

        Assertions.assertNull(testTaskManager.getSubTaskByID(3));
    }

    @Test
    void deleteTaskByID() {
        testTaskManager.deleteTaskByID(1);

        Assertions.assertNull(testTaskManager.getTaskByID(1));
    }

    @Test
    void deleteEpicByID() {
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 2);
        testTaskManager.createSubTask(testSubTask);
        testTaskManager.deleteEpicByID(2);

        Assertions.assertNull(testTaskManager.getEpicByID(2));
        Assertions.assertNull(testTaskManager.getSubTaskByID(3));
    }

    @Test
    void deleteSubTaskByID() {
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 2);
        testTaskManager.createSubTask(testSubTask);
        testTaskManager.deleteSubTaskByID(3);

        Assertions.assertNull(testTaskManager.getSubTaskByID(3));
    }

    @Test
    void updateTask() {
        String expectedName = "Updated Test task";
        testTask.setName("Updated Test task");

        assertEquals(expectedName, testTaskManager.getTaskByID(1).getName());
    }

    @Test
    void updateEpic() {
        String expectedName = "Updated Test Epic";
        testEpic.setName("Updated Test Epic");

        assertEquals(expectedName, testTaskManager.getEpicByID(2).getName());
    }

    @Test
    void updateSubTask() {
        String expectedName = "Updated Test SubTask";
        testSubTask.setName("Updated Test SubTask");

        assertEquals(expectedName, testTaskManager.getSubTaskByID(3).getName());
    }

    @Test
    void taskManagerLoadHistoryTest() {
        testTaskManager.getTaskByID(1);
        assertNotNull(testTaskManager.getHistory());
    }

    @Test
    void getSubTasksFromEpicByID() {
        assertNotNull(testTaskManager.getSubTasksFromEpicByID(2));  // простой вариант, как с историей

        assertEquals("Test subTask", testTaskManager.getSubTasksFromEpicByID(2).getFirst().getName());
        // так мне показалось более точно и правильно, решил сделать 2 варианта
    }

}
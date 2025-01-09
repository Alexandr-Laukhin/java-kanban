package main;

import classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskManagerTest {

    private InMemoryTaskManager testTaskManager;

    @BeforeEach
    void createTestTaskManager() {
        testTaskManager = new InMemoryTaskManager();
    }

    @Test
    void createTaskTest() {
        Task testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);

        Assertions.assertEquals(testTask, testTaskManager.getTaskByID(1));
    }

    @Test
    void createEpic() {
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);

        Assertions.assertEquals(testEpic, testTaskManager.getEpicByID(1));
    }

    @Test
    void createSubTask() {
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask);

        Assertions.assertEquals(testSubTask, testTaskManager.getSubTaskByID(2));
    }

    @Test
    void deleteTasks() {
        Task testTask = new Task("Test task", "Test Task Description");
        Task testTask2 = new Task("Test task2", "Test Task Description2");
        testTaskManager.createTask(testTask);
        testTaskManager.createTask(testTask2);
        testTaskManager.deleteTasks();

        Assertions.assertNull(testTaskManager.getTaskByID(1));
    }

    @Test
    void deleteEpics() {
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
        Task testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);
        testTaskManager.deleteTaskByID(1);

        Assertions.assertNull(testTaskManager.getTaskByID(1));
    }

    @Test
    void deleteEpicByID() {
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
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask);
        testTaskManager.deleteSubTaskByID(2);

        Assertions.assertNull(testTaskManager.getSubTaskByID(2));
    }

    @Test
    void updateTask() {
        String expectedName = "Updated Test task";
        Task testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);
        testTask.setName("Updated Test task");

        Assertions.assertEquals(expectedName, testTaskManager.getTaskByID(1).getName());
    }

    @Test
    void updateEpic() {
        String expectedName = "Updated Test Epic";
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        testEpic.setName("Updated Test Epic");

        Assertions.assertEquals(expectedName, testTaskManager.getEpicByID(1).getName());
    }

    @Test
    void updateSubTask() {
        String expectedName = "Updated Test SubTask";
        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask);
        testSubTask.setName("Updated Test SubTask");

        Assertions.assertEquals(expectedName, testTaskManager.getSubTaskByID(2).getName());
    }

}
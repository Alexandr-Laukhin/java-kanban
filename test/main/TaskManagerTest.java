package main;

import classes.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected Task testTask;
    protected Task testTask2;
    protected Epic testEpic;
    protected Epic testEpic2;
    protected SubTask testSubTask;
    protected SubTask testSubTask2;
    protected T testTaskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void createTestTaskManager() {
        testTaskManager = createTaskManager();
        testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);
        testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        testSubTask = new SubTask("Test subTask", "Test SubTask Description", 2);
        testTaskManager.createSubTask(testSubTask);
        testTask2 = new Task("Test task2", "Test Task Description2");
        testTaskManager.createTask(testTask2);
        testSubTask2 = new SubTask("Test subTask2", "Test SubTask Description2", 2);
        testTaskManager.createSubTask(testSubTask2);
        testEpic2 = new Epic("Test epic2", "Test Epic Description2");
        testTaskManager.createEpic(testEpic2);
    }

    @AfterEach
    void clearAll() {
        testTaskManager.deleteTasks();
        testTaskManager.deleteEpics();
        testTaskManager.deleteSubTasks();  // учитывая, что при удалении эпиков все сабтаски автоматом удаляются, этот вызов не особо нужен
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
        testTaskManager.getTaskByID(1);
        testTaskManager.getTaskByID(4);
        testTaskManager.getSubTaskByID(3);

        testTaskManager.deleteTasks();

        assertEquals(1, testTaskManager.getHistory().size());
        assertEquals(0, testTaskManager.getTasks().size());
    }

    @Test
    void deleteEpics() {
        testTaskManager.getEpicByID(2);
        testTaskManager.getSubTaskByID(3);
        testTaskManager.getTaskByID(1);

        testTaskManager.deleteEpics();

        assertEquals(1, testTaskManager.getHistory().size());
        assertEquals(0, testTaskManager.getEpics().size());
        assertEquals(0, testTaskManager.getSubTasks().size());
    }

    @Test
    void deleteSubTasks() {
        testTaskManager.getEpicByID(2);
        testTaskManager.getSubTaskByID(3);

        testTaskManager.deleteSubTasks();

        assertEquals(1, testTaskManager.getHistory().size());
        assertEquals(0, testTaskManager.getSubTasks().size());
    }

    @Test
    void deleteTaskByID() {
        testTaskManager.getTaskByID(1);
        testTaskManager.getEpicByID(2);
        testTaskManager.deleteTaskByID(1);

        assertEquals(1, testTaskManager.getHistory().size());
    }

    @Test
    void deleteEpicByID() {

        testTaskManager.getEpicByID(2);
        testTaskManager.getEpicByID(6);
        testTaskManager.getSubTaskByID(3);
        testTaskManager.getSubTaskByID(5);

        testTaskManager.deleteEpicByID(2);

        assertEquals(1, testTaskManager.getHistory().size());
    }

    @Test
    void deleteSubTaskByID() {
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 2);
        testTaskManager.createSubTask(testSubTask);

        testTaskManager.getSubTaskByID(3);
        testTaskManager.getSubTaskByID(5);
        testTaskManager.deleteSubTaskByID(3);

        assertEquals(1, testTaskManager.getHistory().size());
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
        testTaskManager.
                getTaskByID(1);
        assertEquals(1, testTaskManager.getHistory().size());
    }

    @Test
    void getSubTasksFromEpicByID() {
        assertNotNull(testTaskManager.getSubTasksFromEpicByID(2));  // простой вариант, как с историей

        assertEquals("Test subTask", testTaskManager.getSubTasksFromEpicByID(2).getFirst().getName());
        // так мне показалось более точно и правильно, решил сделать 2 варианта
    }
}
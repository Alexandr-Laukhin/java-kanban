package main;

import classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreationTest {

    @Test
    void createTaskTest() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        String expectedName = "Test task";

        Task testTask = new Task("Test task", "Test Task Description");
        // Task expectedTask = new Task("Test task", "Test Task Description");            // сначала пытался сравнить через создание задачи, но без создания
                                                                                          // expectedTask выходит ошибка NullPointerException. А так как мы проверяем
                                                                                          // как раз создание задачи, то создавать через проверяемый метод ожидаемый
                                                                                          // результат как-то не очень надежно выглядит. Если есть более изящный
                                                                                          // вариант, чем просто сравнить по имени, подскажи, пожалуйста,
        testTaskManager.createTask(testTask);

        Assertions.assertEquals(expectedName, testTaskManager.getTaskByID(1).getName());
    }

    @Test
    void createEpic() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        String expectedName = "Test epic";

        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        // Epic expectedEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);

        Assertions.assertEquals(expectedName, testTaskManager.getEpicByID(1).getName());
    }

    @Test
    void createSubTask() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        String expectedName = "Test subTask";

        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);

        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        // SubTask expectedSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask);

        Assertions.assertEquals(expectedName, testTaskManager.getSubTaskByID(2).getName());
    }

    @Test
    void epicCantBeUsedAsASubtask() {


    }
}
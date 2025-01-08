package main;

import classes.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTest {

    @Test
    void updateTask() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        String expectedName = "Updated Test task";

        Task testTask = new Task("Test task", "Test Task Description");
        testTaskManager.createTask(testTask);
        testTask = new Task("Updated Test task", "Updated Test Task Description");
        testTaskManager.updateTask(testTask);

        Assertions.assertEquals(expectedName, testTaskManager.getTaskByID(0).getName());

        /* Тут не совсем корректно работает. По умолчанию id у всех стоит 0, и когда мы обновляем задачу, id мы, в том
           числе, обновляем автоматом на 0. Вообще не понимаю как это обойти, если вручную не править id. Но ты говорил в прошлый раз, что мы сейчас
           не акцентируем внимание на том, как именно будет проходить создание объекта перед обновлением. Если
           предположить, что кто-то создаст правильную задачу с изначально правильным id и будет обновление на нее,
           то все работает корректно. (и в таком случае надо изменить id в последней строчке с 0 на нужный)

           И тот же вопрос к тесту истории (что там сохраняются предыдущие версии задач, до обновления).
           Без обновления не уверен, что получится как-то этот момент проверить. */
    }

    @Test
    void updateEpic() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        String expectedName = "Updated Test Epic";

        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        testEpic = new Epic("Updated Test Epic", " Updated Test Epic Description");
        testTaskManager.updateEpic(testEpic);

        Assertions.assertEquals(expectedName, testTaskManager.getEpicByID(0).getName());
    }

    @Test
    void updateSubTask() {
        InMemoryTaskManager testTaskManager = new InMemoryTaskManager();
        String expectedName = "Updated Test SubTask";

        Epic testEpic = new Epic("Test epic", "Test Epic Description");
        testTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("Test subTask", "Test SubTask Description", 1);
        testTaskManager.createSubTask(testSubTask);

        testSubTask = new SubTask("Updated Test SubTask", "Updated Test SubTask Description", 1);
        testTaskManager.updateTask(testSubTask);

        Assertions.assertEquals(expectedName, testTaskManager.getTaskByID(0).getName());
    }
}
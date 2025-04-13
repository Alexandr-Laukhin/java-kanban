package main;

import classes.Epic;
import classes.SubTask;
import classes.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

class FileBackedTaskManagerTest {

    private FileBackedTaskManager testFileBackedTaskManager;
    private Task testTask;
    private File tempFile;


    @BeforeEach
    void createManagerAndTask() throws IOException {
        testFileBackedTaskManager = new FileBackedTaskManager();
        testTask = new Task("TestTask", "Test description");
        tempFile = File.createTempFile("testTemp", ".csv");
        testFileBackedTaskManager.setSaveFileName(tempFile);
        testFileBackedTaskManager.createTask(testTask);
    }

    @AfterEach
    void deleteTempFile() {
        tempFile.delete();
    }

    @Test
    void testLoadFromFile() throws IOException {
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertEquals(1, secondManager.getTasks().size());
    }

    @Test
    void saveTest() throws IOException {
        testFileBackedTaskManager.save();
        Reader fileReader = new FileReader(tempFile);
        BufferedReader br = new BufferedReader(fileReader);
        String line = br.readLine();
        String expectedLine = "1, class classes.Task, TestTask, NEW, Test description";

        Assertions.assertEquals(expectedLine, line);
    }

    @Test
    void testToString() {
        String line = testFileBackedTaskManager.toString(testTask);
        String expectedLine = "1, class classes.Task, TestTask, NEW, Test description";

        Assertions.assertEquals(expectedLine, line);
    }

    @Test
    void testFromString() {
        tempFile.delete();
        String line = "1, class classes.Task, TestTask, NEW, Test description";
        Task task = FileBackedTaskManager.fromString(line);
        int expectedId = 1;
        String expectedName = "TestTask";

        Assertions.assertEquals(expectedId, task.getId());
        Assertions.assertEquals(expectedName, task.getName());
    }

    @Test
    void saveEmptyFile() throws IOException {
        FileBackedTaskManager empty = new FileBackedTaskManager();
        empty.setSaveFileName(tempFile);
        empty.save();
        BufferedReader br = new BufferedReader(new FileReader(tempFile));

        Assertions.assertNull(br.readLine());
    }

    @Test
    void loadEmptyFile() throws IOException {
        FileBackedTaskManager empty = new FileBackedTaskManager();
        empty.setSaveFileName(tempFile);
        empty.save();
        FileBackedTaskManager empryLoad = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertTrue(empryLoad.getTasks().isEmpty());
        Assertions.assertTrue(empryLoad.getEpics().isEmpty());
        Assertions.assertTrue(empryLoad.getSubTasks().isEmpty());
    }

    @Test
    void saveSomeTasks() throws IOException {
        Epic testEpic = new Epic("TestEpic", "Test epic description");
        testFileBackedTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("TestSubTask", "Test subtask description", 2);
        testFileBackedTaskManager.createTask(testSubTask);
        testFileBackedTaskManager.save();
        Reader fileReader = new FileReader(tempFile);
        BufferedReader br = new BufferedReader(fileReader);

        String line = br.readLine();
        String line2 = br.readLine();
        String line3 = br.readLine();
        String expectedLine = "1, class classes.Task, TestTask, NEW, Test description";
        String expectedLine3 = "2, class classes.Epic, TestEpic, NEW, Test epic description";
        String expectedLine2 = "3, class classes.SubTask, TestSubTask, NEW, Test subtask description, 2";

        Assertions.assertEquals(expectedLine, line);
        Assertions.assertEquals(expectedLine2, line2);
        Assertions.assertEquals(expectedLine3, line3);
    }

    @Test
    void loadSomeTasks() throws IOException {
        Epic testEpic = new Epic("TestEpic", "Test epic description");
        testFileBackedTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("TestSubTask", "Test subtask description", 2);
        testFileBackedTaskManager.createTask(testSubTask);
        testFileBackedTaskManager.save();
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertEquals("TestTask", secondManager.getTaskByID(1).getName());
        Assertions.assertEquals("TestSubTask", secondManager.getTaskByID(2).getName());
        Assertions.assertEquals("TestEpic", secondManager.getTaskByID(3).getName());

        // Привет! Подскажи, пожалуйста, почему такая странная очередность выдачи результата? Сохраняю сначала таску,
        // потом эпик, потом сабстаску, и, по логике, id должны быть 1 -таска, 2 -эпик, 3 -сабтаска ,
        // а записывается он в файл как таска-сабтаска-эпик.
    }
}
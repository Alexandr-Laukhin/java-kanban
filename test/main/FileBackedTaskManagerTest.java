package main;

import classes.Epic;
import classes.SubTask;
import classes.Task;
import classes.ToFromString;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static classes.ToFromString.fromString;

class FileBackedTaskManagerTest {

    private FileBackedTaskManager testFileBackedTaskManager;
    private Task testTask;
    private File tempFile;


    @BeforeEach
    void createManagerAndTask() throws IOException {
        testTask = new Task("TestTask", "Test description");
        tempFile = File.createTempFile("testTemp", ".csv");
        testFileBackedTaskManager = new FileBackedTaskManager(tempFile);
        testFileBackedTaskManager.createTask(testTask);
    }

    @AfterEach
    void deleteTempFile() {
        tempFile.delete();
    }

    @Test
    void testLoadFromFile() {
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertEquals(1, secondManager.getTasks().size());
    }

    @Test
    void saveTest() throws IOException {
        Reader fileReader = new FileReader(tempFile);
        BufferedReader br = new BufferedReader(fileReader);
        br.readLine();
        String line2 = br.readLine();
        String expectedLine = "1, TASK, TestTask, NEW, Test description";

        Assertions.assertEquals(expectedLine, line2);
    }

    @Test
    void testToString() {
        String line = ToFromString.toString(testTask);
        String expectedLine = "1, TASK, TestTask, NEW, Test description";

        Assertions.assertEquals(expectedLine, line);
    }

    @Test
    void testFromString() {
        tempFile.delete();
        String line = "1, TASK, TestTask, NEW, Test description";
        Task task = fromString(line);
        int expectedId = 1;
        String expectedName = "TestTask";

        Assertions.assertEquals(expectedId, task.getId());
        Assertions.assertEquals(expectedName, task.getName());
    }

    @Test
    void saveEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("testEmptyTemp", ".csv");
        BufferedReader br = new BufferedReader(new FileReader(emptyFile));

        Assertions.assertNull(br.readLine());
        emptyFile.delete();
    }

    @Test
    void loadEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("testEmptyTemp", ".csv");
        FileBackedTaskManager empryLoad = FileBackedTaskManager.loadFromFile(emptyFile);

        Assertions.assertTrue(empryLoad.getTasks().isEmpty());
        Assertions.assertTrue(empryLoad.getEpics().isEmpty());
        Assertions.assertTrue(empryLoad.getSubTasks().isEmpty());
        emptyFile.delete();
    }

    @Test
    void saveSomeTasks() throws IOException {
        Epic testEpic = new Epic("TestEpic", "Test epic description");
        testFileBackedTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("TestSubTask", "Test subtask description", 2);
        testFileBackedTaskManager.createTask(testSubTask);
        Reader fileReader = new FileReader(tempFile);
        BufferedReader br = new BufferedReader(fileReader);

        br.readLine();
        String line = br.readLine();
        String line2 = br.readLine();
        String line3 = br.readLine();
        String expectedLine = "1, TASK, TestTask, NEW, Test description";
        String expectedLine3 = "2, EPIC, TestEpic, NEW, Test epic description";
        String expectedLine2 = "3, SUBTASK, TestSubTask, NEW, Test subtask description, 2";

        Assertions.assertEquals(expectedLine, line);
        Assertions.assertEquals(expectedLine2, line2);
        Assertions.assertEquals(expectedLine3, line3);
    }

    @Test
    void loadSomeTasks() {
        Epic testEpic = new Epic("TestEpic", "Test epic description");
        testFileBackedTaskManager.createEpic(testEpic);
        SubTask testSubTask = new SubTask("TestSubTask", "Test subtask description", 2);
        testFileBackedTaskManager.createSubTask(testSubTask);
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertEquals(testFileBackedTaskManager.getTasks(), secondManager.getTasks());
    }
}
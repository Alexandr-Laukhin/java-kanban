package main;

import classes.Epic;
import classes.SubTask;
import classes.Task;
import classes.TaskConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;

import static classes.TaskConverter.fromString;

class FileBackedTaskManagerTest extends TaskManagerTest <FileBackedTaskManager> {

    protected TaskManager testFileBackedTaskManager;
    protected Task testTask;
    protected File tempFile;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            tempFile = File.createTempFile("testTemp", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new FileBackedTaskManager(tempFile);
    }

    @BeforeEach
    void createManagerAndTask() throws IOException {
        testTask = new Task("TestTask", "Test description");
        testTask.setStartTime(LocalDate.now().atTime(13, 0));
        testTask.setDuration(Duration.ofMinutes(10));
        tempFile = File.createTempFile("testTemp", ".csv");
        testFileBackedTaskManager = new FileBackedTaskManager(tempFile);
        testFileBackedTaskManager.createTask(testTask, testFileBackedTaskManager);
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
        String expectedLine = "1,TASK,TestTask,NEW,Test description,10.05.2025 13:00,10";

        Assertions.assertEquals(expectedLine, line2);
    }

    @Test
    void testToString() {
        String line = TaskConverter.toString(testTask);
        String expectedLine = "1,TASK,TestTask,NEW,Test description,10.05.2025 13:00,10";

        Assertions.assertEquals(expectedLine, line);
    }

    @Test
    void testFromString() {
        tempFile.delete();
        String line = "1,TASK,TestTask,NEW,Test description,10.05.2025 13:00,10";
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
        testFileBackedTaskManager.createEpic(testEpic, testFileBackedTaskManager);
        SubTask testSubTask = new SubTask("TestSubTask", "Test subtask description", 2);
        testFileBackedTaskManager.createSubTask(testSubTask, testFileBackedTaskManager);
        Reader fileReader = new FileReader(tempFile);
        BufferedReader br = new BufferedReader(fileReader);

        br.readLine();
        String line = br.readLine();
        String line2 = br.readLine();
        String line3 = br.readLine();
        String expectedLine = "1,TASK,TestTask,NEW,Test description,10.05.2025 13:00,10";
        String expectedLine2 = "2,EPIC,TestEpic,NEW,Test epic description,null";
        String expectedLine3 = "3,SUBTASK,TestSubTask,NEW,Test subtask description,null,0,2";

        Assertions.assertEquals(expectedLine, line);
        Assertions.assertEquals(expectedLine2, line2);
        Assertions.assertEquals(expectedLine3, line3);
    }

    @Test
    void loadSomeTasks() {
        Epic testEpic = new Epic("TestEpic", "Test epic description");
        testEpic.setStartTime(LocalDate.now().atTime(14, 0));
        testFileBackedTaskManager.createEpic(testEpic, testFileBackedTaskManager);
        SubTask testSubTask = new SubTask("TestSubTask", "Test subtask description", 2);
        testSubTask.setStartTime(LocalDate.now().atTime(14, 1));
        testSubTask.setDuration(Duration.ofMinutes(10));
        testFileBackedTaskManager.createSubTask(testSubTask, testFileBackedTaskManager);
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertEquals(testFileBackedTaskManager.getTasks(), secondManager.getTasks());
        Assertions.assertEquals(testFileBackedTaskManager.getEpics(), secondManager.getEpics());
        Assertions.assertEquals(testFileBackedTaskManager.getSubTasks(), secondManager.getSubTasks());
    }
}
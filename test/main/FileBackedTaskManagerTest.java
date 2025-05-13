package main;

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

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    // пометка для себя
    // В родителе перед каждым создается:
    // testTaskManager
    // testTaskManager - id 1
    // testEpic - id 2
    // testSubTask - id 3 - parentEpic id 2
    // testTask2 - id 4
    // testSubTask2 - id 5 - parentEpic id 2
    // testEpic2 - id 6


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
    void setTimeAndDuration() {
        testTask.setStartTime(LocalDate.of(2025, 5, 10).atTime(13, 0));
        testTask.setDuration(Duration.ofMinutes(10));
        testTaskManager.save();
    }

    @AfterEach
    void deleteTempFile() {
        tempFile.delete();
    }

    @Test
    void testLoadFromFile() {
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertEquals(2, secondManager.getTasks().size());
    }

    @Test
    void saveTest() throws IOException {
        testTask.setStartTime(LocalDate.of(2025, 5, 10).atTime(13, 0));
        testTask.setDuration(Duration.ofMinutes(10));

        Reader fileReader = new FileReader(tempFile);
        BufferedReader br = new BufferedReader(fileReader);
        br.readLine();
        String line2 = br.readLine();
        String expectedLine = "1,TASK,Test task,NEW,Test Task Description,10.05.2025 13:00,10";

        Assertions.assertEquals(expectedLine, line2);
    }

    @Test
    void testToString() {
        String line = TaskConverter.toString(testTask);
        String expectedLine = "1,TASK,Test task,NEW,Test Task Description,10.05.2025 13:00,10";

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
        FileBackedTaskManager emptyLoad = FileBackedTaskManager.loadFromFile(emptyFile);

        Assertions.assertTrue(emptyLoad.getTasks().isEmpty());
        Assertions.assertTrue(emptyLoad.getEpics().isEmpty());
        Assertions.assertTrue(emptyLoad.getSubTasks().isEmpty());
        emptyFile.delete();
    }

    @Test
    void saveSomeTasks() throws IOException {
        Reader fileReader = new FileReader(tempFile);
        BufferedReader br = new BufferedReader(fileReader);

        br.readLine();
        String line = br.readLine();
        String line2 = br.readLine();
        String line3 = br.readLine();
        String expectedLine = "1,TASK,Test task,NEW,Test Task Description,10.05.2025 13:00,10";
        String expectedLine2 = "4,TASK,Test task2,NEW,Test Task Description2,null,0";
        String expectedLine3 = "2,EPIC,Test epic,NEW,Test Epic Description,null";

        Assertions.assertEquals(expectedLine, line);
        Assertions.assertEquals(expectedLine2, line2);
        Assertions.assertEquals(expectedLine3, line3);
    }

    @Test
    void loadSomeTasks() {
        testEpic.setStartTime(LocalDate.now().atTime(14, 0));
        testSubTask.setStartTime(LocalDate.now().atTime(14, 1));
        testSubTask.setDuration(Duration.ofMinutes(10));
        testTaskManager.save();
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(tempFile);

        Assertions.assertEquals(testTaskManager.getTasks(), secondManager.getTasks());
        Assertions.assertEquals(testTaskManager.getEpics(), secondManager.getEpics());
        Assertions.assertEquals(testTaskManager.getSubTasks(), secondManager.getSubTasks());
    }
}
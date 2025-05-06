import classes.*;
import classes.Status;
import main.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static main.FileBackedTaskManager.loadFromFile;

public class Main {

    public static void main(String[] args) throws IOException {

        String saveTasks = "taskSaves.csv";
        File file = new File(saveTasks);
        // InMemoryTaskManager taskManager = new InMemoryTaskManager();
        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(file);


        Epic epic1 = new Epic("Test epic", "Test description", Duration.ofMinutes(10));
        Epic epic12 = new Epic("Test 12", "Test description 12", Duration.ofMinutes(10));
        SubTask subTask1 = new SubTask("test subtask 1", "test subtask description 1", Duration.ofMinutes(10), 1);
        SubTask subTask2 = new SubTask("test subtask 2", "test subtask description 2", Duration.ofMinutes(10), 1);
        SubTask subTask3 = new SubTask("test subtask 3", "test subtask description 3", Duration.ofMinutes(10), 1);

        backedTaskManager.createEpic(epic1);

        backedTaskManager.createSubTask(subTask1);
        backedTaskManager.createSubTask(subTask2);
        backedTaskManager.createSubTask(subTask3);

        // taskManager.deleteSubTasks();

        Epic epic2 = new Epic("Test 2", "Test description 2", Duration.ofMinutes(10));
        backedTaskManager.createEpic(epic2);
        backedTaskManager.createEpic(epic12);

        SubTask subTask12 = new SubTask("test subtask 12", "test subtask description 12", Duration.ofMinutes(10), 6);
        backedTaskManager.createSubTask(subTask12);

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.DONE);
        subTask3.setStatus(Status.DONE);

        backedTaskManager.updateSubTask(subTask1);
        backedTaskManager.updateSubTask(subTask2);
        backedTaskManager.updateSubTask(subTask3);

        subTask3.setStatus(Status.NEW);
        backedTaskManager.updateSubTask(subTask3);

        Task taskTest = new Task("Test task", "Test Task Description", Duration.ofMinutes(10));
        backedTaskManager.createTask(taskTest);
        backedTaskManager.getTaskByID(8);

        backedTaskManager.getEpicByID(1);
        backedTaskManager.getSubTaskByID(2);
        backedTaskManager.getSubTaskByID(3);
        backedTaskManager.getSubTaskByID(4);
        backedTaskManager.getHistory();
        backedTaskManager.getSubTaskByID(2);
        backedTaskManager.getSubTaskByID(2);
        backedTaskManager.getSubTaskByID(2);
        backedTaskManager.getSubTaskByID(2);
        backedTaskManager.getHistory();


        System.out.println(backedTaskManager.getHistory());
        List<Task> firstArray = backedTaskManager.getHistory();
        for (Task arrayTask : firstArray) {
            System.out.println(arrayTask.getName());
        }

        System.out.println("------------------");

        //System.out.println(loadFromFile(file).getHistory());

        FileBackedTaskManager managerFromFile = loadFromFile(file);

        List<Task> tasksFromFile = managerFromFile.getTasks();
        List<Epic> epicsFromFile = managerFromFile.getEpics();
        List<SubTask> subTasksFromFile = managerFromFile.getSubTasks();

        for (Task arrayTask : tasksFromFile) {
            System.out.println(arrayTask.getName());
        }

        for (Task arrayTask : epicsFromFile) {
            System.out.println(arrayTask.getName());
        }

        for (Task arrayTask : subTasksFromFile) {
            System.out.println(arrayTask.getName());
        }
    }

}



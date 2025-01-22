import classes.*;
import classes.Status;
import main.InMemoryHistoryManager;
import main.InMemoryTaskManager;
import main.TaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();


        Epic epic1 = new Epic("Test epic", "Test description");
        Epic epic12 = new Epic("Test 12", "Test description 12");
        SubTask subTask1 = new SubTask("test subtask 1", "test subtask description 1", 1);
        SubTask subTask2 = new SubTask("test subtask 2", "test subtask description 2", 1);
        SubTask subTask3 = new SubTask("test subtask 3", "test subtask description 3", 1);

        taskManager.createEpic(epic1);

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        // taskManager.deleteSubTasks();

        Epic epic2 = new Epic("Test 2", "Test description 2");
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic12);

        SubTask subTask12 = new SubTask("test subtask 12", "test subtask description 12", 6);
        taskManager.createSubTask(subTask12);
        // taskManager.deleteEpicByID(1);


//        taskManager.deleteSubTaskByID(2);
//        taskManager.deleteSubTaskByID(3);
//        taskManager.deleteSubTaskByID(4);


        Task task = new Task("Test task 85", "Test description 85");

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.DONE);
        subTask3.setStatus(Status.DONE);

        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        taskManager.updateSubTask(subTask3);

        subTask3.setStatus(Status.NEW);
        taskManager.updateSubTask(subTask3);
//        taskManager.deleteSubTaskByID(2);
//        taskManager.deleteSubTaskByID(3);

        //taskManager.deleteSubTasks();
        Task taskTest = new Task("Test task", "Test Task Description");
        taskManager.createTask(taskTest);
        taskManager.getTaskByID(8);

        taskManager.getEpicByID(1);
        taskManager.getSubTaskByID(2);
        taskManager.getSubTaskByID(3);
        taskManager.getSubTaskByID(4);
        taskManager.getHistory();
        taskManager.getSubTaskByID(2);
        taskManager.getSubTaskByID(2);
        taskManager.getSubTaskByID(2);
        taskManager.getSubTaskByID(2);
        taskManager.getHistory();

        // комментарий для проверки работы веток в гит
        // dnjhjq rjvvtynfhbq




    }
}



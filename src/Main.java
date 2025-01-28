import classes.*;

import main.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();  // пользовательский сценарий из задания прописал

        Epic epic1 = new Epic("Test epic", "Test description");
        Epic epic2 = new Epic("Test epic 2", "Test description 2");
        SubTask subTask1 = new SubTask("test subtask 1", "test subtask description 1", 1);
        SubTask subTask2 = new SubTask("test subtask 2", "test subtask description 2", 1);
        SubTask subTask3 = new SubTask("test subtask 3", "test subtask description 3", 1);
        Task taskTest = new Task("Test task", "Test Task Description");
        Task taskTest2 = new Task("Test task2", "Test Task Description2");

        taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createEpic(epic2);
        taskManager.createTask(taskTest);
        taskManager.createTask(taskTest2);

        taskManager.getTaskByID(6);
        taskManager.getTaskByID(7);
        taskManager.getTaskByID(6);
        taskManager.getTaskByID(7);
        taskManager.getSubTaskByID(2);
        taskManager.getSubTaskByID(3);
        taskManager.getSubTaskByID(4);
        taskManager.getSubTaskByID(2);
        taskManager.getSubTaskByID(3);
        taskManager.getSubTaskByID(4);
        taskManager.getEpicByID(1);
        taskManager.getEpicByID(5);
        taskManager.getEpicByID(1);
        taskManager.getEpicByID(5);
        taskManager.getTaskByID(6);

        taskManager.getHistory().stream().map(Task::getName).forEach(System.out::println);
        System.out.println("-------------------------------");

        taskManager.deleteTaskByID(6);

        taskManager.getHistory().stream().map(Task::getName).forEach(System.out::println);
        System.out.println("-------------------------------");

        taskManager.deleteEpicByID(1);

        taskManager.getHistory().stream().map(Task::getName).forEach(System.out::println);
        System.out.println("-------------------------------");

        taskManager.deleteTaskByID(7);
        taskManager.deleteEpicByID(5);


    }
}



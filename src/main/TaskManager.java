package main;

import classes.Epic;
import classes.SubTask;
import classes.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void createTask(Task task, TaskManager taskManager);

    void createEpic(Epic epic, TaskManager taskManager);

    void createSubTask(SubTask subTask, TaskManager taskManager);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getSubTasks();

    ArrayList<SubTask> getSubTasksFromEpicByID(int epicID);

    List<Task> getHistory();

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    void deleteTaskByID(int taskID);

    void deleteEpicByID(int epicID);

    void deleteSubTaskByID(int subTaskID);

    Task getTaskByID(int taskID);

    Epic getEpicByID(int epicID);

    SubTask getSubTaskByID(int subTaskID);

    void updateTask(Task task, TaskManager taskManager);

    void updateEpic(Epic epic, TaskManager taskManager);

    void updateSubTask(SubTask subTask, TaskManager taskManager);

}

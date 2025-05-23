package main;

import classes.Epic;
import classes.SubTask;
import classes.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getSubTasks();

    ArrayList<SubTask> getSubTasksFromEpicByID(int epicID);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    void deleteTaskByID(int taskID);

    void deleteEpicByID(int epicID);

    void deleteSubTaskByID(int subTaskID);

    Task getTaskByID(int taskID);

    Epic getEpicByID(int epicID);

    SubTask getSubTaskByID(int subTaskID);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);
}

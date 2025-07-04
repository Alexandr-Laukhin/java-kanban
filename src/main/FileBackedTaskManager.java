package main;

import classes.*;
import classes.exceptions.ManagerLoadException;
import classes.exceptions.ManagerSaveException;

import java.io.*;
import java.util.stream.Collectors;

import static classes.TaskConverter.fromString;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File saveFileName;

    public FileBackedTaskManager(File saveFileName) {
        this.saveFileName = saveFileName;
    }

    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try (Reader fileReader = new FileReader(file);

             BufferedReader br = new BufferedReader(fileReader)) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                Task task = fromString(line);

                switch (task.getType()) {
                    case TASK -> {
                        taskManager.tasks.put(task.getId(), task);
                        taskManager.counter++;
                    }
                    case EPIC -> {
                        taskManager.epics.put(task.getId(), (Epic) task);
                        taskManager.counter++;
                    }
                    case SUBTASK -> {
                        taskManager.subTasks.put(task.getId(), (SubTask) task);
                        taskManager.counter++;
                    }
                }
            }

            for (SubTask subTask : taskManager.getSubTasks()) {
                Epic parentEpic = taskManager.epics.get(subTask.getParentID());
                int subTaskId = subTask.getId();
                if (!parentEpic.getSubTasksID().contains(subTaskId)) {
                    parentEpic.getSubTasksID().add(subTask.getId());
                }
            }

        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка загрузки задач из файла");
        }
        return taskManager;
    }

    protected void save() {

        try (Writer fileWriter = new FileWriter(saveFileName)) {

            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");

            if (!getTasks().isEmpty()) {
                fileWriter.write(getTasks().stream()
                        .map(TaskConverter::toString)
                        .collect(Collectors.joining("\n")));
            }

            if (!getEpics().isEmpty()) {
                fileWriter.write("\n");
                fileWriter.write(getEpics().stream()
                        .map(TaskConverter::toString)
                        .collect(Collectors.joining("\n")));
            }

            if (!getSubTasks().isEmpty()) {
                fileWriter.write("\n");
                fileWriter.write(getSubTasks().stream()
                        .map(TaskConverter::toString)
                        .collect(Collectors.joining("\n")));
            }


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public void deleteTaskByID(int taskID) {
        super.deleteTaskByID(taskID);
        save();
    }

    @Override
    public void deleteEpicByID(int epicID) {
        super.deleteEpicByID(epicID);
        save();
    }

    @Override
    public void deleteSubTaskByID(int subTaskID) {
        super.deleteSubTaskByID(subTaskID);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }
}

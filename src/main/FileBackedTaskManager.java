package main;

import classes.*;

import java.io.*;

import static classes.ToFromString.fromString;

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
                if (task.getType() == TaskTypes.TASK) {
                    taskManager.updateTask(task);
                } else if (task.getType() == TaskTypes.EPIC) {
                    taskManager.updateEpic((Epic) task);
                } else if (task.getType() == TaskTypes.SUBTASK) {
                    taskManager.updateSubTask((SubTask) task);
                }
            }

        } catch (IOException e) {
            try {
                throw new ManagerLoadException("Ошибка загрузки задач из файла");
            } catch (ManagerLoadException ex) {
                throw new RuntimeException(ex);
            }
        }
        return taskManager;
    }

    private void save() {

        try (Writer fileWriter = new FileWriter(saveFileName)) {

            fileWriter.write("id,type,name,status,description,epic\n");

            for (Task taskObject : getTasks()) {
                String convertedTask = ToFromString.toString(taskObject);
                fileWriter.write(convertedTask + "\n");
            }

            for (Epic epicObject : getEpics()) {
                String convertedEpic = ToFromString.toString(epicObject);
                fileWriter.write(convertedEpic + "\n");
            }

            for (SubTask subTaskObject : getSubTasks()) {
                String convertedSubTask = ToFromString.toString(subTaskObject);
                fileWriter.write(convertedSubTask + "\n");
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка сохранения");
            } catch (ManagerSaveException ex) {
                throw new RuntimeException(ex);
            }
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

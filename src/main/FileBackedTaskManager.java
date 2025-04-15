package main;

import classes.*;

import java.io.*;

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
                    case TASK -> taskManager.tasks.put(task.getId(), task);
                    case EPIC -> taskManager.epics.put(task.getId(), (Epic) task);
                    case SUBTASK -> taskManager.subTasks.put(task.getId(), (SubTask) task);
                }
            }

        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка загрузки задач из файла");
        }
        return taskManager;
    }

    private void save() {

        try (Writer fileWriter = new FileWriter(saveFileName)) {

            fileWriter.write("id,type,name,status,description,epic\n");

            for (Task taskObject : getTasks()) {
                String convertedTask = TaskConverter.toString(taskObject);
                fileWriter.write(convertedTask + "\n");
            }

            for (Epic epicObject : getEpics()) {
                String convertedEpic = TaskConverter.toString(epicObject);
                fileWriter.write(convertedEpic + "\n");
            }

            for (SubTask subTaskObject : getSubTasks()) {
                String convertedSubTask = TaskConverter.toString(subTaskObject);
                fileWriter.write(convertedSubTask + "\n");
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

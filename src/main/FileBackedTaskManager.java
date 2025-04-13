package main;

import classes.Epic;
import classes.Status;
import classes.SubTask;
import classes.Task;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {



    private File saveFileName = new File("taskSaves.csv");

    public FileBackedTaskManager() {
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {

        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        try (Reader fileReader = new FileReader(file);
             BufferedReader br = new BufferedReader(fileReader)) {
            while (br.ready()) {
                String line = br.readLine();
                Task task = fromString(line);
                if (task instanceof Task) {
                    taskManager.createTask(task);
                } else if (task instanceof Epic) {
                    taskManager.createEpic((Epic) task);
                } else {
                    taskManager.createSubTask((SubTask) task);
                }
            }

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            e.printStackTrace();
        }

        return taskManager;
    }


    public void save() throws IOException {

        try (Writer fileWriter = new FileWriter(saveFileName)) {

            for (Task taskObject : getTasks()) {
                String convertedTask = toString(taskObject);
                fileWriter.write(convertedTask + "\n");
            }

            for (Epic epicObject : getEpics()) {
                String convertedEpic = toString(epicObject);
                fileWriter.write(convertedEpic + "\n");
            }

            for (SubTask subTaskObject : getSubTasks()) {
                String convertedSubTask = toString(subTaskObject);
                fileWriter.write(convertedSubTask + "\n");
            }
        }
    }

    public String toString(Task task) {
        String convertedTask;
        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            convertedTask = subTask.getId() + ", " + subTask.getClass() + ", " + subTask.getName() + ", " + subTask.getStatus() + ", " + subTask.getDescription() + ", " + subTask.getParentID();
        } else {
            convertedTask = task.getId() + ", " + task.getClass() + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription();
        }
        return convertedTask;
    }

    public static Task fromString(String value) {
        String[] convertedString = value.split(", ");
        Task task;

        if (convertedString[1].equals("class classes.Task")) {
            task = new Task(convertedString[2], convertedString[4]);
            task.setId(Integer.parseInt(convertedString[0]));
            if (convertedString[3].equals("NEW")) {
                task.setStatus(Status.NEW);
            } else if (convertedString[3].equals("IN_PROGRESS")) {
                task.setStatus(Status.IN_PROGRESS);
            } else {
                task.setStatus(Status.DONE);
            }

        } else if (convertedString[1].equals("class classes.Epic")) {
            Epic epic = new Epic(convertedString[2], convertedString[4]);
            epic.setId(Integer.parseInt(convertedString[0]));
            if (convertedString[3].equals("NEW")) {
                epic.setStatus(Status.NEW);
            } else if (convertedString[3].equals("IN_PROGRESS")) {
                epic.setStatus(Status.IN_PROGRESS);
            } else {
                epic.setStatus(Status.DONE);
            }
            task = epic;

        } else {
            SubTask subTask = new SubTask(convertedString[2], convertedString[4], Integer.parseInt(convertedString[5]));
            subTask.setId(Integer.parseInt(convertedString[0]));
            if (convertedString[3].equals("NEW")) {
                subTask.setStatus(Status.NEW);
            } else if (convertedString[3].equals("IN_PROGRESS")) {
                subTask.setStatus(Status.IN_PROGRESS);
            } else {
                subTask.setStatus(Status.DONE);
            }
            task = subTask;
        }
        return task;
    }

    public File getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(File saveFileName) {
        this.saveFileName = saveFileName;
    }

    @Override
    public void createTask(Task task) {
        try {
            super.createTask(task);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при создании задачи: " + e.getMessage());
        }
    }

    @Override
    public void createEpic(Epic epic) {
        try {
            super.createEpic(epic);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при создании эпика: " + e.getMessage());
        }
    }

    @Override
    public void createSubTask(SubTask subTask) {
        try {
            super.createSubTask(subTask);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при создании подзадачи: " + e.getMessage());
        }
    }

    @Override
    public void deleteTasks() {
        try {
            super.deleteTasks();
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при удалении задач: " + e.getMessage());
        }
    }

    @Override
    public void deleteEpics() {
        try {
            super.deleteEpics();
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при удалении эпиков: " + e.getMessage());
        }
    }

    @Override
    public void deleteSubTasks() {
        try {
            super.deleteSubTasks();
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при удалении подзадач: " + e.getMessage());
        }
    }

    @Override
    public void deleteTaskByID(int taskID) {
        try {
            super.deleteTaskByID(taskID);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при удалении задачи: " + e.getMessage());
        }
    }

    @Override
    public void deleteEpicByID(int epicID) {
        try {
            super.deleteEpicByID(epicID);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при удалении эпика: " + e.getMessage());
        }
    }

    @Override
    public void deleteSubTaskByID(int subTaskID) {
        try {
            super.deleteSubTaskByID(subTaskID);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при удалении подзадачи: " + e.getMessage());
        }
    }

    @Override
    public void updateTask(Task task) {
        try {
            super.updateTask(task);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при обновлении задачи: " + e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        try {
            super.updateEpic(epic);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при обновлении эпика: " + e.getMessage());
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        try {
            super.updateSubTask(subTask);
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при обновлении подзадачи: " + e.getMessage());
        }
    }
}

package classes;

public class ToFromString {

    public static String toString(Task task) {
        String convertedTask;
        if (task.getType() == TaskTypes.SUBTASK) {
            SubTask subTask = (SubTask) task;
            convertedTask = subTask.getId() + ", " + subTask.getType() + ", " + subTask.getName() + ", " + subTask.getStatus() + ", " + subTask.getDescription() + ", " + subTask.getParentID();
        } else {
            convertedTask = task.getId() + ", " + task.getType() + ", " + task.getName() + ", " + task.getStatus() + ", " + task.getDescription();
        }
        return convertedTask;
    }

    public static Task fromString(String value) {
        String[] convertedString = value.split(", ");
        Task task;

        if (convertedString[1].equals("TASK")) {
            task = new Task(convertedString[2], convertedString[4]);
            task.setId(Integer.parseInt(convertedString[0]));
            if (convertedString[3].equals("NEW")) {
                task.setStatus(Status.NEW);
            } else if (convertedString[3].equals("IN_PROGRESS")) {
                task.setStatus(Status.IN_PROGRESS);
            } else {
                task.setStatus(Status.DONE);
            }

        } else if (convertedString[1].equals("EPIC")) {
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
}

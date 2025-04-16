package classes;

public class TaskConverter {

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
        int id = Integer.parseInt(convertedString[0]);
        TaskTypes type = TaskTypes.valueOf(convertedString[1]);
        String taskName = convertedString[2];
        Status status = Status.valueOf(convertedString[3]);
        String taskDescription = convertedString[4];
        int parentId;

        return switch (type) {
            case TASK -> new Task(taskName, taskDescription, status, id);
            case EPIC -> new Epic(taskName, taskDescription, status, id);
            case SUBTASK -> {
                parentId = Integer.parseInt(convertedString[5]);
                yield new SubTask(taskName, taskDescription, status, id, parentId);
            }
        }; // так мне показалось лакончинее и красивее, если нужно, верну на if/else
    }
}

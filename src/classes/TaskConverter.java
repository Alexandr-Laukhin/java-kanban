package classes;

import java.time.Duration;

public class TaskConverter {

    public static String toString(Task task) {
        return task.getType() == TaskTypes.SUBTASK
                ? String.format("%d, %s, %s, %s, %s, %d, %d",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task.getDuration().toMinutes(),
                ((SubTask) task).getParentID())
                : String.format("%d, %s, %s, %s, %s, %d",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task.getDuration().toMinutes());
    }

    public static Task fromString(String value) {
        String[] convertedString = value.split(", ");
        int id = Integer.parseInt(convertedString[0]);
        TaskTypes type = TaskTypes.valueOf(convertedString[1]);
        String taskName = convertedString[2];
        Status status = Status.valueOf(convertedString[3]);
        String taskDescription = convertedString[4];
        Duration duration = Duration.ofMinutes(Integer.parseInt(convertedString[5]));
        int parentId;

        return switch (type) {
            case TASK -> new Task(taskName, taskDescription, status, id, duration);
            case EPIC -> new Epic(taskName, taskDescription, status, id, duration);
            case SUBTASK -> {
                parentId = Integer.parseInt(convertedString[6]);
                yield new SubTask(taskName, taskDescription, status, id, duration, parentId);
            }
        };
    }
}

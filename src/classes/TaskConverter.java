package classes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskConverter {

    public static String toString(Task task) {
        String startTime = null;
        long duration = 0;

        if (task.getStartTimeCheck().isPresent()) {
            startTime = task.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }

        if (task.getDurationCheck().isPresent()) {
            duration = task.getDuration().toMinutes();
        }

        return switch (task.getType()) {
            case TASK -> String.format("%d,%s,%s,%s,%s,%s,%d",
                    task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(), startTime, duration);

            case EPIC -> String.format("%d,%s,%s,%s,%s,%s",
                    task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(), startTime);

            case SUBTASK -> String.format("%d,%s,%s,%s,%s,%s,%d,%d",
                    task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(), startTime, duration,
                    ((SubTask) task).getParentID());
        };
    }

    public static Task fromString(String value) {
        String[] convertedString = value.split(",");
        int id = Integer.parseInt(convertedString[0]);
        TaskTypes type = TaskTypes.valueOf(convertedString[1]);
        String taskName = convertedString[2];
        Status status = Status.valueOf(convertedString[3]);
        String taskDescription = convertedString[4];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime startTime;
        Duration duration;
        int parentId;

        return switch (type) {
            case TASK -> {
                Task task = new Task(taskName, taskDescription, status, id);
                if (!convertedString[5].equals("null")) {
                    startTime = LocalDateTime.parse(convertedString[5], formatter);
                    task.setStartTime(startTime);
                }
                if (!convertedString[6].equals("0")) {
                    duration = Duration.ofMinutes(Integer.parseInt(convertedString[6]));
                    task.setDuration(duration);
                }
                yield task;
            }

            case EPIC -> {
                Epic epic = new Epic(taskName, taskDescription, status, id);
                if (!convertedString[5].equals("null")) {
                    startTime = LocalDateTime.parse(convertedString[5], formatter);
                    epic.setStartTime(startTime);
                }
                yield epic;
            }

            case SUBTASK -> {
                parentId = Integer.parseInt(convertedString[7]);
                SubTask subTask = new SubTask(taskName, taskDescription, status, id, parentId);

                if (!convertedString[5].equals("null")) {
                    startTime = LocalDateTime.parse(convertedString[5], formatter);
                    subTask.setStartTime(startTime);
                }
                if (!convertedString[6].equals("0")) {
                    duration = Duration.ofMinutes(Integer.parseInt(convertedString[6]));
                    subTask.setDuration(duration);
                }

                yield subTask;
            }
        };
    }
}
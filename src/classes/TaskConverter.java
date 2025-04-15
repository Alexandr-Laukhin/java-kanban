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
        Task task;
        int id = Integer.parseInt(convertedString[0]);
        TaskTypes type = TaskTypes.valueOf(convertedString[1]);
        String taskName = convertedString[2];
        Status status = Status.valueOf(convertedString[3]);
        String taskDescription = convertedString[4];
        int parentId;


        if (type == TaskTypes.TASK) {
            task = new Task(taskName, taskDescription);
            task.setId(id);
            task.setStatus(status);

            return task;

        } else if (type == TaskTypes.EPIC) {
            Epic epic = new Epic(taskName, taskDescription);
            epic.setId(id);
            epic.setStatus(status);

            return epic;

        } else {
            parentId = Integer.parseInt(convertedString[5]);
            SubTask subTask = new SubTask(taskName, taskDescription, parentId);
            subTask.setId(id);
            subTask.setStatus(status);

            return subTask;

            // Ты писал, что здесь все можно выразить одной строчкой, я не смог найти другого метода, кроме как
            // записать все то же самое, что у меня уже записано, только в одну строчку. Можешь более подробно
            // пояснить, что имелось ввиду? У меня вышло вот такая запись:
            // SubTask subTask = new SubTask(taskName, taskDescription, Integer.parseInt(convertedString[5]));
            //subTask.setId(id); subTask.setStatus(status); return subTask;

            // Но она, как мне показалось, менее читабельна, чем разбитая по строчкам. Хотя, если нужно, записать так не проблема.
        }
    }
}

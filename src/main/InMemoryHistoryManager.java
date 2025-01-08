package main;

import classes.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> history = new ArrayList<>();


    @Override
    public List<Task> getHistory() {
        for (Task task : history) {
            System.out.println(task.getName());
        }
        return history;
    }

    @Override
    public <T extends Task> void add(T task) {
            if (history.size() >= 10) {
                history.removeFirst();
            }
            history.add(task);
        }
    }





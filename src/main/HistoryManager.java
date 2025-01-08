package main;

import classes.Task;

import java.util.List;

public interface HistoryManager {


    List<Task> getHistory();

    <T extends Task> void add(T task);
}

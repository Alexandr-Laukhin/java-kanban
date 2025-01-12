package classes;

import main.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void subtasksWithTheSameIdShouldBeEqual() {
        Epic testEpic = new Epic ("First epic", "First Description");
        testEpic.setId(1);
        SubTask subTask = new SubTask("First subtask", "First subtask description", 1);
        SubTask subTask1 = new SubTask("Second subtask", "Second subtask description", 1);

        assertEquals(subTask, subTask1);
    }

}
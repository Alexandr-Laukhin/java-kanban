package classes;


import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void subtasksWithTheSameIdShouldBeEqual() {
        Epic testEpic = new Epic("First epic", "First Description", Duration.ofMinutes(10));
        testEpic.setId(1);
        SubTask subTask = new SubTask("First subtask", "First subtask description", Duration.ofMinutes(10), 1);
        SubTask subTask1 = new SubTask("Second subtask", "Second subtask description", Duration.ofMinutes(10), 1);

        assertEquals(subTask, subTask1);
    }

}
package classes;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicsWithTheSameIdShouldBeEqual() {
        Epic testEpic = new Epic("First epic", "First Description", Duration.ofMinutes(10));
        Epic testEpic1 = new Epic("Second epic", "Second description", Duration.ofMinutes(10));

        assertEquals(testEpic, testEpic1);
    }
}
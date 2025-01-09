package classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void managersLoadObjectTest() {
        assertNotNull(Managers.getDefault());
    }
}
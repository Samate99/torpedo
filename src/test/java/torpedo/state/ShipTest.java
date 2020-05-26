package torpedo.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShipTest {
    @Test
    void testIsOverlap() {
        Ship ship1 = new Ship(0, 0, 1, 3);
        Ship ship2 = new Ship(1, 0, 1, 3);
        Ship ship3 = new Ship(5, 0, 1, 3);
        assertTrue(ship1.isOverlap(ship2, 1));
        assertFalse(ship1.isOverlap(ship3, 1));
    }
}

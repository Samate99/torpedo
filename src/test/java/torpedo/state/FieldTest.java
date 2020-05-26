package torpedo.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    @Test
    void testGetNextShipSize() {
        Field field = new Field();
        assertEquals(5, field.getNextSize());
        field.addShip(0, 0, false);
        assertEquals(4, field.getNextSize());
        field.addShip(2, 0, false);
        assertEquals(3, field.getNextSize());
        field.addShip(4, 0, false);
        assertEquals(3, field.getNextSize());
        field.addShip(6, 0, false);
        assertEquals(2, field.getNextSize());
        field.addShip(8, 0, false);
        assertEquals(0, field.getNextSize());
    }

    @Test
    void testGetNextShipName() {
        Field field = new Field();
        assertEquals("Carrier", field.getNextShipName());
        field.addShip(0, 0, false);
        assertEquals("Battleship", field.getNextShipName());
        field.addShip(2, 0, false);
        assertEquals("Destroyer", field.getNextShipName());
        field.addShip(4, 0, false);
        assertEquals("Submarine", field.getNextShipName());
        field.addShip(6, 0, false);
        assertEquals("Patrol Boat", field.getNextShipName());
        field.addShip(8, 0, false);
        assertEquals("Unknown", field.getNextShipName());
    }

    @Test
    void testAddShip() {
        Field field = new Field();
        assertTrue(field.addShip(0, 0, false));
        assertFalse(field.addShip(0, 0, false));
        assertFalse(field.addShip(1, 0, false));
        assertTrue(field.addShip(2, 0, true));
        assertFalse(field.addShip(0, 8, false));
        assertFalse(field.addShip(8, 1, true));
    }

    @Test
    void testTryGuess() {
        Field field = new Field();
        field.addShip(0, 0, false);
        assertTrue(field.tryGuess(0, 0));
        assertFalse(field.tryGuess(0, 0));
        assertFalse(field.tryGuess(4, 0));
    }

    @Test
    void testIsSolved() {
        Field field = new Field();
        field.addShip(0, 0, true);

        assertFalse(field.isSolved());

        field.tryGuess(0, 0);
        field.tryGuess(1, 0);
        field.tryGuess(2, 0);

        assertFalse(field.isSolved());

        field.tryGuess(3, 0);
        field.tryGuess(4, 0);

        assertTrue(field.isSolved());

        field.addShip(6, 0, false);
        assertFalse(field.isSolved());
        field.tryGuess(6, 0);
        field.tryGuess(6, 1);
        field.tryGuess(6, 2);
        field.tryGuess(6, 3);
        assertTrue(field.isSolved());
    }

    @Test
    void testGetters() {
        Field field = new Field();
        assertEquals(0, field.getShips().size());
        field.addShip(0, 0, true);
        assertEquals(1, field.getShips().size());
        assertEquals(0, field.getTestedFields().size());
        field.tryGuess(0, 0);
        assertEquals(1, field.getTestedFields().size());
    }
}

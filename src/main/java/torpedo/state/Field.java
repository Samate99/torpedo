package torpedo.state;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Field {
    @Getter
    private List<Ship> ships;
    @Getter
    private List<Integer> testedFields;

    public Field() {
        this.ships = new ArrayList<Ship>();
        this.testedFields = new ArrayList<Integer>();
    }

    /**
     * This function returns the ships size.
     * @return the size of the ships.
     */
    public int getNextSize() {
        System.out.println("ShipSize");
        switch (this.ships.size()) {
            case 0:
                return 5;
            case 1:
                return 4;
            case 2:
            case 3:
                return 3;
            case 4:
                return 2;
        }
        return 0;
    }

    /**
     * This function returns the ships display name.
     * @return the ships display name.
     */
    public String getNextShipName() {
        System.out.println("Get ship name");
        switch (this.ships.size()) {
            case 0:
                return "Carrier";
            case 1:
                return "Battleship";
            case 2:
                return "Destroyer";
            case 3:
                return "Submarine";
            case 4:
                return "Patrol Boat";
        }
        return "Unknown";
    }

       /**
     *This function returns the ships
     * @param x coordinate of the sip
     * @param y coordinate of the sip
     * @param side position of the ship
     * @return a ship to the game
     */
    
    public boolean addShip(int x, int y, boolean side) {
        System.out.println("Add ship");
        if (this.getNextSize() == 0)
            return false;

        if (side) {
            if (x > 10 - this.getNextSize())
                return false;
        } else {
            if (y > 10 - this.getNextSize())
                return false;
        }

        Ship newShip = new Ship(x, y, side ? this.getNextSize() : 1, side ? 1 : this.getNextSize());

        for (Ship s : this.ships) {
            if (newShip.isOverlap(s, 1)) {
                return false;
            }
        }

        this.ships.add(newShip);

        return true;
    }
    
      /**
     * This function is the game try-guess mechanism
     * @param x is one of the coordinate of the guess
     * @param y is one of the coordinate of the guess
     * @return the game try-guess mechanism
     */

    public boolean tryGuess(int x, int y) {
        System.out.println("Try");
        if (!this.testedFields.contains(y * 10 + x + 1))
            this.testedFields.add(y * 10 + x + 1);
        else
            return false;

        for (Ship s : this.ships) {
            if (s.isOverlap(new Ship(x, y, 1, 1), 0)) {
                int destroyed = 0;
                if (s.getWidth() == 1) {
                    destroyed = y - s.getY();
                } else {
                    destroyed = x - s.getX();
                }

                if (s.getDestroyed().contains(destroyed))
                    return false;

                s.getDestroyed().add(destroyed);
                return true;
            }
        }

        return false;
    }

     /**
     * This function check the game solve status
     * @return the game solve status
     */
    
    public boolean isSolved() {
       System.out.println("hit");
        for (Ship s : this.ships) {
            int size = 0;
            if (s.getWidth() == 1) {
                size = s.getHeight();
            } else {
                size = s.getWidth();
            }

            if (s.getDestroyed().size() != size)
                return false;
        }
        return true;
    }

}

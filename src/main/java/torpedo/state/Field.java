package torpedo.state;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Field {
    @Getter
    private List<Ship> ships;

    public Field() {
        this.ships = new ArrayList<Ship>();
    }

    public int getNextSize() {
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

    public String getNextShipName() {
        switch (getNextSize()) {
            case 5:
                return "Carrier";
            case 4:
                return "Battleship";
            case 3:
                return "Destroyer";
            case 2:
                return "Submarine";
            case 1:
                return "Patrol Boat";
        }
        return "Unknown";
    }

    public boolean addShip(int x, int y, boolean side) {
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

}

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

    public void addShip(int x, int y) {
        Ship s = new Ship(x, y, 1, 3);
        this.ships.add(s);
    }

}

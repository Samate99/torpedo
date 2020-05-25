package torpedo.state;

import lombok.Data;

@Data
public class Ship {
    private int x;
    private int y;
    private int width;
    private int height;

    private ShipState state;

    public Ship(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.state = ShipState.BLACK;
    }

    public boolean isOverlap (Ship ship, int space) {
        return x < ship.x + ship.width + space && x + width + space > ship.x && y < ship.y + ship.height + space && y + height + space > ship.y;
    }
}

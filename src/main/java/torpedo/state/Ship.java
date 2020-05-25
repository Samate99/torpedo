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
}

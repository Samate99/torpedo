package torpedo.state;

/**
 * Enum representing the four cardinal directions.
 */
public enum Direction {

    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

    private int dx;
    private int dy;

    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Returns the change in the x-coordinate when moving a step in this
     * direction.
     *
     * @return the change in the x-coordinate when moving a step in this
     * direction
     */
    public int getDx() {
        return dx;
    }

    /**
     * Returns the change in the y-coordinate when moving a step in this
     * direction.
     *
     * @return the change in the y-coordinate when moving a step in this
     * direction
     */
    public int getDy() {
        return dy;
    }

    /**
     * Returns the direction that corresponds to the changes in the x-coordinate
     * and the y-coordinate specified.
     *
     * @param dx the change in the x-coordinate
     * @param dy the change in the y-coordinate
     * @return the direction that corresponds to the changes in the x-coordinate
     * and the y-coordinate specified
     */
    public static Direction of(int dx, int dy) {
        for (Direction direction : values()) {
            if (direction.dx == dx && direction.dy == dy) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the opposite direction of this direction.
     *
     * @return the opposite direction of this direction
     */
    public Direction opposite() {
        switch (this) {
            case UP: return DOWN;
            case RIGHT: return LEFT;
            case DOWN:return UP;
            case LEFT: return RIGHT;
        }
        throw new AssertionError();
    }

}

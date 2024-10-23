public class PathAction extends Action {
    public enum Direction {UP, DOWN, LEFT, RIGHT}

    private final Direction direction;
    private final int distance;

    public PathAction(Direction direction, int distance) {
        this.direction = direction;
        this.distance = distance;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Moved " + distance + " spaces in direction: " + direction;
    }
}

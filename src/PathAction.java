public class PathAction extends Action {
    private final int deltaX; // Movement along the x-axis
    private final int deltaY; // Movement along the y-axis

    public PathAction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    @Override
    public String toString() {
        return "Move by (" + deltaX + ", " + deltaY + ")";
    }
}

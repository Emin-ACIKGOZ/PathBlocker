import java.util.ArrayList;
import java.util.List;

public class PathBlockerState extends State {

    private boolean[][] matrix; // True = wall or visited, False = empty space
    private int playerX, playerY; // Player's current position
    private int goalX, goalY; // Goal position
    private boolean goalReached;

    public PathBlockerState(boolean[][] matrix, int playerX, int playerY, int goalX, int goalY) {
        this.matrix = matrix;
        this.playerX = playerX;
        this.playerY = playerY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.goalReached = false;
    }

    private PathBlockerState(boolean[][] matrix, int playerX, int playerY, int goalX, int goalY, boolean goalReached) {
        this.matrix = matrix;
        this.playerX = playerX;
        this.playerY = playerY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.goalReached = goalReached;
    }

    @Override
    public boolean isGoal() {
        // Check if player has reached the goal
        return goalReached;
    }

    @Override
    public List<Action> getActionList() {
        // List of valid actions (move in four directions)
        List<Action> actions = new ArrayList<>();

        int moveUp = getMovementDistance(0, -1);
        if (moveUp > 0) actions.add(new PathAction(0, -moveUp));

        int moveDown = getMovementDistance(0, 1);
        if (moveDown > 0) actions.add(new PathAction(0, moveDown));

        int moveLeft = getMovementDistance(-1, 0);
        if (moveLeft > 0) actions.add(new PathAction(-moveLeft, 0));

        int moveRight = getMovementDistance(1, 0);
        if (moveRight > 0) actions.add(new PathAction(moveRight, 0));

        return actions;
    }

    private int getMovementDistance(int dx, int dy) {
        // Amount of distance travelled in one direction
        int distance = 0;
        int x = playerX;
        int y = playerY;

        // Move until the goal or an obstacle is encountered
        while (x + dx >= 0 && x + dx < matrix[0].length && y + dy >= 0 && y + dy < matrix.length
                && !matrix[y + dy][x + dx]) {
            x += dx;
            y += dy;
            distance++;
        }
        return distance;
    }

    @Override
    public State doAction(Action action) {
        if (action instanceof PathAction) {
            PathAction pathAction = (PathAction) action;
            return movePlayer(playerX + pathAction.getDeltaX(), playerY + pathAction.getDeltaY());
        }
        return this;
    }

    private PathBlockerState movePlayer(int newX, int newY) {
        boolean[][] newMatrix = cloneMatrix();
        // Mark current position as visited (true)
        newMatrix[playerY][playerX] = true;
        // Set new position
        boolean goalReached = newX == goalX && newY == goalY;
        return new PathBlockerState(newMatrix, newX, newY, goalX, goalY, goalReached);
    }

    private boolean[][] cloneMatrix() {
        boolean[][] clonedMatrix = new boolean[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, clonedMatrix[i], 0, matrix[i].length);
        }
        return clonedMatrix;
    }

    @Override
    public State undoAction(Action action) {
        if (action instanceof PathAction) {
            PathAction pathAction = (PathAction) action;
            return movePlayer(playerX - pathAction.getDeltaX(), playerY - pathAction.getDeltaY());
        }
        return this;
    }

    @Override
    public PathBlockerState clone() throws CloneNotSupportedException {
        // Return a deep copy of the state
        return new PathBlockerState(cloneMatrix(), playerX, playerY, goalX, goalY, goalReached);
    }
}
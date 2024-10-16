import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PathBlockerState extends State {

    private int[][] matrix; // 0 for empty space, 1 for walls, 2 for character position, 3 for goal
    private int playerX, playerY; // Player's current position
    private int goalX, goalY; // Goal position
    private boolean goalReached;

    public PathBlockerState(int[][] matrix, int playerX, int playerY, int goalX, int goalY) {
        this.matrix = matrix;
        this.playerX = playerX;
        this.playerY = playerY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.goalReached = false;
    }

    private PathBlockerState(int[][] matrix, int playerX, int playerY, int goalX, int goalY, boolean goalReached) {
        this.matrix = matrix;
        this.playerX = playerX;
        this.playerY = playerY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.goalReached = goalReached;
    }

    public PathBlockerState(String filePath) { // create initial state based on .txt files

        File file = new File(filePath);

        try {
            Scanner sc = new Scanner(file);

            List<List<Integer>> outer = new ArrayList<>(); // I used a 2d list because of the unknown dimensions of the .txt file

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                List<Integer> inner = new ArrayList<>();

                for (int i = 0; i < line.length(); i++) {
                    Integer temp = Character.getNumericValue(line.charAt(i));
                    inner.add(temp);
                }

                outer.add(inner);
            }

            int listRowSize = outer.size();
            int listColumnsSize = outer.getFirst().size();

            this.matrix = new int[listRowSize][listColumnsSize];

            for (int i = 0; i < this.matrix.length; i++) { // converting the list to static array

                List<Integer> row = outer.get(i);

                for (int j = 0; j < this.matrix[i].length; j++) {
                    this.matrix[i][j] = row.get(j);
                }
            }

            for (int i = 0; i < this.matrix.length; i++) {
                for (int j = 0; j < this.matrix[i].length; j++) {
                    if (this.matrix[i][j] == 2) {
                        this.playerY = i;
                        this.playerX = j;
                    } else if (this.matrix[i][j] == 3) {
                        this.goalY = i;
                        this.goalX = j;
                    }
                }
            }

            this.goalReached = false;

            //TESTING
            System.out.println("Matrix size = " + matrix.length +"x"+matrix[0].length);
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // we might change the exception handling logic later
        }

    }

    @Override
    public boolean isGoal() {
        // Check if player has reached the goal
        return goalReached;
    }

    @Override
    public List<Action> getActionList() {
        List<Action> actions = new ArrayList<>();

        int upDist = getDistance(PathAction.Direction.UP);
        if (upDist > 0) actions.add(new PathAction(PathAction.Direction.UP, upDist));

        int downDist = getDistance(PathAction.Direction.DOWN);
        if (downDist > 0) actions.add(new PathAction(PathAction.Direction.DOWN, downDist));

        int leftDist = getDistance(PathAction.Direction.LEFT);
        if (leftDist > 0) actions.add(new PathAction(PathAction.Direction.LEFT, leftDist));

        int rightDist = getDistance(PathAction.Direction.RIGHT);
        if (rightDist > 0) actions.add(new PathAction(PathAction.Direction.RIGHT, rightDist));

        return actions;
    }


    private int getDistance(PathAction.Direction direction) {
        int x = playerX;
        int y = playerY;
        int distance = 0;

        while (true) {
            switch (direction) {
                case UP:
                    if (y - 1 >= 0 && matrix[y - 1][x] !=1 ) y--;
                    else return distance;
                    break;
                case DOWN:
                    if (y + 1 < matrix.length && matrix[y + 1][x]!=1 ) y++;
                    else return distance;
                    break;
                case LEFT:
                    if (x - 1 >= 0 && matrix[y][x - 1] !=1 ) x--;
                    else return distance;
                    break;
                case RIGHT:
                    if (x + 1 < matrix[0].length && matrix[y][x + 1]!=1 ) x++;
                    else return distance;
                    break;
            }
            distance++;
            if (x == goalX && y == goalY) return distance;
        }
    }

    @Override
    public State doAction(Action action) {
        if (action instanceof PathAction pathAction) {
            return movePlayer(pathAction);
        }
        return this;
    }

    private PathBlockerState movePlayer(PathAction action) {
        int newX = playerX;
        int newY = playerY;
        int[][] newMatrix = cloneMatrix();

        for (int i = 0; i < action.getDistance(); i++) {
            // Update position based on direction
            switch (action.getDirection()) {
                case UP:
                    newY--;
                    break;
                case DOWN:
                    newY++;
                    break;
                case LEFT:
                    newX--;
                    break;
                case RIGHT:
                    newX++;
                    break;
            }

            // Mark current tile as visited (wall)
            newMatrix[playerY][playerX] = 1;

            // Check if goal is reached
            if (newX == goalX && newY == goalY) {
                newMatrix[playerY][playerX] = 2; //Update player location
                return new PathBlockerState(newMatrix, newX, newY, goalX, goalY, true);
            }

            playerX = newX;
            playerY = newY;
        }
        newMatrix[playerY][playerX] = 2; //Update player location
        return new PathBlockerState(newMatrix, newX, newY, goalX, goalY, false);
    }

    private int[][] cloneMatrix() {
        int[][] clonedMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, clonedMatrix[i], 0, matrix[i].length);
        }
        return clonedMatrix;
    }

    @Override
    public State undoAction(Action action) {
        if (action instanceof PathAction pathAction) {
            int newX = playerX;
            int newY = playerY;
            int[][] newMatrix = cloneMatrix();

            for (int i = 0; i < pathAction.getDistance(); i++) {
                // Unmark visited tile (make it empty again)
                newMatrix[playerY][playerX] = 0;

                // Move the player back
                switch (pathAction.getDirection()) {
                    case UP:
                        newY++;
                        break;
                    case DOWN:
                        newY--;
                        break;
                    case LEFT:
                        newX++;
                        break;
                    case RIGHT:
                        newX--;
                        break;
                }

                playerX = newX;
                playerY = newY;
            }

            newMatrix[playerY][playerX] = 2; //update player location
            return new PathBlockerState(newMatrix, newX, newY, goalX, goalY, false);
        }
        return this;
    }


    @Override
    public PathBlockerState clone() throws CloneNotSupportedException {
        //Returns a deep copy of the state
        return new PathBlockerState(cloneMatrix(), playerX, playerY, goalX, goalY, goalReached);
    }

    public void printMatrix() {
        for (int[] row : this.matrix) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PathBlockerState extends State {

    private int[][] matrix; // 0 for empty space, 1 for walls, 2 for character position, 3 for goal
    public int playerX, playerY; // Player's current position
    public int goalX, goalY; // Goal position
    private boolean goalReached;

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
                int lineLength = line.length();
                List<Integer> inner = new ArrayList<>();

                for (int i = 0; i < lineLength; i++) {

                    if (line.charAt(i) != '0' && line.charAt(i) != '1' && line.charAt(i) != '2' && line.charAt(i) != '3')
                        continue; // skips any character that shouldn't be present

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
            System.out.println("Matrix size: " + matrix.length + "x" + matrix[0].length);
        } catch (FileNotFoundException e) {
            // Create default matrix if the file is not found
            this.matrix = new int[][]{{2, 1, 3}};
            this.playerX = 0;
            this.playerY = 0;
            this.goalX = 2;
            this.goalY = 0;
            this.goalReached = false;

            System.err.println("File not found. Default PathBlockerState created.");
            System.err.println(e.getMessage());
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
                    if (y - 1 >= 0 && matrix[y - 1][x] != 1) y--;
                    else return distance;
                    break;
                case DOWN:
                    if (y + 1 < matrix.length && matrix[y + 1][x] != 1) y++;
                    else return distance;
                    break;
                case LEFT:
                    if (x - 1 >= 0 && matrix[y][x - 1] != 1) x--;
                    else return distance;
                    break;
                case RIGHT:
                    if (x + 1 < matrix[0].length && matrix[y][x + 1] != 1) x++;
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

        int distance = action.getDistance(); // moved function call outside loop
        for (int i = 0; i < distance; i++) {

            // Mark current tile as visited (wall)
            matrix[playerY][playerX] = 1;

            // Update position based on direction
            switch (action.getDirection()) {
                case UP:
                    playerY--;
                    break;
                case DOWN:
                    playerY++;
                    break;
                case LEFT:
                    playerX--;
                    break;
                case RIGHT:
                    playerX++;
                    break;
            }

            // Check if goal is reached
            if (playerX == goalX && playerY == goalY) {
                matrix[playerY][playerX] = 2; //Update player location
                this.goalReached = true;
                return this;
            }
        }
        matrix[playerY][playerX] = 2; //Update player location
        return this; //check if properly false?
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

            int distance = pathAction.getDistance(); // moved function call outside loop
            for (int i = 0; i < distance; i++) {

                // Unmark visited tile (make it empty again)
                matrix[playerY][playerX] = 0;

                // Move the player back
                switch (pathAction.getDirection()) {
                    case UP:
                        playerY++;
                        break;
                    case DOWN:
                        playerY--;
                        break;
                    case LEFT:
                        playerX++;
                        break;
                    case RIGHT:
                        playerX--;
                        break;
                }

            }

            matrix[playerY][playerX] = 2; //update player location

            return this;
        }
        return this;
    }


    @Override
    public PathBlockerState clone() throws CloneNotSupportedException {
        //Returns a deep copy of the state
        return new PathBlockerState(cloneMatrix(), playerX, playerY, goalX, goalY, goalReached);
    }

    @Override
    public int getDepthLimit() {
        long sum = (long) matrix.length + (long) matrix[0].length;
        if (sum > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return (int) sum;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : this.matrix) {
            for (int cell : row) {
                String cellType = switch (cell) {
                    case 0 -> " ";
                    case 1 -> "X";
                    case 2 -> "P";
                    case 3 -> "G";
                    default -> "?";
                };

                sb.append(cellType).append(" ");
            }
            sb.append("\n"); // Add a newline after each row
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Arrays.deepHashCode(matrix);
        result = 31 * result + playerX;
        result = 31 * result + playerY;
        result = 31 * result + goalX;
        result = 31 * result + goalY;
        result = 31 * result + Boolean.hashCode(goalReached);
        return result;
    }


}

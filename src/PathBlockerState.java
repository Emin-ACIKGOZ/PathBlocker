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

    public PathBlockerState(String filePath){ // create initial state based on .txt files

        File file = new File(filePath);

        try {
            Scanner sc = new Scanner(file);

            List<List<Integer>> outer = new ArrayList(); // i used 2d list because of the unknown dimensions of the .txt file

            while (sc.hasNextLine()){
                String line = sc.nextLine();

                List<Integer> inner = new ArrayList();

                for (int i = 0;i<line.length();i++){
                    Integer temp = Character.getNumericValue(line.charAt(i));
                    inner.add(temp);
                }

                outer.add(inner);
            }

            int listRowSize = outer.size();
            int listColumnsSize = outer.get(0).size();

            this.matrix = new int[listRowSize][listColumnsSize];

            for (int i = 0;i< this.matrix.length;i++){ // converting the list to static array

                List<Integer> row = outer.get(i);

                for (int j = 0;j< this.matrix[i].length;j++){
                    this.matrix[i][j] = row.get(j);
                }
            }

            for (int i = 0;i< this.matrix.length;i++){
                for (int j = 0;j< this.matrix[i].length;j++){
                    if (this.matrix[i][j] == 2){
                        this.playerY = i;
                        this.playerX = j;
                    }
                    else if (this.matrix[i][j] == 3){
                        this.goalY = i;
                        this.goalX = j;
                    }
                }
            }

            this.goalReached = false;

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
        //Amount of distance travelled in one direction
        int distance = 0;
        int x = playerX;
        int y = playerY;

        //Move until the goal or an obstacle is encountered
        while (x + dx >= 0 && x + dx < matrix[0].length && y + dy >= 0 && y + dy < matrix.length
                && matrix[y + dy][x + dx] == 0) {
            x += dx;
            y += dy;
            distance++;
        }
        return distance;

        // note from ugur: since i have changed the
        // logic of boolean matrix to int, i assumed that the character
        // should keep moving until the road is not empty (0). But this logic might be wrong i am not so sure.
        // so we might need to adjust the last condition on the loop.
    }

    @Override
    public State doAction(Action action) {
        if (action instanceof PathAction) {
            PathAction pathAction = (PathAction) action;
            return movePlayer(playerX + pathAction.getDeltaX(), playerY + pathAction.getDeltaY());
        }
        return this;
    }

    private PathBlockerState movePlayer(Direction direction) {
        //NEEDS TO BE IMPLEMENTED PROPERLY
        // boolean[][] newMatrix = cloneMatrix();
        // Mark current position as visited (true)
        // newMatrix[playerY][playerX] = true;
        // Set new position
        // return new PathBlockerState(newMatrix, newX, newY, goalX, goalY, );
        //If the current location of the player matches the goal's location, stop moving and set goalReached to true
//        if(x == goalX && y == goalY){
//            goalReached = true;
//            return distance;
//        }

        int[][] newMatrix = this.cloneMatrix();
        int newPlayerX = playerX;
        int newPlayerY = playerY;
        boolean newGoalReached = false;

        switch(direction){

            case Direction.RIGHT:

                while (newPlayerX < matrix[0].length-1 && matrix[newPlayerY][newPlayerX+1] != 1){ // stop if you are at the bound or your next cell is a wall

                    if (newPlayerX == goalX && newPlayerY == goalY){ // you reached the goal

                        newGoalReached = true;

                        return new PathBlockerState(newMatrix, newPlayerX, newPlayerY, goalX, goalY, newGoalReached);

                    }

                    matrix[newPlayerY][newPlayerX+1] = 2;
                    matrix[newPlayerY][newPlayerX] = 1;

                    newPlayerX++;

                }
                break;

            case Direction.DOWN:

                while (newPlayerY < matrix.length-1 && matrix[newPlayerY+1][newPlayerX] != 1){ // stop if you are at the bound or your next cell is a wall

                    if (newPlayerX == goalX && newPlayerY == goalY){ // you reached the goal

                        newGoalReached = true;

                        return new PathBlockerState(newMatrix, newPlayerX, newPlayerY, goalX, goalY, newGoalReached);

                    }

                    matrix[newPlayerY+1][newPlayerX] = 2;
                    matrix[newPlayerY][newPlayerX] = 1;

                    newPlayerY++;

                }
                break;

            case Direction.LEFT:

                while (newPlayerX > 0 && matrix[newPlayerY][newPlayerX-1] != 1){ // stop if you are at the bound or your next cell is a wall

                    if (newPlayerX == goalX && newPlayerY == goalY){ // you reached the goal

                        newGoalReached = true;

                        return new PathBlockerState(newMatrix, newPlayerX, newPlayerY, goalX, goalY, newGoalReached);

                    }

                    matrix[newPlayerY][newPlayerX-1] = 2;
                    matrix[newPlayerY][newPlayerX] = 1;

                    newPlayerX--;

                }
                break;

            case Direction.UP:

                while (newPlayerY > 0 && matrix[newPlayerY-1][newPlayerX] != 1){ // stop if you are at the bound or your next cell is a wall

                    if (newPlayerX == goalX && newPlayerY == goalY){ // you reached the goal

                        newGoalReached = true;

                        return new PathBlockerState(newMatrix, newPlayerX, newPlayerY, goalX, goalY, newGoalReached);

                    }

                    matrix[newPlayerY-1][newPlayerX] = 2;
                    matrix[newPlayerY][newPlayerX] = 1;

                    newPlayerY--;

                }
                break;
        }

        return new PathBlockerState(newMatrix, newPlayerX, newPlayerY, goalX, goalY, newGoalReached);
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
        if (action instanceof PathAction) {
            //needs to be implemented!
        }
        return this;
    }

    @Override
    public PathBlockerState clone() throws CloneNotSupportedException {
        // Return a deep copy of the state
        return new PathBlockerState(cloneMatrix(), playerX, playerY, goalX, goalY, goalReached);
    }

    public void printMatrix(){
        for (int i = 0;i< this.matrix.length;i++){
            for (int j = 0;j< this.matrix[i].length;j++){
                System.out.print(this.matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}

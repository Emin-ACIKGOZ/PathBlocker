import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        // Error: when empty new lines at the top or bottom, enemy space characters at the start of the line
        // Error: Does not detect the goal

        //testFromOneEndOfPossibleActions("lvl01.txt",'l');

        // tries each possible action and prints each possible state
        //testEachDirection("teyzen.txt");

        PathBlockerState initialState = new PathBlockerState("lvl01.txt");
        System.out.println("Initial State");
        System.out.println("-------------");
        initialState.printMatrix();

        long startTime = System.nanoTime();
        Solver solver =
                //new BFSTreeSolver(31);     // 0.003-0.005
                //new BFSGraphSolver(31); // 0.005
                //new DFSTreeSolver(31);
                //new DFSGraphSolver(31);
                new DFSTreeMemEfficientSolver(31);
                //new IterativeDeepeningSolver(31);
                //new IterativeDeepeningRecursiveSolver(31);
                //new IterativeDeepeningMemEfficientSolver(100);

        try {
            List<State> solution = solver.solve(initialState);
            long endTime = System.nanoTime();

            if (solution == null || solution.size() == 0) {
                System.out.println("No solution is found!");
            }
            else {
                System.out.println("Solution step count : " + (solution.size() - 1));
                for (int i = 0; i < solution.size(); i++) {
                    PathBlockerState pathBlockerState = (PathBlockerState) (solution.get(i));
                    pathBlockerState.printMatrix();

                    if (i == 0)
                        System.out.println("  --> Initial state\n");
                    else
                        System.out.println("  --> Step " + i + "\n");
                }

                System.out.println();
                System.out.println("Maximum frontier size : " + solver.getMaximumFrontierSize());
                System.out.println("Current frontier size : " + solver.getFrontierSize());
                System.out.println("Current explored size : " + solver.getExploredSize());
                System.out.println("Visited node count : " + solver.getVisitedCount());

                System.out.println();
                System.out.println("Maximum explored depth : " + solver.getMaximumExploredDepth());
                System.out.println("Time as seconds: " + (endTime - startTime) / 1000000000.0 );

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public static void testFromOneEndOfPossibleActions(String filePath, char fromWhere){
        //WITH BORDER
        PathBlockerState pbs = new PathBlockerState(filePath);
        pbs.printMatrix();
        System.out.println();

        int step = 1;
        PathBlockerState currentPbs = pbs;

        while (!currentPbs.isGoal()) {  // Continue until the goal is reached
            System.out.println("Step " + step + ":");

            List<Action> actions = currentPbs.getActionList();

            if (actions.isEmpty()) {
                System.out.println("No more actions available. Unable to reach the goal.");
                break;  // Exit if no further actions can be performed
            }

            if(fromWhere == 's'){
                currentPbs = (PathBlockerState) currentPbs.doAction(actions.getFirst());
            }
            else if(fromWhere == 'l'){
                currentPbs = (PathBlockerState) currentPbs.doAction(actions.getLast());
            }

            currentPbs.printMatrix();
            System.out.println();

            step++;
        }

        if (currentPbs.isGoal()) {
            System.out.println("Goal reached!");
            System.out.println("goal X: " + currentPbs.goalX + " goal Y: "+ currentPbs.goalY );
            System.out.println("player X: " + currentPbs.playerX + " player Y: "+ currentPbs.playerY );
        } else {
            System.out.println("Goal not reached.");
        }

//        PathBlockerState pbs = new PathBlockerState("lvl01.txt");
//        pbs.printMatrix();
//        System.out.println();
//        System.out.println("1:");
//        List<Action> actions1 = pbs.getActionList();
//        PathBlockerState pbs2 = (PathBlockerState) pbs.doAction(actions1.get(0));
//        pbs2.printMatrix();
//        System.out.println();
//
//        System.out.println("2:");
//        List<Action> actions2 = pbs2.getActionList();
//        PathBlockerState pbs3 = (PathBlockerState) pbs2.doAction(actions2.get(0));
//        pbs3.printMatrix();
//        System.out.println();
    }

    // tries each possible action and prints each possible state one by one
    public static void testEachDirection(String txtPath){
        PathBlockerState initialPbs = new PathBlockerState(txtPath);
        initialPbs.printMatrix();
        System.out.println();

        Queue<PathBlockerState> statesQueue = new LinkedList<>();
        statesQueue.add(initialPbs);

        int step = 1;

        while (!statesQueue.isEmpty()) {  // Continue until no states remain to process
            PathBlockerState currentPbs = statesQueue.poll();  // Get the next state from the queue
            if(currentPbs.isGoal()){
                currentPbs.printMatrix();
                System.out.println("success!!!!!!");
                break;
            }
            System.out.println("Step " + step + ":");

            // Get the list of actions available from the current state
            List<Action> actions = currentPbs.getActionList();

            // Print the number of actions available
            System.out.println("Available actions: " + actions.size());
            if (actions.isEmpty()) {
                System.out.println("No more actions available for this state.");
                continue;  // Move to the next state in the queue
            }

            // Try all actions in the actions list
            for (Action action : actions) {
                System.out.println("Attempting action: " + action);  // Print the current action being tried

                // Clone the current state before applying the action
                PathBlockerState clonedPbs = null;
                try {
                    clonedPbs = (PathBlockerState) currentPbs.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }

                if(clonedPbs.isGoal()){
                    currentPbs.printMatrix();
                    System.out.println("success!!!!!!");
                    break;
                }

                // Apply the action to the cloned state
                PathBlockerState newPbs = (PathBlockerState) clonedPbs.doAction(action);

                // If the action results in a valid new state
                if (newPbs != null) {
                    System.out.println("Action resulted in a new state:");
                    newPbs.printMatrix();  // Print the new state after applying the action
                    System.out.println();

                    // Add the new state to the queue to continue exploring its actions
                    statesQueue.add(newPbs);
                } else {
                    System.out.println("Action resulted in an invalid state.");
                }
            }

            // Increment the step for the next loop iteration
            step++;
        }

        System.out.println("No more states to process.");
    }

}
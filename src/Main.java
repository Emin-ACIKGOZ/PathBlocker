import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {

        solveAllLevels(10);


        // NOTE TO UGUR FROM AZIZ: otuzbir COMMENTED ALL PREVIOUS MAIN

//        PathBlockerState initialState = new PathBlockerState("lvl01.txt");
////System.out.println("Initial State");
////System.out.println("-------------");
////System.out.println(initialState);
//
//        String textToBeSavedAsPng = "";
//        StringBuilder pngName = new StringBuilder("level01/0001.png"); // i will later change the string according to levels
//
//        long startTime = System.nanoTime();
//        Solver solver =
//                //new BFSTreeSolver(31);                                        // 0.003-0.005
//                //new BFSGraphSolver(31);                               // 0.005
//                //new DFSTreeSolver(31);
//                //new DFSGraphSolver(31);
//                //new DFSTreeMemEfficientSolver(31);
//                //new IterativeDeepeningSolver(31);
//                new IterativeDeepeningRecursiveSolver(31);
//                //new IterativeDeepeningMemEfficientSolver(31);
//
//        try {
//            List<State> solution = solver.solve(initialState);
//            long endTime = System.nanoTime();
//            int solutionSize = solution.size();
//            if (solution == null || solutionSize == 0) {
//
//                String temp = "No solution is found!";
//                System.out.println("temp");
//
//                textToBeSavedAsPng += temp;
//                PngConverter.saveStringAsImage(textToBeSavedAsPng,pngName.toString(),1920,18);
//            }
//            else {
//                System.out.println("Solution step count : " + (solutionSize - 1));
//
//                textToBeSavedAsPng += initialState.toString();
//                PngConverter.saveStringAsImage(textToBeSavedAsPng,pngName.toString(),1920,18);
//
//                // note from ugur to aziz: we might need to
//                // add the initialState to solution sequence
//                // but i am not really sure so for now,
//                // i am converting initial state independently
//
//                for (int i = 0; i < solutionSize; i++) {
//
//                    textToBeSavedAsPng = "";
//
//                    PathBlockerState pathBlockerState = (PathBlockerState) (solution.get(i));
////System.out.println(pathBlockerState);
//
//                    textToBeSavedAsPng += pathBlockerState.toString();
//
//                    if (i == 0){
////System.out.println("  --> Initial state\n");
//                    }
//                    else{
////System.out.println("  --> Step " + i + "\n");
//
//                        int pngIndex = i+1;
//
//                        if (pngIndex<10){
//                            pngName.replace(11,12,(pngIndex+""));
//                        }
//
//                        else{
//                            pngName.replace(10,12,(pngIndex+""));
//                        }
//
//                    }
//                    PngConverter.saveStringAsImage(textToBeSavedAsPng,pngName.toString(),360,18);
//                }
//
//                System.out.println();
//                System.out.println("Maximum frontier size : " + solver.getMaximumFrontierSize());
//                System.out.println("Current frontier size : " + solver.getFrontierSize());
//                System.out.println("Current explored size : " + solver.getExploredSize());
//                System.out.println("Visited node count : " + solver.getVisitedCount());
//
//                System.out.println();
//                System.out.println("Maximum explored depth : " + solver.getMaximumExploredDepth());
//                System.out.println("Time as seconds: " + (endTime - startTime) / 1000000000.0 );
//
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }


    public static void solveAllLevels(int totalLevels) {
        for (int level = 1; level <= totalLevels; level++) {
            String levelFileName = String.format("lvl%02d.txt", level); // lvl01.txt, lvl02.txt, ...
            PathBlockerState initialState = new PathBlockerState(levelFileName);
            System.out.println("Solving level: " + levelFileName);

            String directoryPath = String.format("level%02d", level);

            Path path = Paths.get(directoryPath);

            if (!Files.exists(path)){ // if directory doesn't exist
                System.out.println(directoryPath + "\\ directory doesn't exist. It will be created manually.");
                try {
                    Files.createDirectories(path); // create the directory manually
                    System.out.println("Directory created successfully.");
                } catch (Exception e) {
                    System.out.println("Failed to create directory: " + e.getMessage());
                }
            }

            StringBuilder pngName = new StringBuilder(String.format("level%02d/0001.png", level));
            String textToBeSavedAsPng = "";

            // Solver array with all solvers
            Solver[] solvers = {
                    new BFSTreeSolver(100000),
                    //new BFSGraphSolver(31),
                    //new DFSTreeSolver(31),
                    //new DFSGraphSolver(31),
                    //new DFSTreeMemEfficientSolver(31),
                    //new IterativeDeepeningSolver(31),
                    //new IterativeDeepeningRecursiveSolver(31),
                    //new IterativeDeepeningMemEfficientSolver(31)
            };

            for (Solver solver : solvers) {
                System.out.println("Using solver: " + solver.getClass().getSimpleName());

                System.gc();
                long startTime = System.nanoTime();

                try {
                    List<State> solution = solver.solve(initialState);
                    long endTime = System.nanoTime();
                    int solutionSize = (solution != null) ? solution.size() : 0;

                    if (solution == null || solutionSize == 0) {
                        String temp = "No solution is found!";
                        System.out.println(temp);
                        textToBeSavedAsPng = temp;
                        PngConverter.saveStringAsImage(textToBeSavedAsPng, pngName.toString(), 1920, 18);
                    } else {
                        System.out.println("Solution step count: " + (solutionSize - 1));
                        textToBeSavedAsPng = initialState.toString();
                        PngConverter.saveStringAsImage(textToBeSavedAsPng, pngName.toString(), 1920, 18);

                        for (int i = 0; i < solutionSize; i++) {
                            textToBeSavedAsPng = "";
                            PathBlockerState pathBlockerState = (PathBlockerState) solution.get(i);
                            textToBeSavedAsPng += pathBlockerState.toString();

                            if (i > 0) {
                                int pngIndex = i + 1;
                                if (pngIndex < 10) {
                                    pngName.replace(11, 12, Integer.toString(pngIndex));
                                } else {
                                    pngName.replace(10, 12, Integer.toString(pngIndex));
                                }
                            }
                            PngConverter.saveStringAsImage(textToBeSavedAsPng, pngName.toString(), 360, 18);
                        }

                        // Performance metrics
                        System.out.println("Maximum frontier size: " + solver.getMaximumFrontierSize());
                        System.out.println("Current frontier size: " + solver.getFrontierSize());
                        System.out.println("Current explored size: " + solver.getExploredSize());
                        System.out.println("Visited node count: " + solver.getVisitedCount());
                        System.out.println("Maximum explored depth: " + solver.getMaximumExploredDepth());
                        System.out.println("Time as seconds: " + (endTime - startTime) / 1_000_000_000.0);
                        System.out.println();
                    }
                } catch (Exception e) {
                    System.out.println("Error during solving with solver " + solver.getClass().getSimpleName());
                    e.printStackTrace();
                }
                System.out.println("------------------------------------");
            }
            System.out.println("=============================================================================");
        }
    }



    public static void testFromOneEndOfPossibleActions(String filePath, char fromWhere){
        //WITH BORDER
        PathBlockerState pbs = new PathBlockerState(filePath);
        System.out.println(pbs);
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

            System.out.println(currentPbs);
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

    }

    // tries each possible action and prints each possible state one by one
    public static void testEachDirection(String txtPath){
        PathBlockerState initialPbs = new PathBlockerState(txtPath);
        System.out.println(initialPbs);
        System.out.println();

        Queue<PathBlockerState> statesQueue = new LinkedList<>();
        statesQueue.add(initialPbs);

        int step = 1;

        while (!statesQueue.isEmpty()) {  // Continue until no states remain to process
            PathBlockerState currentPbs = statesQueue.poll();  // Get the next state from the queue
            if(currentPbs.isGoal()){
                System.out.println(currentPbs);
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
                    System.out.println(currentPbs);
                    System.out.println("success!!!!!!");
                    break;
                }

                // Apply the action to the cloned state
                PathBlockerState newPbs = (PathBlockerState) clonedPbs.doAction(action);

                // If the action results in a valid new state
                if (newPbs != null) {
                    System.out.println("Action resulted in a new state:");
                    System.out.println(newPbs);  // Print the new state after applying the action
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/*
Project Questions:

1) Why Do You Prefer The Search Algorithm You Chose (DFS Tree Version)?

We chose the DFS Tree version because it’s simple, fast, and memory efficient.
DFS explores one path deeply before backtracking,
which saves memory since we only need to keep the current path in the stack,
unlike BFS, which stores all nodes on the same level.

Additionally, DFS works better for small problems because it can find a solution quickly without exploring all paths.
However, if the problem size increases significantly,
DFS may become inefficient due to the risk of missing optimal solutions or getting stuck in long paths.
In such cases, we would consider switching to a different algorithm,
like A*, to ensure better performance and optimal solutions.

------------------------------------------------------------------------------------------------------------------------
2) Can You Achieve The Optimal Result? Why? Why not?

DFS does not guarantee an optimal result as DFS doesn't systematically search all possible paths at the same depth,
so it might miss the optimal (shortest) path. If a solution is found at a much lower layer first, it will be returned.
It may not be the shortest path as we do not check each layer for a possible solution like BFS would.
However, it’s still useful when any valid solution is an acceptable solution.

------------------------------------------------------------------------------------------------------------------------
3) How Did You Achieve Efficiency In Keeping The States?

We achieved memory efficiency by only keeping the current path in the frontier stack.
In DFS, we don’t store all the explored nodes like in BFS, which saves memory.
Once a path reaches a dead-end, DFS simply backtracks and discards those nodes from the stack,
leaving only the nodes on the current path remaining in memory.
Also, since cycles do not occur in this game, we don't need an explored set to track visited nodes.
This makes the algorithm more efficient since we save memory by not needing to keep track of visited nodes.

------------------------------------------------------------------------------------------------------------------------
4) If You Prefer To Use DFS (Tree Ver.), Do You Need to Avoid Cycles?

We do not need to avoid cycles in our algorithm for this problem.
The structure of the problem does not have cycles
as each move will limit the number of possible future moves we can make
until we either reach the goal or we hit a dead end.
It’s not possible to obtain an earlier state after making moves,
as making a move makes it impossible to traverse through the same path.
Additionally, our implementation of DFS has a depth limit to prevent it from being trapped in a cycle.
Using DFS with a tree structure for this problem is intuitive and efficient.

For larger matrices we would likely implement an algorithm such as A* that mitigates cycles by avoiding expensive paths.
The design of our project allows us to choose the appropriate algorithm for the problem we are solving.

------------------------------------------------------------------------------------------------------------------------
5) What Would Be The Path-Cost For This Problem?

The path-cost for this problem would be the total cost of all the steps taken from the start to the goal.
We define a step as a full move in a direction,
and we consider each move to have the same cost as they each use one turn to execute.
If each step has a cost of one, the total path-cost will simply be the number of steps taken to reach the goal.

Example: If the solution path contains 10 steps, the total path-cost is 10.

If different actions were to have different costs,
the total path-cost would be the sum of the individual costs for each step along the solution path.
An example of this would be a solution that demands the smallest distance (spaces in the matrix) traveled.

Example: 1st step 5 units up, 2nd step 3 units left. Total path-cost is 8.

DFS is not guaranteed to return the optimal path.
If the problem demands the shortest path or an optimal path-cost,
a more suitable algorithm, such as A*, would be used instead.
But for smaller problems similar to this one, DFS shows better performance compared to other algorithms.
*/

public class Main {
    public static void main(String[] args) {
        solveAllLevels(10);
    }

    public static void solveAllLevels(int numberOfLevels) {
        double totalTime = 0;  // To accumulate total time across levels
        int solvedLevels = 0;  // To track how many levels were solved

        for (int level = 1; level <= numberOfLevels; level++) {
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
                    //new BFSTreeSolver(100000),
                    //new BFSGraphSolver(31),
                    //new DFSTreeSolver(31),
                    //new DFSGraphSolver(31),
                    new DFSTreeMemEfficientSolver(31),
                    //new IterativeDeepeningSolver(31),
                    //new IterativeDeepeningRecursiveSolver(31),
                    //new IterativeDeepeningMemEfficientSolver(31)
            };

            for (Solver solver : solvers) {
                System.out.println("Using solver: " + solver.getClass().getSimpleName());

                long startTime = System.nanoTime();

                try {
                    List<State> solution = solver.solve(initialState);
                    long endTime = System.nanoTime();
                    double timeInMillis = (endTime - startTime) / 1_000_000.0;

                    int solutionSize = (solution != null) ? solution.size() : 0;

                    if (solution == null || solutionSize == 0) {
                        String temp = "No solution is found!";
                        System.out.println(temp);
                        textToBeSavedAsPng = temp;
                        PngConverter.saveStringAsImage(textToBeSavedAsPng, pngName.toString(), 360, 18);
                    } else {
                        System.out.println("Solution step count: " + (solutionSize - 1));
                        textToBeSavedAsPng = initialState.toString();
                        PngConverter.saveStringAsImage(textToBeSavedAsPng, pngName.toString(), 360, 18);
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
                        System.out.println("Time in milliseconds: " + timeInMillis);
                        System.out.println();

                        // Accumulate total time and count solved levels
                        totalTime += timeInMillis;
                        solvedLevels++;
                    }
                } catch (Exception e) {
                    System.err.println("Error during solving with solver " + solver.getClass().getSimpleName());
                    System.err.println(e.getMessage());
                }
            }
            System.out.println("=============================================================================");
        }

        // Print total and average time at the end
        System.out.println("Total time for all levels: " + String.format("%.3f", totalTime) + " milliseconds");
        if (solvedLevels > 0) {
            double averageTime = totalTime / solvedLevels;
            System.out.println("Average time per solved level: " + String.format("%.3f", averageTime) + " milliseconds");
        }
    }
}
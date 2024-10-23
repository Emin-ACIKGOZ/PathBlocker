import java.util.ArrayList;
import java.util.List;


    public class IterativeDeepeningMemEfficientSolver extends Solver {
        private int maximumExploredDepth;

        IterativeDeepeningMemEfficientSolver(int depthLimit)
        {
            super(depthLimit);
        }

        @Override
        List<State> solve(State initialState) throws Exception
        {
            visitedCount = 0;
            maximumExploredDepth = 0;

            for (int currentDepthLimit = 1; currentDepthLimit <= depthLimit; currentDepthLimit++) {

                List<State> solution = new ArrayList<State>();
                if (recursiveSolver(initialState, 0, currentDepthLimit, solution)) {
                    solution.add(0, initialState);
                    return solution;
                }
            }

            // no solution is found!
            return null;
        }

        boolean recursiveSolver(State state, int depth, int currentDepthLimit, List<State> solution) throws Exception
        {
            visitedCount++;
            maximumExploredDepth = Math.max(maximumExploredDepth, depth);

            // check if the node is a goal state
            if (state.isGoal()) {
                return true;
            }

            if (depth < currentDepthLimit) {
                // expand the chosen node, adding the resulting nodes to the frontier
                for(Action action : state.getActionList()) {
                    state.doAction(action);
                    boolean goalFound = recursiveSolver(state, depth + 1, currentDepthLimit, solution);
                    if (goalFound) {
                        solution.add(0, state.clone());
                    }
                    state.undoAction(action);

                    if (goalFound) {
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        int getMaximumFrontierSize()
        {
            return 1;
        }

        @Override
        int getFrontierSize()
        {
            return 1;
        }

        @Override
        int getExploredSize()
        {
            return 0;
        }

        @Override
        int getMaximumExploredDepth()
        {
            return maximumExploredDepth + 1;
        }

    }


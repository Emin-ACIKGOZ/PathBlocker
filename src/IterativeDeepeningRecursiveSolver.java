import java.util.*;

public class IterativeDeepeningRecursiveSolver extends Solver {
    private int maximumExploredDepth;

    IterativeDeepeningRecursiveSolver(int depthLimit)
    {
        super(depthLimit);
    }

    @Override
    List<State> solve(State initialState) throws Exception
    {
        visitedCount = 0;
        maximumExploredDepth = 0;

        for (int currentDepthLimit = 1; currentDepthLimit <= depthLimit; currentDepthLimit++) {
            Node solutionNode = recursiveSolver(new Node(initialState), currentDepthLimit);

            if (solutionNode != null && solutionNode.state.isGoal()) {
                return createSolutionSequence(solutionNode);
            }
        }

        // no solution is found!
        return null;
    }

    Node recursiveSolver(Node node, int currentDepthLimit) throws Exception
    {
        visitedCount++;
        maximumExploredDepth = Math.max(maximumExploredDepth, node.depth);

        // check if the node is a goal state
        if (node.state.isGoal()) {
            return node;
        }

        if (node.depth < currentDepthLimit) {
            // expand the chosen node, adding the resulting nodes to the frontier
            for(Action action : node.state.getActionList()) {
                State newState = node.state.clone().doAction(action);
                Node newNode = new Node(newState, action, node);

                Node resultNode = recursiveSolver(newNode, currentDepthLimit);
                if (resultNode != null && resultNode.state.isGoal()) {
                    return resultNode;
                }
            }
        }

        return null;
    }

    @Override
    int getMaximumFrontierSize()
    {
        return maximumExploredDepth;
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

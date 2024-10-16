import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSTreeSolver extends Solver{
    private Queue<Node> frontier = new LinkedList<Node>();			// open set (fringe)

    private int maximumFrontierSize;
    private int maximumExploredDepth;

    BFSTreeSolver(int depthLimit)
    {
        super(depthLimit);
    }

    @Override
    List<State> solve(State initialState) throws Exception
    {
        maximumFrontierSize = 0;
        maximumExploredDepth = 0;

        // initialize the frontier using the initial state of problem
        frontier.clear();
        frontier.add(new Node(initialState));
        visitedCount = 1;

        // exploring with LIFO strategy
        while (frontier.size() > 0) {
            // choose a leaf node and remove it from the frontier
            Node node = frontier.poll();
            maximumExploredDepth = Math.max(maximumExploredDepth,  node.depth);

            // check if the node is a goal state
            if (node.state.isGoal()) {
                return createSolutionSequence(node);
            }

            if (node.depth < depthLimit) {
                // expand the chosen node, adding the resulting nodes to the frontier
                for(Action action : node.state.getActionList()) {
                    visitedCount++;

                    State newState = node.state.clone().doAction(action);
                    Node newNode = new Node(newState, action, node);
                    if (newNode.state.isGoal()) {
                        return createSolutionSequence(newNode);
                    }

                    // add only if not in the frontier or explored set
                    if (!frontier.contains(newNode)) {
                        frontier.add(new Node(newState, action, node));
                    }
                }
                maximumFrontierSize = Math.max(maximumFrontierSize, frontier.size());
            }
        }

        // no solution is found!
        return null;
    }

    @Override
    int getMaximumFrontierSize()
    {
        return maximumFrontierSize;
    }

    @Override
    int getFrontierSize()
    {
        return frontier.size();
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

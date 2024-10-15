import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Solver {
    protected long visitedCount;
    final int depthLimit;

    Solver()
    {
        depthLimit = Integer.MAX_VALUE;
    }

    Solver(int depthLimit)
    {
        this.depthLimit = depthLimit;
    }

    abstract List<State> solve(State initialState) throws Exception;

    abstract int getMaximumFrontierSize();

    abstract int getFrontierSize();

    abstract int getExploredSize();

    long getVisitedCount()
    {
        return visitedCount;
    }

    abstract int getMaximumExploredDepth();

    List<State> createSolutionSequence(Node currentNode)
    {
        List<State> solutionSequence = new ArrayList<State>();

        while (currentNode != null) {
            solutionSequence.add(currentNode.state);
            currentNode = currentNode.parent;
        }
        Collections.reverse(solutionSequence);

        return solutionSequence;
    }
}

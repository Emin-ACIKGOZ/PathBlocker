import java.util.List;

public abstract class State implements Cloneable {

    abstract public boolean isGoal();

    abstract public List<Action> getActionList();

    abstract public State doAction(Action action);

    abstract public State undoAction(Action action);

    abstract public State clone() throws CloneNotSupportedException;
}
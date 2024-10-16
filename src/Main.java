import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        borderlessTest();
        //borderedTest();;
    }

    public static void borderlessTest(){
        //WITHOUT BORDER
        PathBlockerState pbs = new PathBlockerState("lvl01Bless.txt");
        pbs.printMatrix();
        System.out.println();
        System.out.println("1:");
        List<Action> actions1 = pbs.getActionList();
        PathBlockerState pbs2 = (PathBlockerState) pbs.doAction(actions1.get(0));
        pbs2.printMatrix();
        System.out.println();

        System.out.println("2 valid move:");
        List<Action> actions2 = pbs2.getActionList();
        PathBlockerState pbs3 = (PathBlockerState) pbs2.doAction(actions2.get(0));
        pbs3.printMatrix();
        System.out.println();

        System.out.println("2 invalid move1:");
        PathBlockerState pbs3_1 = (PathBlockerState) pbs2.doAction(actions2.get(1));
        pbs3_1.printMatrix();
        System.out.println();
        System.out.println("2 invalid move2:");
        PathBlockerState pbs3_2 = (PathBlockerState) pbs2.doAction(actions2.get(1));
        pbs3_1.printMatrix();
        System.out.println();
        System.out.println("2 invalid move3:");
        PathBlockerState pbs3_3 = (PathBlockerState) pbs2.doAction(actions2.get(1));
        pbs3_1.printMatrix();
        System.out.println();
    }

    public static void borderedTest(){
        //WITH BORDER
        PathBlockerState pbs = new PathBlockerState("lvl01.txt");
        pbs.printMatrix();
        System.out.println();
        System.out.println("1:");
        List<Action> actions1 = pbs.getActionList();
        PathBlockerState pbs2 = (PathBlockerState) pbs.doAction(actions1.get(0));
        pbs2.printMatrix();
        System.out.println();

        System.out.println("2 valid move:");
        List<Action> actions2 = pbs2.getActionList();
        PathBlockerState pbs3 = (PathBlockerState) pbs2.doAction(actions2.get(0));
        pbs3.printMatrix();
        System.out.println();

        System.out.println("2 invalid move1:");
        PathBlockerState pbs3_1 = (PathBlockerState) pbs2.doAction(actions2.get(1));
        pbs3_1.printMatrix();
        System.out.println();
        System.out.println("2 invalid move2:");
        PathBlockerState pbs3_2 = (PathBlockerState) pbs2.doAction(actions2.get(1));
        pbs3_1.printMatrix();
        System.out.println();
        System.out.println("2 invalid move3:");
        PathBlockerState pbs3_3 = (PathBlockerState) pbs2.doAction(actions2.get(1));
        pbs3_1.printMatrix();
        System.out.println();
    }

}
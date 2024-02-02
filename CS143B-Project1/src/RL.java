import java.util.LinkedList;

public class RL {

    LinkedList<pcb> PriorityZero;
    LinkedList<pcb> PriorityOne;
    LinkedList<pcb> PriorityTwo;

    public RL() {

        PriorityZero = new LinkedList<pcb>();
        PriorityOne = new LinkedList<pcb>();
        PriorityTwo = new LinkedList<pcb>();

    }

    public LinkedList<pcb> getPriorityZero() { return PriorityZero; }
    public LinkedList<pcb> getPriorityOne() { return PriorityOne; }
    public LinkedList<pcb> getPriorityTwo() { return PriorityTwo; }

    public LinkedList<pcb> getCurrentHighestPriority() {

        if(PriorityTwo.isEmpty()) {

            if(PriorityOne.isEmpty()) {

                //System.out.println("Returning Priority 0");
                return PriorityZero;

            }

            //System.out.println("Returning Priority 1");
            return PriorityOne;

        }

        //System.out.println("Returning Priority 2");

        return PriorityTwo;

    }


}

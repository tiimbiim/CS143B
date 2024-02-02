import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class pcb implements Comparable<pcb> {
    
    static int nextID = 0;
    int id, state, parent;
    int priority;
    LinkedList<pcb> childrenList;
    Map<rcb, Integer> resourcesList;

    public enum STATE {
        
        READY(1),
        BLOCKED(0);

        final int VALUE;

        STATE(int numVal) { VALUE = numVal; }

    }

    public pcb() {

        this.id = nextID++;
        this.state = STATE.READY.VALUE;
        this.parent = 0;
        this.priority = 0;
        this.childrenList = new LinkedList<pcb>();
        this.resourcesList = new HashMap<>();

    }

    LinkedList<pcb> getChildList() { return childrenList; }
    Map<rcb, Integer> getResourceList() { return resourcesList; }

    static void resetID() { nextID = 0; }

    @Override
    public int compareTo(pcb other) { return Integer.compare(other.priority, this.priority); }

    int getID() { return this.id; }
    void setID(int newID) { this.id = newID; }
    
    void setParent(int newParent) { this.parent = newParent; }
    int getParent() { return this.parent; }

    public void setState(int newState) { this.state = newState; }       //0: Blocked, 1: Ready
    int getState() { return this.state; }

    void setPriority(int newPrio) { this.priority = newPrio; }
    int getPriority() { return this.priority; }


    public String printProcess() {

        return "[Process " + getID() + " (" + this + ")" + ", " + "State: " + getState() + ", "+ "Priority: " + getPriority() + ", " + "Parent: " + getParent() + "]\n";

    }

    public String printChildren() {
        
        String s = "Children: \n";

        for (pcb p : childrenList) {

            s += p.printProcess();

        }

        return s;

    }

    public String printResources() {

        String r = "Resources: \n";

        for (rcb p : resourcesList.keySet()) {

            r += p.printResource() + " ";
            r += getResourceList().get(p);

        }

        return r;

    }


}

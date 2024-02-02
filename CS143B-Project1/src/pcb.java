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

        public final int VALUE;

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

    public LinkedList<pcb> getChildList() { return childrenList; }
    public Map<rcb, Integer> getResourceList() { return resourcesList; }

    public static void resetID() { nextID = 0; }

    @Override
    public int compareTo(pcb other) { return Integer.compare(other.priority, this.priority); }

    public int getID() { return this.id; }
    public void setID(int newID) { this.id = newID; }
    
    public void setParent(int newParent) { this.parent = newParent; }
    public int getParent() { return this.parent; }

    public void setState(int newState) { this.state = newState; }       //0: Blocked, 1: Ready
    public int getState() { return this.state; }

    public void setPriority(int newPrio) { this.priority = newPrio; }
    public int getPriority() { return this.priority; }


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

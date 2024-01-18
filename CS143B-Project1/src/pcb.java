import java.util.LinkedList;

public class pcb implements Comparable<pcb> {
    
    static int nextID = 0;
    int id, state, parent;
    int priority;
    LinkedList<pcb> childrenList;
    LinkedList<rcb> resourcesList;

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
        this.resourcesList = new LinkedList<rcb>();

    }

    LinkedList<pcb> getChildList() { return childrenList; }
    LinkedList<rcb> getResourceList() { return resourcesList; }

    @Override
    public int compareTo(pcb other) { return Integer.compare(other.priority, this.priority); }

    int getID() { return this.id; }
    
    void setParent(int newParent) { this.parent = newParent; }
    int getParent() { return this.parent; }

    public void setState(int newState) { this.state = newState; }
    int getState() { return this.state; }

    void setPriority(int newPrio) { this.priority = newPrio; }
    int getPriority() { return this.priority; }


    public String printProcess() {

        return "Process " + getID() + " (" + this + ")" + "\n" + "State: " + getState() + "\n"+ "Priority: " + getPriority() + "\n" + "Parent: " + getParent();

    }


}

import java.util.LinkedList;

public class Process {
    
    static int nextID = 0;
    int id, state, parent;
    int priority;
    LinkedList<Process> childrenList;
    LinkedList<Resource> resourcesList;

    public enum STATE {

        READY(1),
        BLOCKED(0);

        final int VALUE;

        STATE(int numVal) { VALUE = numVal; }

    }

    public Process() {

        this.id = nextID++;
        this.state = STATE.BLOCKED.VALUE;
        this.parent = 0;
        this.priority = 0;
        this.childrenList = new LinkedList<Process>();
        this.resourcesList = new LinkedList<Resource>();

    }

    LinkedList<Process> getChildList() { return childrenList; }
    LinkedList<Resource> getResourceList() { return resourcesList; }

    // void setID(int newID) { this.id = newID; }
    int getID() { return this.id; }

    public void setState(int newState) { this.state = newState; }
    int getState() { return this.state; }

    void setParent(int newParent) { this.parent = newParent; }
    int getParent() { return this.parent; }

    void setPriority(int newPrio) { this.priority = newPrio; }
    int getPriority() { return this.priority; }

}

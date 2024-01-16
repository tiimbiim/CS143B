import java.util.LinkedList;

public class Source {

    static Process[] PCB;
    static Resource[] RCB;
    static LinkedList<Process> RL;
    
    public static Process create(int priority, int caller) {
        
        Process newProcess = new Process();

        newProcess.setState(Process.STATE.READY.VALUE);
        newProcess.setPriority(priority);
        
        PCB[newProcess.getID()] = newProcess;
        
        if(caller != newProcess.getID())
        PCB[caller].getChildList().add(newProcess);     //in the case of init(), do not add Process 0 to its own childlist
        
        return newProcess;

    }

    public static void init() {

        PCB = new Process[16];
        RCB = new Resource[4];

        create(0, 0);
        create(1, 0);
        create(2, 0);

    }

    public static void main(String[] args) throws Exception {
        
        init();

        for (int i = 0; i < 16; i++) {

            if(PCB[i] != null) {
                System.out.println("PCB[" + i + "]\n" + PCB[i].printProcess());
                System.out.println("Children: " + PCB[i].getChildList().toString());
                System.out.println("Resources: " + PCB[i].getResourceList().toString() + "\n");
            }

        }

    }
}

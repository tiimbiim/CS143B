import java.util.Collections;
import java.util.LinkedList;

public class Source {

    static Process[] PCB;
    static Resource[] RCB;
    static LinkedList<Process> RL;
    
    public static Process create(int priority, int caller) {
        
        Process newProcess = new Process();

        newProcess.setState(Process.STATE.READY.VALUE);
        newProcess.setPriority(priority);
        newProcess.setParent(caller);
        
        PCB[newProcess.getID()] = newProcess;
        
        if(caller != newProcess.getID())
            PCB[caller].getChildList().add(newProcess);     //in the case of init(), do not add Process 0 to its own childlist
        
        return newProcess;

    }

    public static void delete(int process) {

        deleteChildren(PCB[process]);
        // release resources too

        PCB[PCB[process].getParent()].getChildList().remove(PCB[process]);
        
        for(Process p : RL) {

            if(p.getID() == process) {

                RL.remove(p);
                break;

            }

        }

        System.out.println("Process " + process + " deleted");

    }

    public static void deleteChildren(Process process) {

        for(Process child : process.getChildList()) {

            deleteChildren(child);
            //release resources too
        }

        process.getChildList().clear();

    }

    public static void request(int resource, int units, int caller) {       //TODO need to reconsider Resource implementation

        if(RCB[resource].getOwner() == -1) {        

            PCB[caller].getResourceList().add(RCB[resource]);
            RCB[resource].setOwner(caller);
            return;
        }

    }

    public static void init() {

        PCB = new Process[16];
        RCB = new Resource[] {new Resource(1, "r0"), new Resource(1, "r1"), new Resource(2, "r2"), new Resource(3, "r3")};
        RL = new LinkedList<Process>();

        create(0, 0);
        create(2, 0);
        create(1, 0);



        for (int i = 0; i < 16; i++) {

            if(PCB[i] != null)
                RL.add(PCB[i]);

        }

    }

    public static void main(String[] args) throws Exception {
        
        init();

        Collections.sort(RL);

        for(Process process : RL) {

            System.out.println(process.printProcess() + "\n");
            System.out.println("Process " + process.getID() + " children: ");
            
            for(Process child : process.getChildList()) {

                System.out.println(child.printProcess() + "\n");

            }

            System.out.println("-----------------------------------\n");

        }

        delete(1);

        for(Process process : RL) {

            System.out.println(process.printProcess() + "\n");
            System.out.println("Process " + process.getID() + " children: ");
            
            for(Process child : process.getChildList()) {

                System.out.println(child.printProcess());

            }

            System.out.println("-----------------------------------\n");

        }

    }
}

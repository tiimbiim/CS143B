import java.util.Collections;
import java.util.LinkedList;

public class Source {

    static pcb[] PCB;
    static rcb[] RCB;
    static LinkedList<pcb> RL;
    
    public static pcb create(int priority, int caller) {
        
        pcb newProcess = new pcb();

        newProcess.setState(pcb.STATE.READY.VALUE);
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
        
        for(pcb p : RL) {

            if(p.getID() == process) {

                RL.remove(p);
                break;

            }
        }

        System.out.println("Process " + process + " deleted");

    }

    public static void deleteChildren(pcb process) {

        for(pcb child : process.getChildList()) {

            deleteChildren(child);
            //release resources too
        }

        process.getChildList().clear();

    }

    public static void request(int resource, int units, int caller) {

        if(RCB[resource].getUnitCount() >= 0) {        //a resource may have an owner, but may have additional units to spare
            if(units <= RCB[resource].getUnitCount()) {

                PCB[caller].getResourceList().add(RCB[resource]);
                RCB[resource].setOwner(caller);
                return;

            }
            else {

                System.out.println("You are requesting too many units of this resource!");      //TODO should be an error code (-1)

            }
        }
        else {      



        }

    }

    public static void init() {

        PCB = new pcb[16];
        RCB = new rcb[] {new rcb(1, "r0"), new rcb(1, "r1"), new rcb(2, "r2"), new rcb(3, "r3")};
        RL = new LinkedList<pcb>();

        create(0, 0);
        create(2, 0);
        //create(1, 0);



        for (int i = 0; i < 16; i++) {

            if(PCB[i] != null)
                RL.add(PCB[i]);

        }

    }

    public static void main(String[] args) throws Exception {
        
        init();

        Collections.sort(RL);

        request(0, 1, 1);

        for(pcb process : RL) {

            System.out.println(process.printProcess() + "\n");
            System.out.println("Process " + process.getID() + " children: ");
            
            for(pcb child : process.getChildList()) {

                System.out.println(child.printProcess() + "\n");

            }

            System.out.println("-----------------------------------\n");

        }



    }
}

import java.util.Collections;
import java.util.LinkedList;

public class source {

    static pcb[] PCB;
    static rcb[] RCB;
    static LinkedList<pcb> RL;
    static int currentRunningProcess;
    
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

                for (int i = 0; i < 15; i++) {

                    if(RCB[resource].getOwners().isEmpty())     //if the resource has no owners, add it to the RCB's owners list
                        RCB[resource].getOwners().add(caller);

                }

                RCB[resource].decrementUnits(units);

                return;

            }
            else {

                System.out.println("You are requesting too many units of this resource!");      //TODO should be an error code (-1)

            }
        }
        else {      //the resource has no free units

            RCB[resource].getWaitList().add(caller);
            
        }

    }

    public static void release(int resource, int units, int caller) {

        for(rcb r : PCB[caller].getResourceList()) {

            if(r.getID() == resource) {

                PCB[caller].getResourceList().remove(r);
                r.getOwners().remove(0);

            }

        }

        //automatically remove the first element in the wait list and add to owners
        //PCB[caller].getResourceList().add(RCB[resource]);
        RCB[resource].getOwners().add(RCB[resource].getWaitList().get(0));
        PCB[RCB[resource].getWaitList().get(0)].getResourceList().add(RCB[resource]);
        RCB[resource].getWaitList().remove(0);

    }

    public static void timeOut() {




    }

    public static void init() {

        PCB = new pcb[16];
        RCB = new rcb[] {new rcb(1), new rcb(1), new rcb(2), new rcb(3)};
        RL = new LinkedList<pcb>();

        create(0, 0);
        create(2, 0);

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
            
            for(pcb child : process.getChildList())
                System.out.println(child.printProcess() + "\n");

            System.out.println("-----------------------------------\n");

        }



    }
}

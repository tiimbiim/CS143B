import java.util.Map;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

public class source {

    static pcb[] PCB;
    static rcb[] RCB;
    static RL RL;
    static pcb currentRunningProcess;
    static String filePath;
    
    public static void create(int priority, int caller) {
        
        pcb newProcess = new pcb();

        newProcess.setState(pcb.STATE.READY.VALUE);
        newProcess.setParent(caller);

        if(priority == 0) { newProcess.setPriority(-1); }
        else { newProcess.setPriority(priority); }

        PCB[newProcess.getID()] = newProcess;
        
        if(caller != 0)
            PCB[caller].getChildList().add(newProcess);     //in the case of init(), do not add Process 0 to its own childlist


        if(priority == 2) { 

            RL.getPriorityTwo().add(newProcess); 
            if(RL.getPriorityTwo().getFirst() == newProcess) { currentRunningProcess = RL.getPriorityTwo().getFirst(); }
        }
        else if(priority == 1) { 
            RL.getPriorityOne().add(newProcess); 
            if(RL.getPriorityOne().getFirst() == newProcess) { currentRunningProcess = RL.getPriorityOne().getFirst(); }
        }
        else if(priority == 0) { 
            RL.getPriorityZero().add(newProcess);
            if(RL.getPriorityZero().getFirst() == newProcess) { currentRunningProcess = RL.getPriorityZero().getFirst(); }
        }
        else { System.out.println("An invalid priority has been entered"); }
        

        System.out.print(currentRunningProcess.getID());

    }

    public static void delete(int process) {

        deleteChildren(PCB[process]);
        // release resources too

        PCB[PCB[process].getParent()].getChildList().remove(PCB[process]);
        
        for(pcb p : RL.getCurrentHighestPriority()) {

            if(p.getID() == process) {

                RL.getCurrentHighestPriority().remove(p);
                //break;

            }
        }

        System.out.print(currentRunningProcess.getID());

    }

    private static void deleteChildren(pcb process) {

        for(pcb child : process.getChildList()) {

            deleteChildren(child);
            //release resources too
        }

        if(process.getPriority() == 2) {

            RL.getPriorityTwo().remove(process);

        }
        else if(process.getPriority() == 1) {

            RL.getPriorityOne().remove(process);

        }
        else if(process.getPriority() == 0) {

            RL.getPriorityZero().remove(process);

        }
        else {

            System.out.println("error");

        }

        process.getChildList().clear();
        

    }

    public static void request(int resource, int units, int caller) {

        // System.out.println("[" + units + " units of resource " + resource + " have been requested]");

        if(RCB[resource].getUnitCount() >= units) {        //a resource may have an owner, but may have additional units to spare
           
            // System.out.println("[There are " + RCB[resource].getUnitCount() + " units of resource " + resource + " available]");

            PCB[caller].getResourceList().put(RCB[resource], units);

            if(!RCB[resource].getOwners().containsKey(caller))     //if the resource has no owners, add it to the RCB's owners list
                RCB[resource].getOwners().put(caller, units);

            RCB[resource].decrementUnits(units);

            System.out.print(currentRunningProcess.getID());

        }
        else {      //the resource has no free units

            // System.out.println("[Process " + caller + " has requested " + units + " units of resource " + resource + " but there are only " + RCB[resource].getUnitCount() + "]");
            
            // if(PCB[caller].getResourceList().containsKey(RCB[resource]))        //the calling function owns this resource (hence why it has no units), so it should not get waitlisted
            //     return;

            RCB[resource].getWaitList().put(caller, units);
            PCB[caller].setState(pcb.STATE.BLOCKED.VALUE);
            
            // if(RL.getCurrentHighestPriority().size() == 1) {

            //     if(RL.getCurrentHighestPriority().getFirst().getPriority() == pcb.STATE.BLOCKED.VALUE) {        //strange case where a lone process needs to be proc switched bc of being waitlisted
    
                    
    
            //     }
    
            // }

            RL.getCurrentHighestPriority().remove(PCB[caller]);
            currentRunningProcess = RL.getCurrentHighestPriority().getFirst();
            System.out.print(currentRunningProcess.getID());
        }
        

    }

    public static void release(int resource, int units, int caller) {

        if(PCB[caller].getResourceList().containsKey(RCB[resource]) && PCB[caller].getResourceList().get(RCB[resource]) >= units) {      //if the process actually owns the resource and has more than it's trying to release
            for(rcb r : PCB[caller].getResourceList().keySet()) {

                if(r.getID() == resource) {

                    PCB[caller].getResourceList().put(r, PCB[caller].getResourceList().get(r) - units);

                    if(PCB[caller].getResourceList().get(r) == 0) 
                        r.getOwners().remove(0);

                    
                }
            }
        }
        else {

            System.out.println("-1");       //the calling process does not own/have enough of the resource to release
            return;

        }
    
        if(!RCB[resource].getWaitList().isEmpty()) {

            RCB[resource].getOwners().put(getFirst(RCB[resource].getWaitList()).getKey(), getFirst(RCB[resource].getWaitList()).getValue());      //Add the first PCB waiting for this resource to the owners list
            PCB[getFirst(RCB[resource].getWaitList()).getKey()].getResourceList().put(RCB[resource], getFirst(RCB[resource].getWaitList()).getValue());       //add this resource to the PCB's resource list
            
            if( PCB[getFirst(RCB[resource].getWaitList()).getKey()].getPriority() == 2)
                RL.getPriorityTwo().add(PCB[getFirst(RCB[resource].getWaitList()).getKey()]);

            else if(PCB[getFirst(RCB[resource].getWaitList()).getKey()].getPriority() == 1) 
                RL.getPriorityOne().add( PCB[getFirst(RCB[resource].getWaitList()).getKey()]);

            else if( PCB[getFirst(RCB[resource].getWaitList()).getKey()].getPriority() == 0)
                RL.getPriorityZero().add( PCB[getFirst(RCB[resource].getWaitList()).getKey()]);

            PCB[getFirst(RCB[resource].getWaitList()).getKey()].setState(pcb.STATE.READY.VALUE);
            RCB[resource].getWaitList().remove(getFirst(RCB[resource].getWaitList()).getKey());      //remove the PCB from the resource's waitlist



        }

        System.out.print(currentRunningProcess.getID());

    }

    public static void timeOut() {

        if(!RL.getCurrentHighestPriority().isEmpty() && RL.getCurrentHighestPriority().size() != 1) {     

            pcb headElement = RL.getCurrentHighestPriority().removeFirst();
            RL.getCurrentHighestPriority().add(headElement);

        }

        currentRunningProcess = RL.getCurrentHighestPriority().getFirst();
        //System.out.println("[Switching from " + RL.getCurrentHighestPriority().getLast().getID() + " to " + currentRunningProcess.getID() + "]");
        System.out.print(currentRunningProcess.getID());

    }

    public static void init() {

        pcb.resetID();
        rcb.resetID();

        PCB = new pcb[16];
        RCB = new rcb[] {new rcb(1), new rcb(1), new rcb(2), new rcb(3)};
        RL = new RL();

        System.out.print("\n");

        create(0, 0);


    }

    public static Map.Entry<Integer,Integer> getFirst(Map<Integer, Integer> map) {

        for(Map.Entry<Integer,Integer> entry : map.entrySet()) { return entry; }

        return null;

    } 

    public static void printAllRLPriorities() {

        System.out.println("\nPRIORITY 2:");

        for(pcb process : RL.getPriorityTwo()) {

            System.out.println(process.printProcess());
            System.out.println(process.printChildren());
            System.out.println(process.printResources() + "\n");

        }

        System.out.println("------------------------------------\n");
        System.out.println("PRIORITY 1: ");

        for (pcb process : RL.getPriorityOne()) {

            System.out.println(process.printProcess());
            System.out.println(process.printChildren());
            System.out.println(process.printResources() + "\n");

        }

        System.out.println("------------------------------------\n");
        System.out.println("PRIORITY 0: ");

        for (pcb process : RL.getPriorityZero()) {

            System.out.println(process.printProcess());
            System.out.println(process.printChildren());
            System.out.println(process.printResources() + "\n");

        }

        System.out.println("------------------------------------\n");
        System.out.println("RCB: ");

        for(int i = 0; i < RCB.length; i++) {

            System.out.println(RCB[i].printResource());
            System.out.println(RCB[i].printOwners());
            System.out.println(RCB[i].printWaitList());

        }
    }
    public static void main(String[] args) throws Exception {
        
        //filePath = "input1.txt";
        filePath = "CS143B-Project1\\input1.txt";        //for laptop

        try(Scanner bim = new Scanner(new FileReader(filePath))) {

            while(bim.hasNextLine()) {

                String line = bim.nextLine();

                if(line.trim().isEmpty())
                    continue;

                Scanner bim2 = new Scanner(line);
                String command = bim2.next();
                

                if(bim2.hasNextInt()) {

                    int number1 = bim2.nextInt();

                    if(bim2.hasNextInt()) {
    
                        int number2 = bim2.nextInt();       //for 2 paraemeter commands (rq, rl)

                        //System.out.println(command + number1 + number2);  

                        if(command.equals("rq"))
                            request(number1, number2, currentRunningProcess.getID());
                        else if(command.equals("rl"))
                            release(number1, number2, currentRunningProcess.getID());
                        else {
                            //System.out.println(command + number1 + number2); 
                            System.out.println("bad input");
                        }

                       //System.out.println(currentRunningProcess.getID());

                        continue;
                    }

                    //System.out.println(command + number1);        //for 1 parameter commands (cr, de)
                    if(command.equals("cr"))
                        create(number1, currentRunningProcess.getID());
                    else if(command.equals("de"))
                        delete(number1);
                    else {
                        //System.out.println(command + number1);
                        System.out.println("bad input");
                    }

                    //System.out.println(currentRunningProcess.getID());

                    continue;
                }
                    
                //System.out.println(command);      //for 0 parameter commands (in, to)

                if(command.equals("in"))    
                    init();
                else if(command.equals("to"))
                    timeOut();
                else {
                    //System.out.println(command);
                    System.out.println("bad input");
                }

                //System.out.println(currentRunningProcess.getID());

                bim2.close();
            }

            printAllRLPriorities();

        }
        catch (IOException e) {

            e.printStackTrace();

        }
      
    }
}

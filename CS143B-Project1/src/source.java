import java.util.Map;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class source {

    static pcb[] PCB;
    static rcb[] RCB;
    static RL RL;
    static pcb currentRunningProcess;
    static String filePath;
    static int numRunningProcesses = 0;
    static FileWriter writer;

    /**
     * 
     * @param priority The priority of the process to be created
     * @param caller   The ID of the currently running process
     * @throws IOException 
     * 
     */
    public static void create(int priority, int caller) throws IOException {
        
        if(numRunningProcesses > 16) {

            System.out.println("curently in create - processes > 16");
            currentRunningProcess.setID(-1);        //There are too many processes being created, error
            return;

        }

        pcb newProcess = new pcb();

        numRunningProcesses += 1;

        newProcess.setState(pcb.STATE.READY.VALUE);
        newProcess.setParent(caller);

        if(priority == 0) { newProcess.setPriority(-1); }
        else { newProcess.setPriority(priority); }

        PCB[newProcess.getID()] = newProcess;
        
        if(caller != 0)
            PCB[caller].getChildList().add(newProcess);     //in the case of init(), do not add Process 0 to its own childlist


        if(priority == 2) { 

            RL.getPriorityTwo().add(newProcess); 
            //if(RL.getPriorityTwo().getFirst() == newProcess) { currentRunningProcess = RL.getPriorityTwo().getFirst(); }
        }
        else if(priority == 1) { 
            RL.getPriorityOne().add(newProcess); 
            //if(RL.getPriorityOne().getFirst() == newProcess) { currentRunningProcess = RL.getPriorityOne().getFirst(); }
        }
        else if(priority == 0) { 
            RL.getPriorityZero().add(newProcess);
            //if(RL.getPriorityZero().getFirst() == newProcess) { currentRunningProcess = RL.getPriorityZero().getFirst(); }
        }
        else { 
            //System.out.println("An invalid priority has been entered"); 
            System.out.println("curently in create - invalid priority");
            currentRunningProcess.setID(-1);
        }

        currentRunningProcess = RL.getCurrentHighestPriority().getFirst();
        
        System.out.print(currentRunningProcess.getID());

    }
    
    /**
     * 
     * @param process The ID of the process to be deleted.
     * @throws IOException 
     * 
     */
    public static void delete(int process) throws IOException {

        if(process > 15) {
            System.out.println("curently in delete - OOR delete");
            writer.write(Integer.toString(-1));       //specified process is out of range, error
            return;
        }

        if(PCB[process] == null) {
            //System.out.println("curently in delete - process DNE");
            //currentRunningProcess.setID(-1);        //specified process doesn't exist, error
            writer.write(Integer.toString(-1));
            return;
        }

        if(!currentRunningProcess.getChildList().contains(PCB[process])) {      //process to be deleted is not an immediate child, return

            System.out.println("curently in delete - process not child");
            writer.write(Integer.toString(-1));
            return;

        }

        deleteChildren(PCB[process]);
        PCB[PCB[process].getParent()].getChildList().remove(PCB[process]);

        for(int i = 0; i < RCB.length; i++) {

            if(RCB[i].getOwners().containsKey(PCB[process].getID())) {
                RCB[i].incrementUnits(RCB[i].getOwners().get(process));
                RCB[i].getOwners().remove(PCB[process].getID());
            }

            RCB[i].getWaitList().remove(process);

        }
        
        
        for(pcb p : RL.getCurrentHighestPriority()) {
            
            if(p.getID() == process)
            RL.getCurrentHighestPriority().remove(p);
            
        }
        
        moveFromWaitlist();

        System.out.print(currentRunningProcess.getID());

    }

    private static void deleteChildren(pcb process) {

        for(pcb child : process.getChildList()) {

            deleteChildren(child);
            child.getResourceList().clear();

            for(int i = 0; i < RCB.length; i++) {

                if(RCB[i].getOwners().containsKey(child.getID())) {

                    RCB[i].incrementUnits(RCB[i].getOwners().get(child.getID()));
                    RCB[i].getOwners().remove(child.getID());


                }



                    moveFromWaitlist();
            }
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

        //moveFromWaitlist();

        process.getChildList().clear();
        

    }
    /**
     * 
     * @param resource  The ID of the resource that is being requested
     * @param units     The number of units of the resource that is being requested
     * @param caller    The ID of the process requesting the resource
     * @throws IOException 
     * 
     */
    public static void request(int resource, int units, int caller) throws IOException {

        if(resource > 3) {      //OOB error
            currentRunningProcess.setID(-1);
            return;
        }

        if(units > RCB[resource].getUnitCount()) {      //trying to request more than a resource has, error

            currentRunningProcess.setID(-1);
            return;

        }
        //System.out.println("[" + units + " units of resource " + resource + " have been requested by process " + caller + "]");

        if(RCB[resource].getUnitCount() >= units) {        //a resource may have an owner, but may have additional units to spare
           
            // System.out.println("[There are " + RCB[resource].getUnitCount() + " units of resource " + resource + " available]");

            if(PCB[caller].getResourceList().containsKey(RCB[resource])) {      //if the calling process already owns some of the resource
                //System.out.println("The calling processes already owns some of the resource");
                PCB[caller].getResourceList().put(RCB[resource], PCB[caller].getResourceList().get(RCB[resource]) + units);
            }
            else{
                PCB[caller].getResourceList().put(RCB[resource], units);
            }


            if(!RCB[resource].getOwners().containsKey(caller))     //if the resource has no owners, add it to the RCB's owners list
                RCB[resource].getOwners().put(caller, units);

            RCB[resource].decrementUnits(units);

            System.out.print(currentRunningProcess.getID());

        }
        else {      //the resource has no free units

            //System.out.println("[Process " + caller + " has requested " + units + " units of resource " + resource + " but there are only " + RCB[resource].getUnitCount() + "]");
            
            //System.out.println("In the else");

            RCB[resource].getWaitList().put(caller, units);
            PCB[caller].setState(pcb.STATE.BLOCKED.VALUE);

            RL.getCurrentHighestPriority().remove(PCB[caller]);
            currentRunningProcess = RL.getCurrentHighestPriority().getFirst();
            System.out.print(currentRunningProcess.getID());
        }
        

    }

    /**
     * 
     * @param resource  The ID of the resource to be released
     * @param units     The  number of units of the resource to be released
     * @param caller    The ID of the process attempting ot release resource units
     * 
     */
    public static void release(int resource, int units, int caller) {

        if(resource > 3) {      //OOB Error
            currentRunningProcess.setID(-1);
            return;
        }

        if(PCB[caller].getResourceList().containsKey(RCB[resource]) && PCB[caller].getResourceList().get(RCB[resource]) >= units) {      //if the process actually owns the resource and has more than it's trying to release
            for(rcb r : PCB[caller].getResourceList().keySet()) {

                if(r.getID() == resource) {

                    PCB[caller].getResourceList().put(r, PCB[caller].getResourceList().get(r) - units);

                    if(PCB[caller].getResourceList().get(r) <= 0) {

                        r.getOwners().remove(caller);
                        PCB[caller].getResourceList().remove(r);

                    }

                }
            }
        }
        else {
            //System.out.println("curently in release");
            currentRunningProcess.setID(-1);        //special case where error occurs
            System.out.println("-1");       //the calling process does not own/have enough of the resource to release
            return;

        }
        
        //System.out.println("resource " + resource + " units: " + RCB[resource].getUnitCount());

        while(!RCB[resource].getWaitList().isEmpty()/*&& getFirst(RCB[resource].getWaitList()).getValue() <= RCB[resource].getUnitCount()*/) {

            RCB[resource].getOwners().put(getFirst(RCB[resource].getWaitList()).getKey(), getFirst(RCB[resource].getWaitList()).getValue());      //Add the first PCB waiting for this resource to the owners list
            PCB[getFirst(RCB[resource].getWaitList()).getKey()].getResourceList().put(RCB[resource], getFirst(RCB[resource].getWaitList()).getValue());       //add this resource to the PCB's resource list
            RCB[resource].decrementUnits(getFirst(RCB[resource].getWaitList()).getValue());
            
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

    public static void init() throws IOException {

        pcb.resetID();
        rcb.resetID();

        PCB = new pcb[16];
        RCB = new rcb[] {new rcb(1), new rcb(1), new rcb(2), new rcb(3)};
        RL = new RL();

        System.out.print("\n");

        numRunningProcesses = 0;

        create(0, 0);


    }
    /**
     * 
     * @param map The map to get the first element of
     * @return    Returns either a Map entry or null
     * 
     */
    public static Map.Entry<Integer,Integer> getFirst(Map<Integer, Integer> map) {

        for(Map.Entry<Integer,Integer> entry : map.entrySet()) { return entry; }

        return null;

    }

    public static void moveFromWaitlist() {     //release handles this in its own function, but not delete(), etc

        for (int i = 0; i < RCB.length; i++) {


            if(RCB[i].getOwners().isEmpty() && !RCB[i].getWaitList().isEmpty() && getFirst(RCB[i].getWaitList()).getValue() <= RCB[i].getUnitCount()) {      //Automatically, we should move to the owners list

                RCB[i].getOwners().put(getFirst(RCB[i].getWaitList()).getKey(), getFirst(RCB[i].getWaitList()).getValue());
                
                if(PCB[getFirst(RCB[i].getWaitList()).getKey()].getPriority() == 2)
                RL.getPriorityTwo().add(PCB[getFirst(RCB[i].getWaitList()).getKey()]);
                
                else if(PCB[getFirst(RCB[i].getWaitList()).getKey()].getPriority() == 1) 
                RL.getPriorityOne().add( PCB[getFirst(RCB[i].getWaitList()).getKey()]);
                
                else if( PCB[getFirst(RCB[i].getWaitList()).getKey()].getPriority() == 0)
                RL.getPriorityZero().add( PCB[getFirst(RCB[i].getWaitList()).getKey()]);
                
                PCB[getFirst(RCB[i].getWaitList()).getKey()].setState(pcb.STATE.READY.VALUE);
                PCB[getFirst(RCB[i].getWaitList()).getKey()].getResourceList().put(RCB[i], getFirst(RCB[i].getWaitList()).getValue());

                RCB[i].getWaitList().remove(getFirst(RCB[i].getWaitList()).getKey());



            }
            
        }

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
        
        filePath = "input.txt";
        //filePath = "CS143B-Project1\\input1.txt";        //for laptop

        //File output = new File("output.txt");
        writer = new FileWriter("output.txt");

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

                        //System.out.println("command: " + command + number1 + number2);  

                        if(command.equals("rq")) {
                            //System.out.print("current running process - " + currentRunningProcess.getID());
                            request(number1, number2, currentRunningProcess.getID());
                            writer.write(Integer.toString(currentRunningProcess.getID()));
                        }
                        else if(command.equals("rl")) {
                            release(number1, number2, currentRunningProcess.getID());
                            writer.write(Integer.toString(currentRunningProcess.getID()));
                        }
                        else {
                            //System.out.println(command + number1 + number2); 
                            System.out.println("bad input");
                        }

                       //System.out.println(currentRunningProcess.getID());

                        continue;
                    }

                    //System.out.println("command - " + command + number1);        //for 1 parameter commands (cr, de)
                    if(command.equals("cr")) {
                        create(number1, currentRunningProcess.getID());
                        writer.write(Integer.toString(currentRunningProcess.getID()));
                    }
                    else if(command.equals("de")) {
                        delete(number1);
                        //writer.write(Integer.toString(currentRunningProcess.getID()));
                    }
                    else {
                        //System.out.println(command + number1);
                        System.out.println("bad input");
                    }

                    //System.out.println(currentRunningProcess.getID());

                    continue;
                }
                    
                //System.out.println(command);      //for 0 parameter commands (in, to)

                if(command.equals("in")) {
                    init();
                    writer.write("\n" + Integer.toString(currentRunningProcess.getID()));
                }
                else if(command.equals("to")) {
                    timeOut();
                    writer.write(Integer.toString(currentRunningProcess.getID()));
                }
                else {
                    //System.out.println(command);
                    System.out.println("bad input");
                }

                //System.out.print("main - " + currentRunningProcess.getID());
                bim2.close();
            }
            
            writer.close();
            printAllRLPriorities();

        }
        catch (IOException e) {

            e.printStackTrace();

        }
      
    }
}

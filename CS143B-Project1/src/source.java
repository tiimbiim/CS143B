public class Source {

    static Process[] pcb;
    static Resource[] rcb;

    
    public static Process create(int priority, int caller) {
        
        Process newProcess = new Process();

        newProcess.setState(Process.STATE.READY.VALUE);
        newProcess.setPriority(priority);
        
        pcb[newProcess.getID()] = newProcess;
        
        if(caller != newProcess.getID())
        pcb[caller].getChildList().add(newProcess);     //in the case of init(), do not add Process 0 to its own childlist
        
        return newProcess;

    }

    public static void init() {

        pcb = new Process[16];
        rcb = new Resource[4];

        create(0, 0);

        create(1, 0);

    }

    public static void main(String[] args) throws Exception {
        
        init();

        for (int i = 0; i < 16; i++) {

            System.out.println(pcb[i].toString());

        }

    }
}

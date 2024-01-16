public class PCB {
    
    Process[] PCB;

    public PCB() {

        this.PCB = new Process[16];

    }

    Process[] getPCB() { return this.PCB; }
    
    Process create(int priority, int parent) {

        Process newChild = new Process();
        newChild.setState(Process.STATE.READY.VALUE);
        newChild.setPriority(priority);
        //newChild.setParent(parent);

        for (int i = 0; i < PCB.length; i++) {

            if(PCB[i] == null) {

                if(i != newChild.getID()) { 

                    System.out.println("Mismatch between PCB index and Process ID - i: "
                                        + i + "Process ID: " + newChild.getID());
                    break;
                }

                PCB[i] = newChild;

                if(parent != i)
                    PCB[parent].childrenList.add(newChild);
                
                break;

            }

        }

        System.out.println("Process " + newChild.getID() + " created");

        return newChild;
    }

    void delete(int procID) {

        

    }

}

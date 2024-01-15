public class PCB {
    
    Process[] PCB;

    public PCB() {

        this.PCB = new Process[16];

    }

    Process create(int prio, int parent) {

        Process newChild = new Process();
        newChild.setState(Process.STATE.READY.VALUE);
        newChild.setPriority(prio);

        for (int i = 0; i < PCB.length; i++) {

            if(PCB[i] == null) {

                PCB[i] = newChild;

            }

            if(i == parent) {

                PCB[parent].childrenList.add(newChild);

            }

        }

        return newChild;
    }

}

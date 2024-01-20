public class rcb {
    
    static int nextID = 0;
    int units, id;
    int[] owners;
    int[] waitList;

    public rcb(int units) {

        this.units = units;
        this.id = nextID++;
        owners = new int[units];     //there can only be as many owners as there are units
        waitList = new int[15];      //there are 16 total processes, so edge case, all 16 processes request a single resource (1 holds it while the other 15 wait)

        for(int i = 0; i < 15; i++) { waitList[i] = -1; }     //set all to -1 to show nothing is on waitlist

        

    }

    void decrementUnits(int units) { 
        System.out.println("Units decrementing by " + units);
        this.units -= units; 
    }
    void incrementUnits(int units) { 
        System.out.println("Units incrementing by " + units);
        this.units += units; 
    }
    int getUnitCount() { return this.units; }

    int getID() { return this.id; }

    int[] getWaitList() { return this.waitList; }
    int[] getOwners() { return this.owners; }



}

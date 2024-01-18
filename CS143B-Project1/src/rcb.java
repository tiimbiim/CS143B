public class rcb {
    
    int owner, units;
    String name;
    int[] waitList;

    public rcb(int units, String name) {

        this.owner = -1;        //by default, set to -1 for no owner
        this.units = units;
        this.name = name;
        waitList = new int[15];      //there are 16 total processes, so edge case, all 16 processes request a single resource (1 holds it while the other 15 wait)

    }

    void decrementUnits(int units) { 
        System.out.println(this.name + " units decrementing by " + units);
        this.units -= units; 
    }
    void incrementUnits(int units) { 
        System.out.println(this.name + " units incrementing by " + units);
        this.units += units; 
    }
    int getUnitCount() { return this.units; }
    
    void setOwner(int newOwner) { this.owner = newOwner; }
    int getOwner() { return this.owner; }

    String getName() { return this.name; }

    int[] getWaitList() { return this.waitList; }

}

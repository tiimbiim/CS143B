public class rcb {
    
    int owner, units;
    String name;
    int[] waitList;

    public rcb(int units, String name) {

        this.owner = -1;        //by default, set to -1 for no owner
        this.units = units;
        this.name = name;
        waitList = new int[3];      //max resource units caps at 3, so default to 3

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

import java.util.ArrayList;

public class rcb {
    
    static int nextID = 0;
    int units, id;
    ArrayList<Integer> owners;
    ArrayList<Integer> waitList;

    public rcb(int units) {

        this.units = units;
        this.id = nextID++;
        owners = new ArrayList<Integer>();     //there can only be as many owners as there are units
        waitList = new ArrayList<Integer>();      //there are 16 total processes, so edge case, all 16 processes request a single resource (1 holds it while the other 15 wait)

        

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

    ArrayList<Integer> getWaitList() { return this.waitList; }
    ArrayList<Integer> getOwners() { return this.owners; }



}

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class rcb {
    
    static int nextID = 0;
    int units, id;
    Map<Integer, Integer> owners;
    Map<Integer, Integer> waitList;     //in the form (Process : UnitsHeld)

    public rcb(int units) {

        this.units = units;
        this.id = nextID++;
        owners = new HashMap<>();
        waitList = new LinkedHashMap<>();

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

    Map<Integer, Integer> getWaitList() { return this.waitList; }
    Map<Integer, Integer> getOwners() { return this.owners; }

    String printResource() { 
        return "[Resource " + getID() + " (" + this + ")" + "]"; 
    }
    String printOwners() { 

        String s = "Owners: \n";

        for (int i : getOwners().keySet()) {

            s += "[Process " + i + " : " + owners.get(i) + "]";

        }

        return s;
    }
    String printWaitList() { 

        String s = "Wait List: \n";

        for(int i : waitList.keySet()) {

            s += "[Process " + i + " : " + waitList.get(i) + "]";

        }

        return s;

    }



}

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class rcb {
    
    static int nextID = 0;
    int units, id, totalUnits;
    Map<Integer, Integer> owners;
    Map<Integer, Integer> waitList;     //in the form (Process : UnitsHeld)

    public rcb(int units) {

        this.units = units;
        this.totalUnits = units;
        this.id = nextID++;
        owners = new HashMap<>();
        waitList = new LinkedHashMap<>();

    }

    public static void resetID() { nextID = 0; }

    public int getTotalUnits() { return this.totalUnits; }

    public void decrementUnits(int units) { 
        //System.out.println("Units decrementing by " + units);
        this.units -= units; 
    }
    public void incrementUnits(int units) { 
        //System.out.println("Units incrementing by " + units);
        this.units += units; 
    }
    public int getUnitCount() { return this.units; }

    public int getID() { return this.id; }

    public Map<Integer, Integer> getWaitList() { return this.waitList; }
    public Map<Integer, Integer> getOwners() { return this.owners; }

    public String printResource() { 
        return "[Resource " + getID() + " (" + this + ")" + "]"; 
    }
    public String printOwners() { 

        String s = "Owners: \n";

        for (int i : getOwners().keySet()) {

            s += "[Process " + i + " : " + owners.get(i) + "]";

        }

        return s;
    }
    public String printWaitList() { 

        String s = "Wait List: \n";

        for(int i : waitList.keySet()) {

            s += "[Process " + i + " : " + waitList.get(i) + "]";

        }

        s += "\n";

        return s;

    }



}

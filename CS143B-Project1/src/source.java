

public class Source {


    public static void main(String[] args) throws Exception {
        
        Resource r1 = new Resource(0, 1, "r1");

        System.out.println("Held by process " + r1.getOwner());
        System.out.println("Units: " + r1.getUnitCount());

        r1.incrementUnits(1);

        System.out.println(r1.name + " units: " + r1.getUnitCount());

        r1.decrementUnits(1);
        r1.setOwner(2);

        System.out.println("Held by process " + r1.getOwner());
        System.out.println(r1.name + " units: " + r1.getUnitCount());

        // Process p1 = new Process();
        // Process p2 = new Process();

        // System.out.println(p1.getID() + " created");

        // System.out.println(p2.getID() + " contains " + p2.getChildList().size());

    }
}

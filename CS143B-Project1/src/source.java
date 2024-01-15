

public class source {
    public static void main(String[] args) throws Exception {
        
        resource r1 = new resource(0, 1, "r1");

        System.out.println("Held by process " + r1.getOwner());
        System.out.println("Units: " + r1.getUnitCount());

        r1.incrementUnits(1);

        System.out.println(r1.name + " units: " + r1.getUnitCount());

        r1.decrementUnits(1);
        r1.setOwner(2);

        System.out.println("Held by process " + r1.getOwner());
        System.out.println(r1.name + " units: " + r1.getUnitCount());

    }
}

public class resource {
    
    int owner, units;
    String name;
    


    public resource(int owner, int units, String name) {

        this.owner = owner;
        this.units = units;
        this.name = name;

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

}

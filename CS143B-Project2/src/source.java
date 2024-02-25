import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

enum Residents {

    PT_AND_PG,
    PT_ONLY,
    PG_ONLY,
    NONE

}

public class source {
    public static void main(String[] args) throws Exception {
        
        Residents r = Residents.PT_AND_PG;

        int[] PM = new int[524288];
        int[][] D = new int[1024][512];
        int[] freePages = new int[1024];

        int PA = 0;

        /*
         *  Initialize freePages with corresponding free pages
         */

        freePages[0] = -1;
        freePages[1] = -1;
        for(int i = 2; i < freePages.length; i++) { freePages[i] = i; }

        /* 
         *  Given init data in the form of
         *  s z f, ... <--
         *  s p j, ...
         */        

        //Scanner s1 = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader("myInit.txt"));
        //System.out.print("Enter the first line of init data: ");
        String line = br.readLine();

        if((line != null)) {
            String[] numbers = line.split("\\s+");

            for(int i = 0; i < numbers.length; i += 3) {

                    int s = Integer.parseInt(numbers[i]);
                    int z = Integer.parseInt(numbers[i+1]);
                    int f = Integer.parseInt(numbers[i+2]);

                    //System.out.println("s: " + s + " - z: " + z + " - f: " + f);

                    PM[2*s] = z;
                    PM[(2*s)+1] = f;

                    if(f < 0) { r = Residents.PG_ONLY; }        //THE PT IS NOT RESIDENT

                    if(f >= 0 && freePages[f] >= 0) {

                        freePages[f] = -1;     //set page to not resident

                    }
                
            }

        }

        /*
         *  Given init data in the form of 
         *  s z f, ...
         *  s p f, ... <-- 
         */

        //Scanner s2 = new Scanner(System.in);
        //System.out.print("Enter the second line of init data: ");
        String line2 = br.readLine();

        if((line2 != null)) {
            String[] numbers = line2.split("\\s+");

            for(int i = 0; i < numbers.length; i += 3) {

                int s = Integer.parseInt(numbers[i]);
                int p = Integer.parseInt(numbers[i+1]);
                int f = Integer.parseInt(numbers[i+2]);

                System.out.println("s: " + s + " - p: " + p + " - f: " + f);
                
                if(PM[2*s+1] >= 0) {

                    PM[PM[(2*s)+1]*512 + p] = f;

                }
                else {

                    D[Math.abs(PM[2*s+1])][p] = f;

                }

                if(f <= 0) { r = Residents.NONE; }

                if(f >= 0 && freePages[f] >= 0) {

                    freePages[f] = -1;     //set page to not resident

                }

                
                
            }

        }

        /*
         *  Given VA input data in the form of
         *  x, y, z, ...
         */

        //Scanner s3 = new Scanner(System.in);
        //FileWriter fw = new FileWriter("output.txt");
        BufferedReader br2 = new BufferedReader(new FileReader("myVA.txt"));
        //System.out.print("Enter the VA data: ");
        String line3 = br2.readLine();

        String[] numbers = line3.split("\\s+");

        //int[] results = new int[numbers.length];

        for(int i = 0; i < numbers.length; i++) {

            int number = Integer.parseInt(numbers[i]);

            int s = number >> 18;
            int w = (number & 511);
            int p = (number >> 9) & 511;
            int pw = (number & 262143);


            if(PM[2*s+1] < 0) {       

                System.out.println("PT NOT RESIDENT");

                int nextFree = 0;
                int diskBlock = Math.abs(PM[2*s+1]);

                for (int j = 0; j < freePages.length; j++) {

                    if(freePages[j] != -1) {

                        nextFree = freePages[j];
                        freePages[j] = -1;

                        PM[2*s+1] = nextFree;       //allocate next free frame

                        break;
                    }

                }

                PM[PM[2*s+1]*512 + p] = D[diskBlock][p];


            }

            if(PM[PM[2*s+1]*512 + p] < 0) {

                System.out.println("PAGE NOT RESIDENT");

                int nextFree = 0;
                //int diskBlock = Math.abs(PM[2*s+1]);

                for (int j = 0; j < freePages.length; j++) {

                    if(freePages[j] != -1) {

                        nextFree = freePages[j];
                        freePages[j] = -1;

                        PM[PM[2*s+1]*512 + p] = nextFree;

                        break;
                    }


                }

            }

            System.out.println("Performing translation with values s: " + s + " - p: " + p + " - w: " + w);

            if(pw < PM[2*s] /*&& PT >= 0 && pg >= 0*/) { 
                int PT = PM[2*s+1];
                int pg = PM[PT*512+p];
                PA = pg*512+w; 

                System.out.println("PT: " + PT + " pg: " + pg);

            }

            System.out.println("PA: " + PA);

        }

        br.close();
        br2.close();

    }
}

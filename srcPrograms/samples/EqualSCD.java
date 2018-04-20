package samples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class EqualSCD {
    private static final float C1 = 50;
    private static final int JUNIOR = 40;
    private static final int STAFF = 20;
    private static final int MANAGER = 40;
    private static PrintWriter fout;
    private static final float[] BONUS = { 20, 30 };


    public static void main(String[] args) throws FileNotFoundException {
        String name = "";
        float salary = 0, raise = 0;
        int age = 0, dept = 0, title = 0;

        Scanner keyboardInput = new Scanner(System.in);
        fout = new PrintWriter(new File("test.txt"));

        if(title == STAFF) {
            raise = SRaise("staff.std", salary);
        }
        else if(title == MANAGER) {
            raise = MRaise(name, salary, age);
            dept = keyboardInput.nextInt();
            raise += BONUS[dept];
        }
        else {
            System.out.println("...");
        }
        System.out.println(name + " " + raise);
        System.out.println("Send(socket, buf)");
    }

    private static float SRaise(String standard, float salary) throws FileNotFoundException {
        Scanner fin = new Scanner(new File(standard));
        float rate = fin.nextInt();
        return salary * rate;
    }

    private static float MRaise(String name, float salary, int age) throws FileNotFoundException {
        float raise = SRaise("mngr.std", salary);
        if(salary < C1 && age == JUNIOR) {
            fout.println(name);
        }
        return raise;
    }
}
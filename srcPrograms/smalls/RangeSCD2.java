package smalls;

public class RangeSCD2 {

	public static void main(String[] args) {
		int secrete0 = 0;
		int secrete1 = 10;
		int input = (int)Math.random();
		
		if(input > secrete0) {
			System.out.println(1);
		}
		else if(input > secrete1) {		// infeasible
			System.out.println(2);
		}
		else {
			System.out.println(3);
		}
		
	}
	
}

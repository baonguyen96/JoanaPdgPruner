public class ThreadDemo {

    private String name;
    private int execTime;

    public ThreadDemo(String name, int execTime) {
        this.name = name;
        this.execTime = execTime;
    }

    public void exec() {
        int count = 0;

        for(int i = 0; i < execTime; i++) {
            count = ((count + i) % 1000) / 2;
        }

//        double throwExceptionOrNotValue = Math.random();
//        if(throwExceptionOrNotValue < 0.5) {
//            throw new Exception(name + " " + throwExceptionOrNotValue);
//        }
    }

}

package src.threads.lab2175;

class Printer extends Thread{
    private Counter mycounter;

    Printer (Counter mycounter){
        this.mycounter=mycounter;
    }

    public void run() {
        synchronized (mycounter) {
            try {
                mycounter.wait();
                System.out.println("Printer: notification received");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Printing : " + Storage.myint);

    }
}

package src.threads.lab2175;

public class Main {
    public static void main(String[] args) {
        Counter mycounter = new Counter();

        Thread printer = new Printer(mycounter);
        Thread counter = new Thread(mycounter);

        printer.start();
        counter.start();
    }
}

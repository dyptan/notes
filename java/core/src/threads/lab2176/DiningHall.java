package src.threads.lab2176;

public class DiningHall implements Runnable{
    static int pizzaNum;
    static int studentID = 1;

    public void makePizza() {
        pizzaNum++;
    }
    public synchronized void servePizza() {
        String result;
        if (pizzaNum > 0) {
            result = "Served ";
            pizzaNum--;
        } else result = "Starved ";
        System.out.println(result + studentID+" Thread ID: "+Thread.currentThread().getId());
        studentID++;
    }

    @Override
    public void run() {
        servePizza();
    }

    public static void main(String[] args) {
        DiningHall d = new DiningHall();
        for (int i = 0; i < 10; i++)
            d.makePizza();
        for (int i = 1; i <= 20; i++) {
            new Thread(d).start();
        }
    }
}

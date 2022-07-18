package src.threads.lab2171;

/**
 * Created by diptan on 17.07.18.
 */
class MyTimeBomb extends Thread {
    @Override
    public void run(){
        for (int i = 10; i > 0 ; i--) {
            System.out.println(i);
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                System.out.println(e.getStackTrace());
            }
        }
        System.out.println("Boom!");
    }
}

public class Main {
    public static void main(String[] args) {
        MyTimeBomb bomb = new MyTimeBomb();
        bomb.start();
    }
}

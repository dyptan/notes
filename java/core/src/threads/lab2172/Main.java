package src.threads.lab2172;

/**
 * Created by diptan on 17.07.18.
 */

public class Main {
    public static void main(String[] args) {
        MyTimeBomb2 bomb = new MyTimeBomb2();
        Thread bombthread = new Thread(bomb);
        bombthread.start();
    }
}


class MyTimeBomb2 implements Runnable{
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


package src.threads.lab2175;

class Counter implements Runnable{
    public void run() {
            while (Storage.myint < 1000) {
                Storage.myint++;
                System.out.println("Counter: " + Storage.myint);
            }
            notifier();
    }

    synchronized void notifier(){
        System.out.println("Counter: sending notification");
        notifyAll();
    }
}

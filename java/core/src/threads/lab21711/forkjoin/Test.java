package src.threads.lab21711.forkjoin;


import java.util.concurrent.*;

import static src.threads.lab21711.forkjoin.SumOfN.N;

public class Test extends Thread {

    public static volatile int varVlt=0;
    public static int varNonVlt=0;

    public static Object obj1=new Object();
    public static Object obj2=new Object();

    public static void main(String[] args) throws InterruptedException {
        // write your code here
        /*MyThread thr1=new MyThread();
        MyThread thr2=new MyThread();
        MyThread thr3=new MyThread();
        thr1.setPriority(1);
        thr2.setPriority(5);
        thr3.setPriority(6);
        thr1.start();
        thr2.start();
        thr3.start();
        //thr2.interrupt();
        //thr2.interrupt();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thr1.interrupt();
        thr2.interrupt();
        thr3.interrupt();*/

        /*ThreadPoolExecutor tpe=new ThreadPoolExecutor(3,10,30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        MyTask mt[]=new MyTask[25];
        for (int i = 0; i < mt.length; i++) {
            mt[i]=new MyTask("Task #"+i);
            tpe.execute(mt[i]);
        }
        tpe.shutdown();*/

        /*int numOfWorks=20;
        ExecutorService pool= Executors.newFixedThreadPool(4);
        MyTestCallable works[]=new MyTestCallable[numOfWorks];
        Future[]futures=new Future[numOfWorks];
        for (int i = 0; i < numOfWorks; i++) {
            works[i]=new MyTestCallable(i+1);
            futures[i]=pool.submit(works[i]);
        }
        for (int i = 0; i < numOfWorks; i++) {
            try {
                System.out.println(futures[i].get()+" ended");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();*/


        /*MyThread mt=new MyThread();
        mt.setDaemon(false);
        mt.start();
        mt.sleep(2000);
        System.out.println("BigIntTest finished");*/


        /*ChangeListener cl=new ChangeListener();
        ChangeMaker cm=new ChangeMaker();
        ChangeMaker cm2=new ChangeMaker();
        cl.start();
        cm.start();
        cm2.start();
        Thread.sleep(10);

        cl.interrupt();
        cm.interrupt();
        cm2.interrupt();*/


        /*UserAccount  acc=new UserAccount(1000);
        for (int i = 0; i < 300; i++) {
            UserAction act=new UserAction(acc,100);
            act.start();
        }
        Thread.sleep(500);
        System.out.println(acc.getMoney());
        */
        /*int[] myArray = new int[1000];
        MySumCount sumCount1 = new MySumCount();
        MySumCount sumCount2 = new MySumCount();
        sumCount1.setDaemon(true);
        sumCount2.setDaemon(true);

        int checkSum=0;
        for (int i = 0; i < myArray.length; i++){
            myArray[i] = (int) (Math.random() * 1000);
            checkSum+=myArray[i];
        }

        MySumCount.index=0;
        MySumCount.lastIndex=1000;
        sumCount1.setIntArray(myArray);
        sumCount2.setIntArray(myArray);

        sumCount1.start();
        sumCount2.start();

        Thread.sleep(100);

        System.out.println("checkSum = "+checkSum);
        System.out.println("Sum = "+MySumCount.getResultSum());
*/
        //System.out.println(sumCount1.getResultSum());
        //System.out.println(sumCount2.getResultSum());


        /*MyCounter mc=new MyCounter();
        mc.increment();
        System.out.println(mc.counter);*/

        /*MyCounter mc=new MyCounter();
        ReentrantLock locker=new ReentrantLock();
        for (int i = 0; i < 10; i++) {
            Thread t=new Thread(new CountThread(mc,locker));
            t.setName("Thread "+i);
            t.start();
        }*/

        /*MyAtomicCounter mac=new MyAtomicCounter();
        MyAtomicThread []mat=new MyAtomicThread[100];
        for (int i = 0; i < mat.length; i++) {
            mat[i]=new MyAtomicThread(mac,1000);
        }
        for (MyAtomicThread t:mat
             ) {
            t.start();
        }
        for (MyAtomicThread t:mat
             ) {
            t.join();
        }

        System.out.println(mac.i1+" "+mac.i2+" "+mac.i3);*/

        //waiting

        /*MyWaitData mwd=new MyWaitData();
        MyWaitDataSender mwds[]={
                new MyWaitDataSender(mwd,"user1"),
                new MyWaitDataSender(mwd,"user2"),
                new MyWaitDataSender(mwd,"user3")
        };
        for (MyWaitDataSender sender:mwds
             ) {
            sender.start();
        }
        MyWaitDataGenereator mwdg=new MyWaitDataGenereator(mwd);
        mwdg.start();*/

        //deadlock

        /*Thread t1=new Thread1();
        Thread t2=new Thread2();
        t1.start();
        t2.start();*/


        //semaphore
        /*Semaphore semaphore=new Semaphore(3);
        Cart cart=new Cart();

        new AddWorker("Adder1",cart,semaphore).start();
        new AddWorker("Adder2",cart,semaphore).start();
        new AddWorker("Adder3",cart,semaphore).start();
        new ReduceWorker("Reducer",cart,semaphore).start();*/

        //CountDownLatch
        /*
        CountDownLatch counter=new CountDownLatch(3);
        new Runner(counter,"Carl").start();
        new Runner(counter,"Joe").start();
        new Runner(counter,"Jack").start();
        System.out.println("Starting thr countdown");
        long countValue=counter.getCount();
        while(countValue>0)
        {
            Thread.sleep(1000);
            System.out.println(countValue);
            if(countValue==1)
            {
                System.out.println("Start");
            }
            counter.countDown();
            countValue=counter.getCount();
        }*/

        //Concurrent Collections

        /*BlockingQueue<Long>queue=new ArrayBlockingQueue<Long>(10);
        Producer producer=new Producer(queue);
        Consumer consumer1=new Consumer(queue);
        Thread pp=new Thread(producer);
        pp.start();
        Thread.sleep(4000);
        Thread cc=new Thread(consumer1);
        cc.start();
        Thread.sleep(10000);
        pp.interrupt();
        */
        //cc.interrupt();

        //Fork-Join
        ForkJoinPool pool=new ForkJoinPool(SumOfN.NUM_THREADS);
        long computedSum=pool.invoke(new SumOfN.RecSumOfN(0,N));
        long formulaSum=(N*(N+1))/2;
        System.out.println(computedSum+" "+formulaSum);
    }
}

class SumOfN {

    public static long N=100/*0_000L*/;
    public static final int NUM_THREADS=15;
    static class RecSumOfN extends RecursiveTask<Long>
    {
        long from;
        long to;

        public RecSumOfN(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            if((to-from)<=N/NUM_THREADS)
            {
                long local_sum=0;
                for (long i = from; i <= to ; i++) {
                    local_sum+=i;
                }
                System.out.println("From "+from+" to "+to+" = "+local_sum);
                return local_sum;
            }
            else
            {
                long mid=(from+to)/2;
                System.out.println("Two ranges: "+from+"-"+mid+" , "+mid+"-"+to);
                RecSumOfN firstHalf = new RecSumOfN(from,mid);
                firstHalf.fork();
                RecSumOfN secondHalf = new RecSumOfN(mid+1,to);
                long resultSecond = secondHalf.compute();
                return firstHalf.join()+resultSecond;
            }
        }
    }

}

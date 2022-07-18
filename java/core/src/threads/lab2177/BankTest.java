package src.threads.lab2177;

import java.util.concurrent.ThreadLocalRandom;

class Bank {
    private int accounts[];
    private int numOfAccounts;
    public Bank(int num_accounts, int init_balance) {
        accounts = new int[num_accounts];
        numOfAccounts = num_accounts;
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = init_balance;
        }
    }
    synchronized void transfer(int from, int to, int amount) {
        try {
            System.out.println("from: " + from + " to: " + to +
                    " amount: " + amount);

            if (accounts[from] < amount) return;
            accounts[from] = accounts[from] - amount;
            Thread.sleep((int)(100*Math.random()));
            accounts[to] = accounts[to] + amount;

            System.out.println("Total balance: " + totalBalance());

        } catch (InterruptedException e) {

        }
    }
    public int totalBalance() {
        int total = 0;
        for (int v : accounts) {
            total = total + v;
        }
        return total;
    }
    public int getNumberAccounts() {
        return numOfAccounts;
    }
}



class Transfer extends Thread{
    private Bank bank;
    private int from;
    private int max;
    public Transfer(Bank bank, int from, int max_amount) {
        this.bank = bank;
        this.from = from;
        max = max_amount;
    }
    public void run(){
        while (true) {
            int from = ThreadLocalRandom.current().nextInt(0, bank.getNumberAccounts());
            int to = ThreadLocalRandom.current().nextInt(0, bank.getNumberAccounts());
            bank.transfer(from, to, (int) (100 * Math.random()));
            // System.out.println("Thread ID: "+Thread.currentThread().getId());
        }
    }
}



public class BankTest {
    public static final int N_ACCOUNTS = 5;
    public static final int INIT_BALANCE = 1000;

    public static void main(String args[]) {
        Bank mybank = new Bank(N_ACCOUNTS,INIT_BALANCE);
        Transfer randomTransfer = new Transfer(mybank, 1, 100);
        for (int i = 1; i <= 20; i++) {
            new Thread(randomTransfer).start();
        }
    }
}

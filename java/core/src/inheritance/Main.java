package src.inheritance;

/**
 * Created by diptan on 01.06.18.
 */
public class Main {
    public static void main(String[] args){
        Device dev1 = new Device("Samsung", 1200.0f, "SM7734592359867T");
        Device dev2 = new Device("Samsung", 1200.0f, "SM7734592359867T");
        Monitor mon1 = new Monitor("LG", 1000.0f, "LG089723460987L", 1200, 800);
        Monitor mon2 = new Monitor("LG", 1000.0f, "LG089723460987L", 1200, 800);
        System.out.println(dev1.equals(dev2));
        System.out.println(mon1.equals(mon2));

    }
}

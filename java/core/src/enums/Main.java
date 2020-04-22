package src.enums;

import java.util.*;

/**
 * Created by diptan on 27.06.18.
 */
public class Main
{
    public static void main(String[] args) {

//        for (DaysOftheWeek day:
//             DaysOftheWeek.values())
//        {
//            switch (day) {
//                case TUESDAY:
//                    System.out.println("My Java day:" + DaysOftheWeek.TUESDAY);
//                    break;
//                case THURSDAY:
//                    System.out.println("My Java day:" + DaysOftheWeek.THURSDAY);
//                    break;
//            }
//        }

//        System.out.println("Enter a day of the week: ");
//        Scanner input = new Scanner(System.in);
//        DaysOftheWeek currentDay = DaysOftheWeek.valueOf(input.next());
//        System.out.println("The next day of the week is "+currentDay.nextDay());


        System.out.println("--------------------------------------------------------------------------");
        System.out.println("---------------------Railroad management simulator------------------------");
        System.out.println("--------------------------------------------------------------------------");
        TrainSchedule tc = new TrainSchedule();
        boolean exitSwitch = true;
        while(exitSwitch) {
            System.out.println();
            System.out.println("--------------------------Adding a train ------------------------------");
            tc.addTrain();
            System.out.println("------------------------------");
            System.out.print("Would you like to add one more train? Type YES or NO: ");
            Scanner scanner = new Scanner(System.in);

            switch (scanner.nextLine().toUpperCase()){
                case "YES":
                    System.out.println("------------------------------");
                    continue;
                case "NO":
                    exitSwitch = false;
                    break;
            }
        }


        System.out.println();
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("List of all trains: ");
        tc.printTrains();
        System.out.println("--------------------------------------------------------------------------");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Found trains: ");
        tc.searchTrain("Kyiv", "Odessa", DaysOftheWeek.WEDNESDAY);
        System.out.println("--------------------------------------------------------------------------");

    }
}

enum DaysOftheWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    public Enum<DaysOftheWeek> nextDay (){
        int nextIndex=valueOf(this.name()).ordinal()+1;
        if (nextIndex<values().length)
            return values()[nextIndex];
        else
            return values()[0];
    }
}

class TrainSchedule {
    private ArrayList<Train> trains = new ArrayList<>();

    public TrainSchedule() {
    }

    public TrainSchedule(Train train) {
        this.trains.add(train);
    }

    public void addTrain(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter train's number: ");
        int newTrainNumber = Integer.valueOf(scanner.nextLine());

        System.out.print("Enter train's dispatch station: ");
        String newTrainDispStation = scanner.nextLine();

        System.out.print("Enter train's arrival station: ");
        String newTrainArrStation = scanner.nextLine();

        System.out.print("Enter train's dispatch time: ");
        String newTrainDispTime = scanner.nextLine();

        System.out.print("Enter train's arrival time: ");
        String newTrainArrTime = scanner.nextLine();

        System.out.print("Enter train's scheduled day of the week: ");

        String[] newTrainDay = scanner.nextLine().toUpperCase().split("\\W\\s*");
        EnumSet<DaysOftheWeek> enumeratedTrainDays = EnumSet.noneOf(DaysOftheWeek.class);

        for (String day:newTrainDay) {
            enumeratedTrainDays.add(DaysOftheWeek.valueOf(day));
        }

        Train currentTrain = new Train(newTrainNumber);
        currentTrain.setStationDispatch(newTrainDispStation);
        currentTrain.setStationArrival(newTrainArrStation);
        currentTrain.setTimeDispatch(newTrainDispTime);
        currentTrain.setTimeArrival(newTrainArrTime);
        currentTrain.setDays(enumeratedTrainDays);

        trains.add(currentTrain);
    }

    public void searchTrain(String searchDispStation,
                            String searchArrStation,
                            DaysOftheWeek searchDepartureAfterTheDay){

        HashSet<Train> trainsFound = new HashSet<>();

        for (Train aTrain : trains) {
            if (aTrain.getStationDispatch().contains(searchDispStation)){
                if (aTrain.getStationArrival().contains(searchArrStation)){
                    for (Enum<DaysOftheWeek> day:aTrain.getDays() ) {
                        if (day.ordinal() > searchDepartureAfterTheDay.ordinal()) {
                            trainsFound.add(aTrain);
                        }
                    }
                }
            }
        }

        for (Train aTrain : trainsFound) {
            System.out.println(aTrain);
        }

    }

    public void printTrains(){
        for (Train aTrain : trains) {
            System.out.println(aTrain);
        }
    }

}


class Train{
    private int number;
    private String stationDispatch ;
    private String stationArrival  ;
    private String timeDispatch   ;
    private String timeArrival    ;
    private EnumSet<DaysOftheWeek> days   ;

    public Train(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public String getStationDispatch() {
        return stationDispatch;
    }
    public void setStationDispatch(String stationDispatch) {
        this.stationDispatch = stationDispatch;
    }

    public String getStationArrival() {
        return stationArrival;
    }
    public void setStationArrival(String stationArrival) {
        this.stationArrival = stationArrival;
    }

    public String getTimeDispatch() {
        return timeDispatch;
    }
    public void setTimeDispatch(String timeDispatch) {
        this.timeDispatch = timeDispatch;
    }

    public String getTimeArrival() {
        return timeArrival;
    }
    public void setTimeArrival(String timeArrival) {
        this.timeArrival = timeArrival;
    }

    public EnumSet<DaysOftheWeek> getDays() {
        return days;
    }
    public void setDays(EnumSet<DaysOftheWeek> days) {
        this.days = days;
    }

    @Override
    public String toString(){
        return String.format("Train no. %s, that goes each %s from %s to %s, dispatching at %s and arriving at %s",
                getNumber(),
                getDays().toString(),
                getStationDispatch(),
                getStationArrival(),
                getTimeDispatch(),
                getTimeArrival());
    }
}
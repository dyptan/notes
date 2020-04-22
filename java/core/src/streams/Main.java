package src.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) {
        List<Integer> intsProvidedByStream = Stream.iterate(10, x->x+10)
                .limit(10)
                .map(y->y/2)
                .collect(toList());

        System.out.println("List genegated by stream: "+intsProvidedByStream);

        System.out.println("\nList filtered by stream: ");
        Stream.of("One", "two", "three", "four", "five")
                .filter(x->(x.startsWith("f")))
                .sorted()
                .forEach(System.out::println);

        Random randomAge = new Random();

        List<Person> myPeople = Arrays.asList(
                (new Person(22,"Ivan", true)),
                (new Person(12,"Vovan", true)),
                (new Person(19,"Myshko", true)),
                (new Person(56,"Oksana", false)),
                (new Person(25,"Ilona", false))
                );

        System.out.println("\nMales selected for mil service: ");
        myPeople.stream()
                .filter(s->(18<s.getAge() & s.getAge()<27))
                .filter(Person::isMale)
                .forEach(System.out::println);

        Double avgFemaleAge = myPeople.stream()
                .filter(x->!x.isMale())
                .mapToInt(Person::getAge)
                .average()
                .getAsDouble();

        System.out.println("\nAverage female age is: "+avgFemaleAge);
    }
}

package src.streams;

public class Person {
    private int age;
    private String name;
    private Boolean isMale;

    public Person(int age, String name, Boolean isMale) {
        this.age = age;
        this.name = name;
        this.isMale = isMale;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public Boolean isMale() {
        return isMale;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", isMale=" + isMale +
                '}';
    }
}

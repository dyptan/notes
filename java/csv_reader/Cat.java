package src.csv_reader;

public class Cat {
    private int Id;
    private String type;
    private String name;
    private int age;
    private String color;

    public Cat(int id, String type, String name, int age, String color) {
        Id = id;
        this.type = type;
        this.name = name;
        this.age = age;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "Id=" + Id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                '}';
    }

    public int getId() {
        return Id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getColor() {
        return color;
    }
}

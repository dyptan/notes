package src.interfaces;
import static java.lang.Math.PI;

/**
 * Created by diptan on 01.06.18.
 */
public class Circle extends Shape {
    double radius;

    @Override
    public int compareTo(Object o) {
        Circle circleInstance = (Circle)o;
        if (this.equals(circleInstance))
            return 0;
        else
        if (this.calcArea() < circleInstance.calcArea())
            return -1;
        else
            return 1;
    }

    public Circle(String shapeColor, double radius) {
        super(shapeColor);
        this.radius = radius;
    }

    @Override
    public double calcArea() {
        return PI*radius;
    }
}

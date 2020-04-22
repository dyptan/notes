package src.interfaces;

/**
 * Created by diptan on 01.06.18.
 */
public class Rectangle extends Shape{
    private double width;
    private double height;

    @Override
    public int compareTo(Object o) {
        Rectangle rectangleInstance = (Rectangle)o;
        if (this.equals(rectangleInstance))
            return 0;
        else
        if (this.calcArea() < rectangleInstance.calcArea())
            return -1;
        else
            return 1;
    }

    public Rectangle(String shareColor, double width, double height) {
        super(shareColor);
        this.width = width;
        this.height = height;
    }

    public double calcArea() {
        return width*height;
    }
}

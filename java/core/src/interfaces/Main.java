package src.interfaces;

import java.util.Arrays;

/**
 * Created by diptan on 01.06.18.
 */
public class Main {
    public static void main(String[] args) {
        Shape[] shapes = {
                new Circle("Green",100.0),
                new Circle("Yellow",200.0),
                new Rectangle("Blue",15.0,200.0),
                new Rectangle("White",10.0,20.0),

        };

        Rectangle[] rectangles = {
                new Rectangle("Black",30.0f, 40.0f),
                new Rectangle("While",30.0f, 40.0f),
                new Rectangle("Cyan",32.0f, 40.0f),
                new Rectangle("Blue",10.0f, 40.0f),
                new Rectangle("Black",30.0f, 40.0f),
                new Rectangle("Black",30.0f, 40.0f),

        };

        double totalArea = 0;
        double totalCircleArea = 0;
        double totalRectangleArea = 0;

        for (Shape shape:shapes) {
            totalArea += shape.calcArea();
            if (shape instanceof Circle) totalCircleArea += shape.calcArea();
            if (shape instanceof Rectangle) totalRectangleArea += shape.calcArea();
        }
        System.out.println("Tolal Area: "+totalArea);
        System.out.println("Tolal Circle Area: "+totalCircleArea);
        System.out.println("Tolal Rectangle Area: "+totalRectangleArea);

        Arrays.sort(rectangles);

        for (Rectangle rectangle: rectangles) {
            rectangle.draw();
        }
    }
}

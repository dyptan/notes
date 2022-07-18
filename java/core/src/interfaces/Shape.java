package src.interfaces;

/**
 * Created by diptan on 01.06.18.
 */
public abstract class Shape implements Drawable, Comparable {

    String shapeColor;

    public Shape(String shapeColor) {
        this.shapeColor = shapeColor;
    }

    public abstract double calcArea();

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+", color is: "+shapeColor;
    }

    public void draw(){
        System.out.println(this.toString()+" area: "+this.calcArea());
    };

    public abstract int compareTo(Object o);

    @Override
    public int hashCode() {
        int result = 17;
        result = 31*result+shapeColor.hashCode();
        result = 31*result+((int)this.calcArea());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.hashCode()==obj.hashCode())
            return true;
        else
            return false;
    }
}

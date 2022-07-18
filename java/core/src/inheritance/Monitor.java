package src.inheritance;

/**
 * Created by diptan on 01.06.18.
 */
public class Monitor extends Device {
    private int resolutionX;
    private int resolutionY;

    public Monitor(String manufacturer, float price, String serialNumber, int resolutionX, int resolutionY) {
        super(manufacturer, price, serialNumber);
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public void setResolutionX(int resolutionX) {
        this.resolutionX = resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public void setResolutionY(int resolutionY) {
        this.resolutionY = resolutionY;
    }

    @Override
    public String toString() {
        return super.toString()+" X ="+this.getResolutionX()+" Y ="+this.getResolutionY();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31*result+resolutionX;
        result = 31*result+resolutionY;
        return result;
    }
}

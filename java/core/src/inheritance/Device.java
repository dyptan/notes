package src.inheritance;

/**
 * Created by diptan on 01.06.18.
 */
public class Device {
    private String manufacturer;
    private float price;
    private String serialNumber;

    public Device(String manufacturer, float price, String serialNumber) {
        this.manufacturer = manufacturer;
        this.price = price;
        this.serialNumber = serialNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+": manufacturer ="+this.getManufacturer()+
                ", price ="+this.getPrice()+", serialNumber ="+this.getSerialNumber();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31*result+manufacturer.hashCode();
        result = 31*result+Float.floatToIntBits(price);
        result = 31*result+serialNumber.hashCode();
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

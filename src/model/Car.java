package model;

/**
 * @author Stefan Sidlovsky
 */
public class Car {
    private String plate;
    private final String manufacturer;
    private final String model;
    private final int manYear;
    private final String category;
    private final int seats;
    private final Transmission transmission;
    private final Fuel fuel;
    private final FourXFour fourXFour;
    private final int power;
    private final int maxSpeed;
    private Integer rentPrice;
    private Integer priceForHour;
    private Availability availability;

    public Car(String plate, String manufacturer, String model, int manYear, String category,
               int seats, Transmission transmission, Fuel fuel, FourXFour fourXFour, int power, int maxSpeed,
               Integer rentPrice, Integer priceForHour, Availability availability) {
        this.plate = plate;
        this.manufacturer = manufacturer;
        this.model = model;
        this.manYear = manYear;
        this.category = category;
        this.seats = seats;
        this.transmission = transmission;
        this.fuel = fuel;
        this.fourXFour = fourXFour;
        this.power= power;
        this.maxSpeed = maxSpeed;
        this.rentPrice = rentPrice;
        this.priceForHour = priceForHour;
        this.availability = availability;
    }


    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getManYear() {
        return manYear;
    }

    public String getCategory() {
        return category;
    }

    public int getSeats() {
        return seats;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public FourXFour getFourXFour() {
        return fourXFour;
    }

    public int getPower() {
        return power;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public Integer getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Integer rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Integer getPriceForHour() {
        return priceForHour;
    }

    public void setPriceForHour(Integer priceForHour) {
        this.priceForHour = priceForHour;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }
}

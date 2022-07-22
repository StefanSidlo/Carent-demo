package model;

import data.CarDao;
import data.UserDao;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.concurrent.TimeUnit;

/**
 * @author Stefan Sidlovsky
 */
public class Order {
    private final String username;
    private final String carPlate;
    private final Timestamp orderTimestamp;
    private Timestamp endTimestamp;
    private final LocalDateTime orderDateTime;
    private OrderProgress progress;
    private final String deliveryAddress;
    private String returnAddress;
    private int finalPrice = 0;
    private final UserDao userDao;
    private final CarDao carDao;
    private final Car car;

    public Order(String username, String carPlate, Timestamp orderTimestamp, String deliveryAddress, UserDao userDao, CarDao carDao){
        this.username = username;
        this.carPlate = carPlate;
        this.orderTimestamp = orderTimestamp;
        this.endTimestamp = null;
        this.orderDateTime = orderTimestamp.toLocalDateTime();
        this.progress = OrderProgress.IN_PROGRESS;
        this.deliveryAddress = deliveryAddress;
        this.returnAddress = "-";
        this.userDao = userDao;
        this.carDao = carDao;
        this.car = getCar();
    }

    public String getUsername() {
        return username;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public Timestamp getOrderTimestamp() {
        return orderTimestamp;
    }

    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    public LocalDateTime getOrderDateTime(){
        return orderDateTime;
    }

    public String getOrderDate(){
        return orderDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getOrderTime(){
        return orderDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public LocalDateTime getEndDateTime(){
        return endTimestamp.toLocalDateTime();
    }

    public String getEndDate(){
        LocalDateTime endDateTime = getEndDateTime();
        return endDateTime.getDayOfMonth() + "-" + endDateTime.getMonthValue() + "-" + endDateTime.getYear();
    }

    public String getEndTime(){
        LocalDateTime endDateTime = getEndDateTime();
        return endDateTime.getHour() + ":" + endDateTime.getMinute();
    }

    public OrderProgress getProgress() {
        return progress;
    }

    public User getUser(){
        return userDao.findUser(username);
    }

    public Car getCar(){
        return carDao.findCar(carPlate);
    }

    public String getModel(){
        return car.getModel();
    }

    public Integer getRentPrice(){
        return car.getRentPrice();
    }

    public Integer getPriceForHour(){
        return car.getPriceForHour();
    }

    public String progressToString(){
        return progress.toString();
    }

    public String timestampToString(){
        return orderTimestamp.toString();
    }

    public void setProgress(OrderProgress progress) {
        this.progress = progress;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public long getDuration(){
        return endTimestamp.getTime() - orderTimestamp.getTime();
    }

    public long getDurationInHours(){
        return TimeUnit.MILLISECONDS.toHours(getDuration());
    }

    public int getCurrentPrice() {
        int validDuration;
        if (getDurationInHours() == 0){
            validDuration = 0;
        }
        else {
            validDuration = (int) getDurationInHours() - 1;
        }
        return getRentPrice() + getPriceForHour() * (validDuration);
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "username='" + username + '\'' +
                ", carPlate='" + carPlate + '\'' +
                '}';
    }
}

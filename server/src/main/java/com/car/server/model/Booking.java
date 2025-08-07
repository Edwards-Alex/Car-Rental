package com.car.server.model;

import com.car.server.model.enums.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Document(collection = "bookings") //Specifies the collection name in MongoDB
public class Booking {

    @Id //Marks this field as the primary identifier
    private String id;

    @DBRef
    @NotBlank(message = "car is required")
    private Car car;

    @DBRef
    @NotBlank(message = "user is required")
    private User user;

    @DBRef
    @NotBlank(message = "owner is required")
    private User owner;

    @NotBlank(message = "pickUp-date is required")
    private LocalDate pickupDate;

    @NotBlank(message = "return-date is required")
    private LocalDate returnDate;


    @Field(targetType = FieldType.STRING)
    private BookingStatus status = BookingStatus.PENDING;


    @NotBlank(message = "price is required")
    private BigDecimal price;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;


    public Booking(){}

    public Booking(Car car, User user, User owner, LocalDate pickupDate, LocalDate returnDate, BigDecimal price) {
        this.car = car;
        this.user = user;
        this.owner = owner;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickUpDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

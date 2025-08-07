package com.car.server.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "cars") //Specifies the collection name in MongoDB
public class Car {
    @Id //Marks this field as the primary identifier
    private String id;

    @DBRef
    private User owner;

    @NotBlank(message = "brand is required")
    private String brand;

    @NotBlank(message = "model is required")
    private String model;

    @NotBlank(message = "image is required")
    private String image;

    @NotBlank(message = "year is required")
    private Integer year;

    @NotBlank(message = "category is required")
    private String category;

    @NotBlank(message = "seatingCapacity is required")
    private Integer seatingCapacity;

    @NotBlank(message = "fuelType is required")
    private String fuelType;

    @NotBlank(message = "transmission is required")
    private String transmission;

    @NotBlank(message = "pricePerDay is required")
    private BigDecimal pricePerDay;

    @NotBlank(message = "location is required")
    private String location;

    @NotBlank(message = "description is required")
    private String description;

    private Boolean available = true;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public Car() {}

    public Car(String id, User owner, String brand, String model, String image, Integer year, String category, Integer seatingCapacity, String fuelType, String transmission, BigDecimal pricePerDay, String location, String description, Boolean available) {
        this.id = id;
        this.owner = owner;
        this.brand = brand;
        this.model = model;
        this.image = image;
        this.year = year;
        this.category = category;
        this.seatingCapacity = seatingCapacity;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.pricePerDay = pricePerDay;
        this.location = location;
        this.description = description;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}

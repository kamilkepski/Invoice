package pl.kepski.invoice;

import java.io.Serializable;

public class Company implements Serializable {
    public Company() {}

    public Company(String name, String address, String city, String country, String id) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.id = id;
    }

    private String name;
    private String address;
    private String city;
    private String country;
    private String id;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getId() {
        return id;
    }
}

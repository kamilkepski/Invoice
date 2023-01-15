package pl.kepski.invoice.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;

public class Invoice {

    private Seller seller;

    private Client client;

    private Recipient recipient;

    ArrayList<Product> products = new ArrayList<>();

    ObservableList<Product> products_list = FXCollections.observableArrayList(products);

    private String ID;
    private String city;
    private LocalDate date;
    private LocalDate saleDate;
    private String paymentDeadline;
    private String paymentMethod;

    public Invoice() {}

    public Invoice(String id, String city, LocalDate date, LocalDate saleDate, String paymentDeadline, String paymentMethod) {
        this.ID = id;
        this.city = city;
        this.date = date;
        this.saleDate = saleDate;
        this.paymentDeadline = paymentDeadline;
        this.paymentMethod = paymentMethod;
    }

    public Invoice(Client client, Recipient recipient, Seller seller, ArrayList<Product> products) {
        this.client = client;
        this.recipient = recipient;
        this.seller = seller;
        this.products = products;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setProducts_list(ObservableList<Product> products_list) {
        this.products_list = products_list;
    }

    public Seller getSeller() {
        return seller;
    }

    public Client getClient() {
        return client;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ObservableList<Product> getProducts_list() {
        return products_list;
    }

    public String getID() {
        return ID;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public String getPaymentDeadline() {
        return paymentDeadline;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public void setPaymentDeadline(String paymentDeadline) {
        this.paymentDeadline = paymentDeadline;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

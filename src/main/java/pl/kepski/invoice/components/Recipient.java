package pl.kepski.invoice.components;

public class Recipient extends Company {
    public static boolean instanceExists = false;

    public Recipient() {}

    public Recipient(String name, String address, String city, String country, String id) {
        super(name, address, city, country, id);
        Recipient.instanceExists = true;
    }
}

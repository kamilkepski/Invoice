package pl.kepski.invoice;

public class Seller extends Company {
    private String bankAccountNumber;

    public Seller() {}

    public Seller(String name, String address, String city, String country, String id, String bankAccountNumber) {
        super(name, address, city, country, id);
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }
}

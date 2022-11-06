package pl.kepski.invoice;

public class Product {
    public Product() {}

    public Product(String productName, String productMeasure, int productAmount, float productPrice, int productTaxValue) {
        this.productName = productName;
        this.productMeasure = productMeasure;
        this.productAmount = productAmount;
        this.productPrice = productPrice;
        this.productTaxValue = productTaxValue;
    }

    private String productName;
    private String productMeasure;
    private int productAmount;
    private float productPrice;
    private float productPriceNoTax;
    private float productAllPrice;
    private Integer productTaxValue;
    private float productTax;

    public String getProductName() {
        return productName;
    }

    public String getProductMeasure() {
        return productMeasure;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public float getProductPriceNoTax() {
        productPriceNoTax = productPrice/(productTaxValue.floatValue()/100+1);
        return productPriceNoTax;
    }

    public float getProductAllPrice() {
        productAllPrice = productAmount * productPriceNoTax;
        return productAllPrice;
    }

    public int getProductTaxValue() {
        return productTaxValue;
    }

    public float getProductTax() {
        productTax = (productTaxValue * productAllPrice)/100;
        return productTax;
    }

    public float getProductFinalPrice() {
        return productAllPrice + productTax;
    }
}

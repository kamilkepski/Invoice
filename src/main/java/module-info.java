module pl.kepski.invoice {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;
    requires java.desktop;


    opens pl.kepski.invoice to javafx.fxml;
    exports pl.kepski.invoice;
    exports pl.kepski.invoice.components;
    opens pl.kepski.invoice.components to javafx.fxml;
}
package pl.kepski.invoice;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Duration;
import pl.kepski.invoice.components.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceController {

    Invoice invoice = new Invoice();

    Client client = new Client();

    Recipient recipient = new Recipient();

    Seller seller = new Seller();

    ArrayList<Product> products = new ArrayList<>();

    ArrayList<Client> clients = new ArrayList<>();

    ObservableList<Product> products_list = FXCollections.observableArrayList(products);

    ObservableList<Client> clients_list = FXCollections.observableArrayList(clients);

    // Buttons
    @FXML private Button client_btn;
    @FXML private Button seller_btn;
    @FXML private Button settings_btn;
    @FXML private Button product_btn;
    @FXML private Button prd_add_btn;
    @FXML private Button prd_delete_btn;
    @FXML private Button client2_add_btn;
    @FXML private Button start_btn;
    @FXML private Button new_invoice;
    @FXML private Button author_btn;
    @FXML private Button client_list_btn;

    // Panes
    List<Pane> panesList;
    @FXML private GridPane client_pane;
    @FXML private GridPane seller_pane;
    @FXML private GridPane settings_pane;
    @FXML private Pane product_pane;
    @FXML private Pane add_product_pane;
    @FXML private Pane client_pane2;
    @FXML private Pane start_pane;
    @FXML private Pane author_pane;
    @FXML private Pane clients_list_pane;
    @FXML private Pane delete_product_pane;

    // TextFields
    @FXML private TextField invoice_id;
    @FXML private TextField invoice_city;
    @FXML private TextField invoice_payment_time;
    @FXML private TextField client_address;
    @FXML private TextField client_city;
    @FXML private TextField client_country;
    @FXML private TextField client_name;
    @FXML private TextField client_nip;
    @FXML private TextField seller_city;
    @FXML private TextField seller_country;
    @FXML private TextField seller_name;
    @FXML private TextField seller_nip;
    @FXML private TextField seller_address;
    @FXML private TextField seller_bank;
    @FXML private TextField recipient_name;
    @FXML private TextField recipient_city;
    @FXML private TextField recipient_address;
    @FXML private TextField recipient_country;
    @FXML private TextField recipient_nip;
    @FXML private TextField product_name;
    @FXML private TextField product_measure;
    @FXML private TextField product_amount;
    @FXML private TextField product_price;

    @FXML private Label header_label;
    @FXML private Label generate_status;

    @FXML private DatePicker invoice_date;
    @FXML private DatePicker sell_date;

    @FXML private ChoiceBox<String> product_tax_value;
    @FXML private ChoiceBox<String> invoice_payment_method;

    @FXML private TableView<Product> edit_products_table;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TableColumn<Product, String> productMeasure;
    @FXML private TableColumn<Product, Integer> productAmount;
    @FXML private TableColumn<Product, Float> productPrice;
    @FXML private TableColumn<Product, Integer> productTax;

    @FXML private TableView<Client> see_clients;
    @FXML private TableColumn<Client, String> clientName;
    @FXML private TableColumn<Client, String> clientID;

    @FXML
    public void initialize() {
        product_tax_value.getItems().removeAll(product_tax_value.getItems());
        product_tax_value.getItems().addAll("0", "5", "8", "23");
        product_tax_value.getSelectionModel().select("8");
        invoice_payment_method.getItems().removeAll(invoice_payment_method.getItems());
        invoice_payment_method.getItems().addAll("Gotówka", "Przelew");
        invoice_payment_method.getSelectionModel().select("Gotówka");
        panesList = List.of(product_pane, add_product_pane, delete_product_pane, client_pane2,
                start_pane, clients_list_pane, client_pane, settings_pane, seller_pane, author_pane);
    }

    public void returnStatus(String statusMessage) {
        generate_status.setText(statusMessage);
        generate_status.setStyle("-fx-text-fill: black");
        generate_status.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.rgb(178, 252, 251), new CornerRadii(5), null)));
        generate_status.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(2)
        );
        visiblePause.setOnFinished(
                event -> generate_status.setVisible(false)
        );
        visiblePause.play();
    }

    private void hideAllPanes() {
        panesList.forEach(pane -> pane.setVisible(false));
    }

    public void changeView(ActionEvent event) {
        if (event.getSource() == client_btn || event.getSource() == new_invoice) {
            hideAllPanes();
            header_label.setText("Dane nabywcy");
            client_pane.setVisible(true);

        } else if (event.getSource() == client2_add_btn) {
            hideAllPanes();
            header_label.setText("Dane odbiorcy");
            client_pane2.setVisible(true);

        } else if (event.getSource() == seller_btn) {
            hideAllPanes();
            header_label.setText("Dane sprzedawcy");
            seller_pane.setVisible(true);

        } else if (event.getSource() == settings_btn) {
            hideAllPanes();
            invoice_date.setValue(LocalDate.now());
            sell_date.setValue(LocalDate.now());
            header_label.setText("Ustawienia");
            settings_pane.setVisible(true);

        } else if (event.getSource() == product_btn) {
            hideAllPanes();
            header_label.setText("Produkty/usługi");
            product_pane.setVisible(true);

        } else if (event.getSource() == prd_add_btn) {
            hideAllPanes();
            header_label.setText("Dodawanie produktu/usługi");
            add_product_pane.setVisible(true);

        } else if (event.getSource() == prd_delete_btn) {
            hideAllPanes();
            header_label.setText("Usuwanie produktu/usługi");
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            productMeasure.setCellValueFactory(new PropertyValueFactory<>("productMeasure"));
            productAmount.setCellValueFactory(new PropertyValueFactory<>("productAmount"));
            productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
            productTax.setCellValueFactory(new PropertyValueFactory<>("productTaxValue"));
            edit_products_table.setItems(products_list);
            delete_product_pane.setVisible(true);

        } else if (event.getSource() == start_btn) {
            hideAllPanes();
            header_label.setText("");
            start_pane.setVisible(true);

        } else if (event.getSource() == author_btn) {
            hideAllPanes();
            header_label.setText("O autorze");
            author_pane.setVisible(true);

        } else if (event.getSource() == client_list_btn) {
            clients.clear();
            clients_list.clear();
            try {
                FileInputStream fis = new FileInputStream("clientsData");
                ObjectInputStream ois = new ObjectInputStream(fis);

                clients = (ArrayList) ois.readObject();

                ois.close();
                fis.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
            }
            clients_list.addAll(clients);
            hideAllPanes();
            clientName.setCellValueFactory(new PropertyValueFactory<>("name"));
            clientID.setCellValueFactory(new PropertyValueFactory<>("id"));
            see_clients.setItems(clients_list);
            header_label.setText("Baza klientów");
            clients_list_pane.setVisible(true);
        }
    }

    public void deleteTableData() {
        Product selectedItem = edit_products_table.getSelectionModel().getSelectedItem();
        edit_products_table.getItems().remove(selectedItem);
        products.remove(selectedItem);
        invoice.setProducts(products);
    }

    public void deleteClientData() {
        Client selectedClient = see_clients.getSelectionModel().getSelectedItem();
        see_clients.getItems().remove(selectedClient);
        clients.remove(selectedClient);
        try
        {
            FileOutputStream fos = new FileOutputStream("clientsData");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(clients);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void saveProduct() {
        products_list.add(new Product(product_name.getText(), product_measure.getText(), Integer.parseInt(product_amount.getText()), Float.parseFloat(product_price.getText()), Integer.parseInt(product_tax_value.getValue())));
        invoice.setProducts_list(products_list);
        System.out.println(products_list);
        returnStatus("Dodano!");
    }

    public void saveClientData() {
        if (client_name.getText().isEmpty() || client_name.getText().isEmpty() || client_city.getText().isEmpty() || client_country.getText().isEmpty() || client_nip.getText().isEmpty()) {
            returnStatus("Wystąpił błąd!");
        } else {
            client = new Client(client_name.getText(), client_address.getText(), client_city.getText(), client_country.getText(), client_nip.getText());
            invoice.setClient(client);
            returnStatus("Dane zapisane!");
        }
    }

    public void chooseClientData() {
        client = see_clients.getSelectionModel().getSelectedItem();
        invoice.setClient(client);
        client_name.setText(client.getName());
        client_address.setText(client.getAddress());
        client_city.setText(client.getCity());
        client_country.setText(client.getCountry());
        client_nip.setText(client.getId());
        clients_list_pane.setVisible(false);
        client_pane.setVisible(true);
    }

    public void saveClientOnList() {
        if (client_name.getText().isEmpty() || client_name.getText().isEmpty() || client_city.getText().isEmpty() || client_country.getText().isEmpty() || client_nip.getText().isEmpty()) {
            returnStatus("Wystąpił błąd!");
        } else {
            clients.add(new Client(client_name.getText(), client_address.getText(), client_city.getText(), client_country.getText(), client_nip.getText()));
            try
            {
                FileOutputStream fos = new FileOutputStream("clientsData");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(clients);
                oos.close();
                fos.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
            returnStatus("Zapis udany!");
        }
    }

    public void saveRecipientData() {
        recipient = new Recipient(recipient_name.getText(), recipient_address.getText(), recipient_city.getText(), recipient_country.getText(), recipient_nip.getText());
        invoice.setRecipient(recipient);
        returnStatus("Dane zapisane!");
    }
    public void saveSellerData() {
        seller = new Seller(seller_name.getText(), seller_address.getText(), seller_city.getText(), seller_country.getText(), seller_nip.getText(), seller_bank.getText());
        invoice.setSeller(seller);
        returnStatus("Dane zapisane!");
    }

    public void saveInvoiceData() {
        invoice.setID(invoice_id.getText());
        invoice.setCity(invoice_city.getText());
        invoice.setDate(invoice_date.getValue());
        invoice.setSaleDate(sell_date.getValue());
        invoice.setPaymentDeadline(invoice_payment_time.getText());
        invoice.setPaymentMethod(invoice_payment_method.getValue());
        returnStatus("Dane zapisane!");
    }

    public void clearInvoiceData() {
        invoice.setID(null);
        invoice.setCity(null);
        invoice.setDate(null);
        invoice.setSaleDate(null);
        invoice.setPaymentDeadline(null);
        invoice.setPaymentMethod(null);
        invoice_id.clear();
        invoice_date.setValue(LocalDate.now());
        sell_date.setValue(LocalDate.now());
        returnStatus("Wyczyszczono!");
    }

    public void clearClientData() {
        client_name.clear();
        client_address.clear();
        client_city.clear();
        client_country.clear();
        client_nip.clear();
        returnStatus("Wyczyszczono!");
    }

    public void clearRecipientData() {
        recipient_name.clear();
        recipient_address.clear();
        recipient_city.clear();
        recipient_country.clear();
        recipient_nip.clear();
        returnStatus("Wyczyszczono!");
    }

    public void generateInvoice() {
        try {
            InvoiceToPDF invoiceToPDF = new InvoiceToPDF(invoice);
            invoiceToPDF.generate();
            generate_status.setText("Faktura gotowa!");
            generate_status.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.rgb(178, 252, 251), new CornerRadii(5), null)));
            generate_status.setStyle("-fx-text-fill: black");
            generate_status.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(2)
            );
            visiblePause.setOnFinished(
                    event -> generate_status.setVisible(false)
            );
            visiblePause.play();
        }
        catch(RuntimeException | IOException e) {
            generate_status.setText("Wystąpił błąd!");
            generate_status.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.RED, new CornerRadii(5), null)));
            generate_status.setVisible(true);
            generate_status.setStyle("-fx-text-fill: white");
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(2)
            );
            visiblePause.setOnFinished(
                    event -> generate_status.setVisible(false)
            );
            visiblePause.play();
        }


    }
}
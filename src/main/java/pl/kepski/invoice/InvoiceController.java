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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class InvoiceController {

    Client client = new Client();
    Recipient recipient = new Recipient();
    Seller seller = new Seller();
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Client> clients = new ArrayList<>();

    ObservableList<Product> products_list = FXCollections.observableArrayList(products);
    ObservableList<Client> clients_list = FXCollections.observableArrayList(clients);

    @FXML
    private Label generate_status;

    @FXML
    private Button client_btn;

    @FXML
    private Button seller_btn;

    @FXML
    private Button settings_btn;

    @FXML
    private Label header_label;

    @FXML
    private GridPane client_pane;

    @FXML
    private GridPane seller_pane;

    @FXML
    private GridPane settings_pane;

    @FXML
    private TextField client_address;

    @FXML
    private TextField client_city;

    @FXML
    private TextField client_country;

    @FXML
    private TextField client_name;

    @FXML
    private TextField client_nip;

    @FXML
    private TextField seller_city;

    @FXML
    private TextField seller_country;

    @FXML
    private TextField seller_name;

    @FXML
    private TextField seller_nip;

    @FXML
    private TextField seller_address;

    @FXML
    private TextField seller_bank;

    @FXML
    private TextField invoice_id;

    @FXML
    private TextField invoice_city;

    @FXML
    private DatePicker invoice_date;

    @FXML
    private DatePicker sell_date;

    @FXML
    private Button product_btn;

    @FXML
    private Pane product_pane;

    @FXML
    private TextField product_name;

    @FXML
    private TextField product_measure;

    @FXML
    private TextField product_amount;

    @FXML
    private TextField product_price;

    @FXML
    private ChoiceBox<String> product_tax_value;

    @FXML
    private ChoiceBox<String> invoice_payment_method;

    @FXML
    private Pane add_product_pane;

    @FXML
    private Button prd_add_btn;

    @FXML
    private Button prd_delete_btn;

    @FXML
    private TableView<Product> edit_products_table;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, String> productMeasure;

    @FXML
    private TableColumn<Product, Integer> productAmount;

    @FXML
    private TableColumn<Product, Float> productPrice;

    @FXML
    private TableColumn<Product, Integer> productTax;

    @FXML
    private Pane delete_product_pane;

    @FXML
    private TextField invoice_payment_time;

    @FXML
    private Button client2_add_btn;

    @FXML
    private Pane client_pane2;

    @FXML
    private TextField recipient_name;
    @FXML
    private TextField recipient_city;
    @FXML
    private TextField recipient_address;
    @FXML
    private TextField recipient_country;
    @FXML
    private TextField recipient_nip;
    @FXML
    private Pane start_pane;
    @FXML
    private Button start_btn;
    @FXML
    private Button new_invoice;
    @FXML
    private Button author_btn;
    @FXML
    private Pane author_pane;
    @FXML
    private Pane clients_list_pane;
    @FXML
    private TableView<Client> see_clients;
    @FXML
    private TableColumn<Client, String> clientName;
    @FXML
    private TableColumn<Client, String> clientID;
    @FXML
    private Button client_list_btn;
    String invoiceID;
    String invoiceCity;
    LocalDate invoiceDate;
    LocalDate sellDate;

    @FXML
    public void initialize() {
        product_tax_value.getItems().removeAll(product_tax_value.getItems());
        product_tax_value.getItems().addAll("0", "5", "8", "23");
        product_tax_value.getSelectionModel().select("8");
        invoice_payment_method.getItems().removeAll(invoice_payment_method.getItems());
        invoice_payment_method.getItems().addAll("Gotówka", "Przelew");
        invoice_payment_method.getSelectionModel().select("Gotówka");
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

    public void changeView(ActionEvent event) {
        if (event.getSource() == client_btn || event.getSource() == new_invoice) {
            seller_pane.setVisible(false);
            settings_pane.setVisible(false);
            product_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            author_pane.setVisible(false);
            add_product_pane.setVisible(false);
            delete_product_pane.setVisible(false);
            client_pane2.setVisible(false);
            start_pane.setVisible(false);
            header_label.setText("Dane nabywcy");
            client_pane.setVisible(true);

        } else if (event.getSource() == client2_add_btn) {
            seller_pane.setVisible(false);
            settings_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            product_pane.setVisible(false);
            author_pane.setVisible(false);
            client_pane.setVisible(false);
            add_product_pane.setVisible(false);
            delete_product_pane.setVisible(false);
            start_pane.setVisible(false);
            header_label.setText("Dane odbiorcy");
            client_pane2.setVisible(true);

        } else if (event.getSource() == seller_btn) {
            client_pane.setVisible(false);
            settings_pane.setVisible(false);
            author_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            product_pane.setVisible(false);
            add_product_pane.setVisible(false);
            delete_product_pane.setVisible(false);
            client_pane2.setVisible(false);
            start_pane.setVisible(false);
            header_label.setText("Dane sprzedawcy");
            seller_pane.setVisible(true);

        } else if (event.getSource() == settings_btn) {
            client_pane.setVisible(false);
            seller_pane.setVisible(false);
            product_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            author_pane.setVisible(false);
            add_product_pane.setVisible(false);
            delete_product_pane.setVisible(false);
            client_pane2.setVisible(false);
            start_pane.setVisible(false);
            invoice_date.setValue(LocalDate.now());
            sell_date.setValue(LocalDate.now());
            header_label.setText("Ustawienia");
            settings_pane.setVisible(true);

        } else if (event.getSource() == product_btn) {
            client_pane.setVisible(false);
            seller_pane.setVisible(false);
            author_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            settings_pane.setVisible(false);
            add_product_pane.setVisible(false);
            client_pane2.setVisible(false);
            delete_product_pane.setVisible(false);
            start_pane.setVisible(false);
            header_label.setText("Produkty/usługi");
            product_pane.setVisible(true);

        } else if (event.getSource() == prd_add_btn) {
            client_pane.setVisible(false);
            seller_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            settings_pane.setVisible(false);
            author_pane.setVisible(false);
            product_pane.setVisible(false);
            client_pane2.setVisible(false);
            delete_product_pane.setVisible(false);
            start_pane.setVisible(false);
            header_label.setText("Dodawanie produktu/usługi");
            add_product_pane.setVisible(true);

        } else if (event.getSource() == prd_delete_btn) {
            client_pane.setVisible(false);
            seller_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            settings_pane.setVisible(false);
            product_pane.setVisible(false);
            client_pane2.setVisible(false);
            add_product_pane.setVisible(false);
            start_pane.setVisible(false);
            author_pane.setVisible(false);
            header_label.setText("Usuwanie produktu/usługi");
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            productMeasure.setCellValueFactory(new PropertyValueFactory<>("productMeasure"));
            productAmount.setCellValueFactory(new PropertyValueFactory<>("productAmount"));
            productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
            productTax.setCellValueFactory(new PropertyValueFactory<>("productTaxValue"));
            edit_products_table.setItems(products_list);
            delete_product_pane.setVisible(true);
        } else if (event.getSource() == start_btn) {
            client_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            seller_pane.setVisible(false);
            settings_pane.setVisible(false);
            product_pane.setVisible(false);
            client_pane2.setVisible(false);
            add_product_pane.setVisible(false);
            delete_product_pane.setVisible(false);
            author_pane.setVisible(false);
            header_label.setText("");
            start_pane.setVisible(true);
        } else if (event.getSource() == author_btn) {
            client_pane.setVisible(false);
            clients_list_pane.setVisible(false);
            seller_pane.setVisible(false);
            settings_pane.setVisible(false);
            product_pane.setVisible(false);
            client_pane2.setVisible(false);
            add_product_pane.setVisible(false);
            delete_product_pane.setVisible(false);
            start_pane.setVisible(false);
            header_label.setText("O autorze");
            author_pane.setVisible(true);
        } else if (event.getSource() == client_list_btn) {
            clients.clear();
            clients_list.clear();
            try
            {
                FileInputStream fis = new FileInputStream("clientsData");
                ObjectInputStream ois = new ObjectInputStream(fis);

                clients = (ArrayList) ois.readObject();

                ois.close();
                fis.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
            catch (ClassNotFoundException c)
            {
                System.out.println("Class not found");
                c.printStackTrace();
            }
            clients_list.addAll(clients);
            client_pane.setVisible(false);
            seller_pane.setVisible(false);
            settings_pane.setVisible(false);
            product_pane.setVisible(false);
            client_pane2.setVisible(false);
            add_product_pane.setVisible(false);
            delete_product_pane.setVisible(false);
            start_pane.setVisible(false);
            author_pane.setVisible(false);
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
        products_list.add(new Product(product_name.getText(), product_measure.getText(), Integer.parseInt(product_amount.getText()), Float.parseFloat(product_price.getText()), Integer.parseInt(product_tax_value.getValue().toString())));
        System.out.println(products_list);
        returnStatus("Dodano!");
    }

    public void saveClientData() {
        if (client_name.getText().isEmpty() || client_name.getText().isEmpty() || client_city.getText().isEmpty() || client_country.getText().isEmpty() || client_nip.getText().isEmpty()) {
            returnStatus("Wystąpił błąd!");
        } else {
            client = new Client(client_name.getText(), client_address.getText(), client_city.getText(), client_country.getText(), client_nip.getText());
            returnStatus("Dane zapisane!");
        }
    }

    public void chooseClientData() {
        client = see_clients.getSelectionModel().getSelectedItem();
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
        returnStatus("Dane zapisane!");
    }
    public void saveSellerData() {
        seller = new Seller(seller_name.getText(), seller_address.getText(), seller_city.getText(), seller_country.getText(), seller_nip.getText(), seller_bank.getText());
        returnStatus("Dane zapisane!");
    }

    public void saveInvoiceData() {
        invoiceID = invoice_id.getText();
        invoiceCity = invoice_city.getText();
        invoiceDate = invoice_date.getValue();
        sellDate = sell_date.getValue();
        returnStatus("Dane zapisane!");
    }

    public void clearInvoiceData() {
        invoiceID = null;
        invoiceDate = null;
        sellDate = null;
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
    public void generateInvoice() throws IOException {
        PDDocument invoice = new PDDocument();
        invoice.addPage(new PDPage());
        PDPage page = invoice.getPage(0);
        int pageHeight = (int) page.getTrimBox().getHeight();
        PDFont fontBold = PDType0Font.load(invoice, new File("src/main/resources/pl/kepski/invoice/fonts/lexendbold.ttf"));
        PDFont fontRegular = PDType0Font.load(invoice, new File("src/main/resources/pl/kepski/invoice/fonts/lexendregular.ttf"));
        try (@Deprecated PDPageContentStream contentWriter = new PDPageContentStream(invoice, page, false, false))
        {
            //City and dates
            contentWriter.beginText();
            contentWriter.newLineAtOffset(380,770);
            contentWriter.setLeading(18.5f);
            contentWriter.setFont(fontRegular, 12);
            contentWriter.showText("Miejscowość: " + invoiceCity);
            contentWriter.newLine();
            contentWriter.showText("Data wystawienia: " + invoiceDate);
            contentWriter.newLine();
            contentWriter.showText("Data sprzedaży: " + sellDate);
            contentWriter.endText();


            //Invoice number
            contentWriter.beginText();
            contentWriter.newLineAtOffset(380,705);
            contentWriter.setFont(fontBold, 16);
            contentWriter.showText("FAKTURA NR " + invoiceID);
            contentWriter.endText();

            //Seller data
            contentWriter.addRect(27, 770, 180, 15);
            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,773);
            contentWriter.setFont(fontBold, 12);
            contentWriter.setLeading(14.5f);
            contentWriter.showText("SPRZEDAWCA");
            contentWriter.newLine();
            contentWriter.setFont(fontRegular, 12);
            contentWriter.showText(seller.getName());
            contentWriter.newLine();
            contentWriter.showText(seller.getCity());
            contentWriter.newLine();
            contentWriter.showText(seller.getAddress());
            contentWriter.newLine();
            contentWriter.showText(seller.getCountry());
            contentWriter.newLine();
            contentWriter.showText("NIP " + seller.getId());
            contentWriter.newLine();
            contentWriter.showText("Numer konta " + seller.getBankAccountNumber());
            contentWriter.endText();

            //Client data
            contentWriter.addRect(27, 640, 180, 15);
            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,643);
            contentWriter.setFont(fontBold, 12);
            contentWriter.setLeading(14.5f);
            contentWriter.showText("NABYWCA");
            contentWriter.newLine();
            contentWriter.setFont(fontRegular, 12);
            contentWriter.showText(client.getName());
            contentWriter.newLine();
            contentWriter.showText(client.getAddress());
            contentWriter.newLine();
            contentWriter.showText(client.getCity());
            contentWriter.newLine();
            contentWriter.showText(client.getCountry());
            contentWriter.newLine();
            contentWriter.showText("NIP " + client.getId());
            contentWriter.endText();

            //Recipient data
            if (Recipient.instanceExists) {
                contentWriter.addRect(377, 640, 180, 15);
                contentWriter.beginText();
                contentWriter.newLineAtOffset(380,643);
                contentWriter.setFont(fontBold, 12);
                contentWriter.setLeading(14.5f);
                contentWriter.showText("ODBIORCA");
                contentWriter.newLine();
                contentWriter.setFont(fontRegular, 12);
                contentWriter.showText(recipient.getName());
                contentWriter.newLine();
                contentWriter.showText(recipient.getAddress());
                contentWriter.newLine();
                contentWriter.showText(recipient.getCity());
                contentWriter.newLine();
                contentWriter.showText(recipient.getCountry());
                contentWriter.newLine();
                contentWriter.showText("NIP " + recipient.getId());
                contentWriter.endText();
                recipient = null;
                Recipient.instanceExists = false;

            } else {
                returnStatus("Brak odbiorcy.");
            }


            contentWriter.setStrokingColor(Color.DARK_GRAY);
            contentWriter.setLineWidth(1);

            int initX = 30;
            int initY = pageHeight-240;
            int cellHeight = 30;
            int cellWidth;

            int colCount = 9;

            String[] tableHeaders = new String[] {"LP", "Nazwa towaru/usługi", "Jm.", "Ilość", "Cena  netto", "Kwota netto", "VAT", "Kwota VAT", "Kwota brutto"};
            String[] summaryTableHeaders = new String[] {"", "Kwota netto", "Kwota VAT", "Kwota brutto", "RAZEM"};

            for (int j = 1; j <= colCount; j++) {
                if (j == 2) {
                    cellWidth = 170;
                    contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                } else if (j == 1) {
                    cellWidth = 25;
                    contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                } else {
                    cellWidth = 50;
                    contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                }
                contentWriter.beginText();
                contentWriter.newLineAtOffset(initX+5, initY-cellHeight+18);
                contentWriter.setFont(fontBold, 12);
                if (tableHeaders[j-1].length() > 6) {
                    contentWriter.showText(tableHeaders[j-1].substring(0,6));
                    contentWriter.setLeading(14.5f);
                    contentWriter.newLine();
                    contentWriter.showText(tableHeaders[j-1].substring(6));
                } else {
                    contentWriter.showText(tableHeaders[j-1]);
                }
                contentWriter.endText();
                initX += cellWidth;
            }
            initX = 30;
            initY -= cellHeight;

            for (int i = 1; i <= products_list.size(); i++) {
                for (int j = 1; j <= colCount; j++) {
                    if (j == 2) {
                        cellWidth = 170;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+5, initY-cellHeight+18);
                        contentWriter.setFont(fontRegular, 10);
                        if (products_list.get(i-1).getProductName().length() > 29) {
                            contentWriter.showText(products_list.get(i-1).getProductName().substring(0,29));
                            contentWriter.setLeading(14.5f);
                            contentWriter.newLine();
                            contentWriter.showText(products_list.get(i-1).getProductName().substring(29));
                        } else {
                            contentWriter.showText(products_list.get(i-1).getProductName());
                        }
                        contentWriter.endText();
                    } else if (j == 1) {
                        cellWidth = 25;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(Integer.toString(i));
                        contentWriter.endText();
                    } else if (j == 3) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+15, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(products_list.get(i-1).getProductMeasure());
                        contentWriter.endText();
                    } else if (j == 4) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+20, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(Integer.toString(products_list.get(i-1).getProductAmount()));
                        contentWriter.endText();
                    } else if (j == 5) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", products_list.get(i-1).getProductPriceNoTax()));
                        contentWriter.endText();
                    } else if (j == 6) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", products_list.get(i-1).getProductAllPrice()));
                        contentWriter.endText();
                    } else if (j == 7) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+15, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(products_list.get(i-1).getProductTaxValue()+"%");
                        contentWriter.endText();
                    } else if (j == 8) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", products_list.get(i-1).getProductTax()));
                        contentWriter.endText();
                    } else if (j == 9) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", products_list.get(i-1).getProductFinalPrice()));
                        contentWriter.endText();
                    } else {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                    }

                    initX += cellWidth;
                }
                initX = 30;
                initY -= cellHeight;
            }

            initX = 375;
            for (int i = 1; i <= 1; i++) {
                for (int j = 1; j <= 4; j++) {
                    cellWidth = 50;
                    contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                    contentWriter.beginText();
                    contentWriter.newLineAtOffset(initX+5, initY-cellHeight+18);
                    contentWriter.setFont(fontBold, 12);
                    if (summaryTableHeaders[j-1].length() > 6) {
                        contentWriter.showText(summaryTableHeaders[j-1].substring(0,6));
                        contentWriter.setLeading(14.5f);
                        contentWriter.newLine();
                        contentWriter.showText(summaryTableHeaders[j-1].substring(6));
                    } else {
                        contentWriter.showText(summaryTableHeaders[j-1]);
                    }
                    contentWriter.endText();

                    initX += cellWidth;
                }
                initX = 375;
                initY -= cellHeight;
            }

            float summaryProductAllPrice = 0;
            float summaryProductTax = 0;
            float summaryProductFinalPrice = 0;

            for (Product product : products_list) {
                summaryProductAllPrice = summaryProductAllPrice + product.getProductAllPrice();
                summaryProductTax = summaryProductTax + product.getProductTax();
                summaryProductFinalPrice = summaryProductFinalPrice + product.getProductFinalPrice();
            }

            for (int i = 1; i <= 1; i++) {
                for (int j = 1; j <= 4; j++) {
                    cellWidth = 50;
                    contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                    contentWriter.beginText();
                    contentWriter.setFont(fontBold, 12);
                    if (j == 1) {
                        contentWriter.newLineAtOffset(initX+3, initY-cellHeight+10);
                        contentWriter.showText("RAZEM");
                    } else if (j == 2) {
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", summaryProductAllPrice));
                    } else if (j == 3) {
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", summaryProductTax));
                    } else {
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", summaryProductFinalPrice));
                    }
                    contentWriter.endText();

                    initX += cellWidth;
                }
                initX = 375;
                initY -= cellHeight;
            }

            String numberD = String.valueOf(summaryProductFinalPrice);
            numberD = numberD.substring(numberD.indexOf(".")+1);

            contentWriter.stroke();
            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,initY-30);
            contentWriter.setFont(fontBold, 12);
            contentWriter.setLeading(14.5f);
            contentWriter.showText("DO ZAPŁATY: " + String.format("%.2f", summaryProductFinalPrice) + " PLN");
            contentWriter.newLine();
            contentWriter.setFont(fontRegular, 12);
            contentWriter.showText("Słownie: " + PriceToText.priceToText(summaryProductFinalPrice) + "PLN " + numberD +"/100");
            contentWriter.endText();

            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,initY-70);
            contentWriter.setFont(fontRegular, 12);
            contentWriter.setLeading(14.5f);
            contentWriter.showText("Termin płatności: " + invoice_payment_time.getText());
            contentWriter.newLine();
            contentWriter.showText("Metoda płatności: " + invoice_payment_method.getValue().toString());
            contentWriter.endText();

            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,initY-140);
            contentWriter.setFont(fontRegular, 10);
            contentWriter.setLeading(14.5f);
            contentWriter.showText(".............................................................................");
            contentWriter.newLine();
            contentWriter.showText("Osoba upoważniona do odbioru");
            contentWriter.endText();

            contentWriter.beginText();
            contentWriter.newLineAtOffset(400,initY-140);
            contentWriter.setFont(fontRegular, 10);
            contentWriter.setLeading(14.5f);
            contentWriter.showText(".............................................................................");
            contentWriter.newLine();
            contentWriter.showText("Osoba upoważniona do wystawienia");
            contentWriter.endText();

            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,10);
            contentWriter.setFont(fontRegular, 8);
            contentWriter.showText("Faktura - darmowy program | Autor: Kamil Kępski");
            contentWriter.endText();
        } catch(RuntimeException e) {
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
            throw new RuntimeException(e);
        }

        PDDocumentInformation invoiceInfo = invoice.getDocumentInformation();
        if (invoiceID == null) {
            invoiceInfo.setTitle("Faktura");
            invoice.save("faktura.pdf");
        } else if (invoiceID.contains("/")) {
            invoiceInfo.setTitle("Faktura " + invoiceID);
            invoice.save("faktura"+invoiceID.replace("/", "-")+".pdf");
        } else {
            invoiceInfo.setTitle("Faktura " + invoiceID);
            invoice.save("faktura"+invoiceID+".pdf");
        }
        invoice.close();
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
}
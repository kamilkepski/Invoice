package pl.kepski.invoice.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import pl.kepski.invoice.PriceToText;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Invoice {
    private Seller seller;

    private Client client;

    private Recipient recipient;

    ArrayList<Product> products = new ArrayList<>();

    ObservableList<Product> products_list = FXCollections.observableArrayList(products);

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

    private String ID;
    private String city;
    private LocalDate date;
    private LocalDate saleDate;
    private String paymentDeadline;
    private String paymentMethod;

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

    public Invoice() {}

    public Invoice(String id, String city, LocalDate date, LocalDate saleDate, String paymentDeadline, String paymentMethod) {
        this.ID = id;
        this.city = city;
        this.date = date;
        this.saleDate = saleDate;
        this.paymentDeadline = paymentDeadline;
        this.paymentMethod = paymentMethod;
    }

    public void generate() throws IOException {

        PDDocument document = new PDDocument();
        document.addPage(new PDPage());
        PDPage page = document.getPage(0);

        int pageHeight = (int) page.getTrimBox().getHeight();
        PDFont fontBold = PDType0Font.load(document, new File("src/main/resources/pl/kepski/invoice/fonts/lexendbold.ttf"));
        PDFont fontRegular = PDType0Font.load(document, new File("src/main/resources/pl/kepski/invoice/fonts/lexendregular.ttf"));

        try (@Deprecated PDPageContentStream contentWriter = new PDPageContentStream(document, page, false, false))
        {
            //City and dates
            contentWriter.beginText();
            contentWriter.newLineAtOffset(380,770);
            contentWriter.setLeading(18.5f);
            contentWriter.setFont(fontRegular, 12);
            contentWriter.showText("Miejscowość: " + city);
            contentWriter.newLine();
            contentWriter.showText("Data wystawienia: " + date);
            contentWriter.newLine();
            contentWriter.showText("Data sprzedaży: " + saleDate);
            contentWriter.endText();

            //Invoice number
            contentWriter.beginText();
            contentWriter.newLineAtOffset(380,705);
            contentWriter.setFont(fontBold, 16);
            contentWriter.showText("FAKTURA NR " + ID);
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
                System.out.println("Brak odbiorcy");
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
                if (j == 1) {
                    cellWidth = 25;
                    contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                } else if (j == 2) {
                    cellWidth = 170;
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
            contentWriter.showText("Termin płatności: " + paymentDeadline);
            contentWriter.newLine();
            contentWriter.showText("Metoda płatności: " + paymentMethod);
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
            throw new RuntimeException(e);
        }

        PDDocumentInformation invoiceInfo = document.getDocumentInformation();
        if (ID == null) {
            invoiceInfo.setTitle("Faktura");
            document.save("faktura.pdf");
        } else if (ID.contains("/")) {
            invoiceInfo.setTitle("Faktura " + ID);
            document.save("faktura"+ID.replace("/", "-")+".pdf");
        } else {
            invoiceInfo.setTitle("Faktura " + ID);
            document.save("faktura"+ID+".pdf");
        }
        document.close();
    }
}

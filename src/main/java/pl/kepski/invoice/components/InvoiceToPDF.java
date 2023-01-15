package pl.kepski.invoice.components;

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

public class InvoiceToPDF {

    private final Invoice invoice;

    public InvoiceToPDF(Invoice invoice) {
        this.invoice = invoice;
    }

    private void addTextToInvoice(String text, PDPageContentStream pdPageContentStream) throws IOException {
        pdPageContentStream.showText(text);
        pdPageContentStream.newLine();
    }

    private void addTextToInvoice(String text, String value, PDPageContentStream pdPageContentStream) throws IOException {
        pdPageContentStream.showText(text + " " + value);
        pdPageContentStream.newLine();
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
            addTextToInvoice("Miejscowość:", invoice.getCity(), contentWriter);
            addTextToInvoice("Data wystawienia:", String.valueOf(invoice.getDate()), contentWriter);
            addTextToInvoice("Data sprzedaży:", String.valueOf(invoice.getSaleDate()), contentWriter);
            contentWriter.endText();

            //Invoice number
            contentWriter.beginText();
            contentWriter.newLineAtOffset(380,705);
            contentWriter.setFont(fontBold, 16);
            contentWriter.showText("FAKTURA NR " + invoice.getID());
            contentWriter.endText();

            //Seller data
            contentWriter.addRect(27, 770, 180, 15);
            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,773);
            contentWriter.setFont(fontBold, 12);
            contentWriter.setLeading(14.5f);
            addTextToInvoice("SPRZEDAWCA", contentWriter);
            contentWriter.setFont(fontRegular, 12);
            addTextToInvoice(invoice.getSeller().getName(), contentWriter);
            addTextToInvoice(invoice.getSeller().getCity(), contentWriter);
            addTextToInvoice(invoice.getSeller().getAddress(), contentWriter);
            addTextToInvoice(invoice.getSeller().getCountry(), contentWriter);
            addTextToInvoice("NIP", invoice.getSeller().getId(), contentWriter);
            addTextToInvoice("Numer konta", invoice.getSeller().getBankAccountNumber(), contentWriter);
            contentWriter.endText();

            //Client data
            contentWriter.addRect(27, 640, 180, 15);
            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,643);
            contentWriter.setFont(fontBold, 12);
            contentWriter.setLeading(14.5f);
            addTextToInvoice("NABYWCA", contentWriter);
            contentWriter.setFont(fontRegular, 12);
            addTextToInvoice(invoice.getClient().getName(), contentWriter);
            addTextToInvoice(invoice.getClient().getAddress(), contentWriter);
            addTextToInvoice(invoice.getClient().getCity(), contentWriter);
            addTextToInvoice(invoice.getClient().getCountry(), contentWriter);
            addTextToInvoice("NIP", invoice.getClient().getId(), contentWriter);
            contentWriter.endText();

            //Recipient data
            if (Recipient.instanceExists) {
                contentWriter.addRect(377, 640, 180, 15);
                contentWriter.beginText();
                contentWriter.newLineAtOffset(380,643);
                contentWriter.setFont(fontBold, 12);
                contentWriter.setLeading(14.5f);
                addTextToInvoice("ODBIORCA", contentWriter);
                contentWriter.setFont(fontRegular, 12);
                addTextToInvoice(invoice.getRecipient().getName(), contentWriter);
                addTextToInvoice(invoice.getRecipient().getAddress(), contentWriter);
                addTextToInvoice(invoice.getRecipient().getCity(), contentWriter);
                addTextToInvoice(invoice.getRecipient().getCountry(), contentWriter);
                addTextToInvoice("NIP", invoice.getRecipient().getId(), contentWriter);
                contentWriter.endText();
                invoice.setRecipient(null);
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

            for (int i = 1; i <= invoice.getProducts_list().size(); i++) {
                for (int j = 1; j <= colCount; j++) {
                    if (j == 2) {
                        cellWidth = 170;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+5, initY-cellHeight+18);
                        contentWriter.setFont(fontRegular, 10);
                        if (invoice.getProducts_list().get(i-1).getProductName().length() > 29) {
                            contentWriter.showText(invoice.getProducts_list().get(i-1).getProductName().substring(0,29));
                            contentWriter.setLeading(14.5f);
                            contentWriter.newLine();
                            contentWriter.showText(invoice.getProducts_list().get(i-1).getProductName().substring(29));
                        } else {
                            contentWriter.showText(invoice.getProducts_list().get(i-1).getProductName());
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
                        contentWriter.showText(invoice.getProducts_list().get(i-1).getProductMeasure());
                        contentWriter.endText();
                    } else if (j == 4) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+20, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(Integer.toString(invoice.getProducts_list().get(i-1).getProductAmount()));
                        contentWriter.endText();
                    } else if (j == 5) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", invoice.getProducts_list().get(i-1).getProductPriceNoTax()));
                        contentWriter.endText();
                    } else if (j == 6) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", invoice.getProducts_list().get(i-1).getProductAllPrice()));
                        contentWriter.endText();
                    } else if (j == 7) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+15, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(invoice.getProducts_list().get(i-1).getProductTaxValue()+"%");
                        contentWriter.endText();
                    } else if (j == 8) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", invoice.getProducts_list().get(i-1).getProductTax()));
                        contentWriter.endText();
                    } else if (j == 9) {
                        cellWidth = 50;
                        contentWriter.addRect(initX, initY, cellWidth, -cellHeight);
                        contentWriter.beginText();
                        contentWriter.newLineAtOffset(initX+10, initY-cellHeight+10);
                        contentWriter.setFont(fontRegular, 10);
                        contentWriter.showText(String.format("%.2f", invoice.getProducts_list().get(i-1).getProductFinalPrice()));
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

            for (Product product : invoice.getProducts_list()) {
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
            addTextToInvoice("Termin płatności:", invoice.getPaymentDeadline(), contentWriter);
            addTextToInvoice("Metoda płatności:", invoice.getPaymentMethod(), contentWriter);
            contentWriter.endText();

            contentWriter.beginText();
            contentWriter.newLineAtOffset(30,initY-140);
            contentWriter.setFont(fontRegular, 10);
            contentWriter.setLeading(14.5f);
            addTextToInvoice(".............................................................................", contentWriter);
            addTextToInvoice("Osoba upoważniona do odbioru", contentWriter);
            contentWriter.endText();

            contentWriter.beginText();
            contentWriter.newLineAtOffset(400,initY-140);
            contentWriter.setFont(fontRegular, 10);
            contentWriter.setLeading(14.5f);
            addTextToInvoice(".............................................................................", contentWriter);
            addTextToInvoice("Osoba upoważniona do wystawienia", contentWriter);
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
        if (invoice.getID() == null) {
            invoiceInfo.setTitle("Faktura");
            document.save("faktura.pdf");
        } else if (invoice.getID().contains("/")) {
            invoiceInfo.setTitle("Faktura " + invoice.getID());
            document.save("faktura"+invoice.getID().replace("/", "-")+".pdf");
        } else {
            invoiceInfo.setTitle("Faktura " + invoice.getID());
            document.save("faktura"+invoice.getID()+".pdf");
        }
        document.close();

    }
}

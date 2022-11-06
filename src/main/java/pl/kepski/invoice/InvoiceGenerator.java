package pl.kepski.invoice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InvoiceGenerator extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InvoiceGenerator.class.getResource("primary-view.fxml"));
        InputStream stream = new FileInputStream("src/main/resources/pl/kepski/invoice/icon.png");
        Image image = new Image(stream);
        stage.getIcons().add(image);
        Scene scene = new Scene(fxmlLoader.load(), 1100, 700);
        stage.setTitle("Faktura - darmowy program");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
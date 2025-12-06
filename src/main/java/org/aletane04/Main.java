package org.aletane04;

import org.aletane04.controller.*;
import org.aletane04.data.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;


public class Main extends Application 
{

    @Override
    public void start(Stage stage) throws Exception 
    {
        Biblioteca myBiblioteca = new Biblioteca();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        Parent root = loader.load();
        
        /* Qui passo il mio oggetto Biblioteca al controller */
        MainController myMainController = loader.getController();
        myMainController.setBiblioteca(myBiblioteca); 
       
        /* Creo la scena e la mostro in risoluzione HD e formato 16:9 */
        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Gestione Biblioteca - G16");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            // Blocco la chiusura immediata di JavaFX
            event.consume();
            // Chiedo conferma con il metodo semplice scritto sopra
            myMainController.onEsciClick();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
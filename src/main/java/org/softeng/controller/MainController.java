/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.softeng.controller;

import javafx.application.Platform;
import javafx.fxml.*;
import java.net.*;
import java.util.*;
import javafx.scene.control.*;
import org.softeng.data.*;

/**
 * FXML Controller class
 *
 * @author 39392
 */
public class MainController implements Initializable 
{

    @FXML private LibriController libriViewController;
    @FXML private UtentiController utentiViewController;
    @FXML private PrestitiController prestitiViewController;

    private Biblioteca myBiblioteca;

     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        /* Vuoto, non è  necessario fare set-up grafico generale */
    }

    public void setBiblioteca(Biblioteca myBiblioteca) {
        this.myBiblioteca = myBiblioteca;
        /* Passo il mio oggetto "manager" agli altri controller */
        if (libriViewController != null)
            libriViewController.setBiblioteca(myBiblioteca);
        if (utentiViewController != null)
            utentiViewController.setBiblioteca(myBiblioteca);
        if (prestitiViewController != null)
            prestitiViewController.setBiblioteca(myBiblioteca);
    }

    @FXML public void onSalvaClick() 
    {
        myBiblioteca.saveAll();
        // Popup Semplice
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Salvataggio");
        alert.setHeaderText(null);
        alert.setContentText("Salvataggio effettuato con successo!");
        alert.showAndWait();
    }
    @FXML public void onEsciClick() 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Uscita");
        alert.setHeaderText("Sei sicuro di voler uscire?");
        alert.setContentText("I dati non salvati andranno persi.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            Platform.exit();
            System.exit(0);
        }
    }
    
}

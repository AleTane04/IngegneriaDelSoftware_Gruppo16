/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.controller;

import javafx.application.Platform;
import javafx.fxml.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.aletane04.data.*;

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
    }
    @FXML public void onEsciClick() 
    {
        myBiblioteca.saveAll();
        Platform.exit(); 
    }
    
}

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
    
    /**
    * @brief Metodo di inizializzazione del controller.
    *
    * Questo metodo implementa l'interfaccia Initializable di JavaFX.
    * Viene chiamato dal FXMLLoader dopo che tutti gli elementi FXML
    * sono stati caricati e iniettati.
    *
    * @pre Tutti gli elementi con l'annotazione @FXML devono essere stati iniettati con successo.
    * @post Nessuna modifica visibile all'interfaccia utente. Il controller è pronto per l'associazione con il modello (manager).
    *
    * @param[in] location La posizione relativa dell'oggetto radice.
    * @param[in] resources Le risorse utilizzate per la localizzazione dell'oggetto radice.
    * 
    */
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        /// Vuoto, non è  necessario fare set-up grafico generale 
    }

    /**
    * @brief Imposta il gestore della biblioteca (modello) e lo propaga a tutti i controller delle sottoviste.
    *
    * Questo metodo stabilisce il legame tra il controller principale e l'istanza di
    * Biblioteca che funge da manager dei dati. Successivamente, si occupa
    * di chiamare il metodo setBiblioteca su ciascuno dei controller
    * delle sottoviste (Utenti, Libri, Prestiti), passando la stessa istanza
    * del manager per inizializzare il loro accesso ai dati.
    *
    * @pre I controller delle sottoviste (libriViewController, utentiViewController,
    * prestitiViewController) devono essere stati iniettati (anche se possono essere nulli).
    * @post L'istanza di Biblioteca è memorizzata nel campo myBiblioteca di questo
    * controller e, se non sono nulli, viene propagata a tutti i controller delle sottoviste.
    *
    * @param[in] myBiblioteca L'istanza di Biblioteca che contiene i dati e la logica di business.
    *
    * 
    */
    public void setBiblioteca(Biblioteca myBiblioteca) {
        this.myBiblioteca = myBiblioteca;
        /// Passo il mio oggetto "manager" agli altri controller 
        if (libriViewController != null)
            libriViewController.setBiblioteca(myBiblioteca);
        if (utentiViewController != null)
            utentiViewController.setBiblioteca(myBiblioteca);
        if (prestitiViewController != null)
            prestitiViewController.setBiblioteca(myBiblioteca);
    }
    
    
    /**
    * @brief Gestisce l'evento di salvataggio manuale dei dati.
    *
    * Questa funzione è un gestore di eventi FXML.
    * Chiama il metodo saveAll() sull'istanza del
    * gestore (modello) myBiblioteca per rendere permanenti su file tutte
    * le modifiche apportate ai dati (Utenti, Libri, Prestiti).
    *
    * @pre Il manager myBiblioteca deve essere stato inizializzato tramite
    * setBiblioteca() e deve contenere i dati correnti del sistema.
    * @post Tutte le collezioni di dati gestite da myBiblioteca (es. Utenti,
    * Libri, Prestiti) sono state scritte e persistite su un mezzo di memorizzazione
    * (tipicamente un file CSV o simile).
    *
    * 
    */
    @FXML 
    public void onSalvaClick() 
    {
        myBiblioteca.saveAll();
        /// Popup Semplice
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Salvataggio");
        alert.setHeaderText(null);
        alert.setContentText("Salvataggio effettuato con successo!");
        alert.showAndWait(); 
    }
    
    
     /**
     * @brief Gestisce l'evento di uscita dall'applicazione richiedendo una conferma all'utente.
     *
     * Questa funzione gestisce eventi FXML. Esegue due operazioni:
     * 1. Visualizza un dialogo di conferma (Alert di tipo CONFIRMATION) all'utente.
     * 2. Se l'utente conferma l'uscita (premendo OK), l'applicazione viene terminata 
     * chiamando Platform.exit() e poi System.exit(0).
     *
     * @pre L'applicazione deve essere in esecuzione.
     * @post L'applicazione JavaFX viene chiusa solo se l'utente conferma; altrimenti, rimane in esecuzione.
     *
     */
    @FXML 
    public void onEsciClick() 
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

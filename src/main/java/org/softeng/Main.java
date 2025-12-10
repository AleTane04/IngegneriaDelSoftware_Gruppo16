/**
 * @file Main.java
 * @brief Classe principale dell'applicazione "Gestione Biblioteca".
 * @author 39392
 * @date 05 Dicembre 2025
 * @version 1.0
 */
package org.softeng;

import org.softeng.controller.*;
import org.softeng.data.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

/**
 * @brief Classe di avvio dell'app JavaFX.
 * * Questa classe estende "Application" ed è responsabile della configurazione dello Stage primario,
 * del caricamento dell'interfaccia grafica FXML e dell'inizializzazione della Biblioteca, ovvero del
 * "Model" nel pattern architetturale MVC.
 */
public class Main extends Application 
{
    /**
     * @brief Metodo di avvio (e punto di "entrata", o "entry point") dell'applicativo JavaFX.
     * * Questo metodo viene chiamato automaticamente dal runtime JavaFX dopo l'inizializzazione del sistema.
     * Si occupa di creare il manager dei dati, caricare la vista principale, collegare il controller
     * e mostrare la finestra all'utente.
     * * @param[in] stage Lo Stage primario (finestra) fornito dalla piattaforma JavaFX.
     * @throws Exception Se il file FXML non viene trovato o se si verificano errori durante il caricamento.
     * * @pre Il file "/fxml/MainView.fxml" deve essere presente nel percorso delle risorse.
     * @pre La classe Launcher deve aver correttamente inizializzato le dipendenze JavaFX.
     * * @post Viene istanziato un oggetto Biblioteca (Model nel pattern architetturale MVC).
     * @post Il MainController ha ricevuto l'istanza di Biblioteca.
     * @post La finestra dell'applicazione è visibile con risoluzione 1280x720 e titolo impostato.
     * @post È configurato un gestore per la richiesta di chiusura della finestra (X) che delega al controller.
     */
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
            /* Blocco la chiusura immediata di JavaFX */
            event.consume();
            /* Chiedo conferma all'utente (nel caso prema la X per uscire dall'applicativo) */
            myMainController.onEsciClick();
        });
        stage.show();
    }
    /**
     * @brief Metodo main standard di Java.
     * * Funge da punto di ingresso per l'avvio dell'applicativo, invocando il metodo launch() di JavaFX.
     * * @param[in] args Argomenti da riga di comando passati all'avvio.
     * * @post Il ciclo di vita dell'applicativo JavaFX viene avviato.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

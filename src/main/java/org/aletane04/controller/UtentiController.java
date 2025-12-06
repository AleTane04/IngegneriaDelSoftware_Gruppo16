/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.aletane04.data.Biblioteca;
import org.aletane04.model.Utente;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UtentiController implements Initializable {

    /* Grafica - View */
    @FXML private TableView<Utente> tabellaUtenti;
    
    /* Colonne della tabella */
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colEmail;

    /* Campi di input e ricerca */
    @FXML private TextField txtNome;
    @FXML private TextField txtCognome;
    @FXML private TextField txtMatricola;
    @FXML private TextField txtEmail;
    @FXML private TextField txtRicerca;

   /* Riferimento al manager */
    private Biblioteca manager;

    /**
     * Questo metodo è chiamato automaticamente da JavaFX appena caricato il file FXML.
     * Necessario per configurare l'aspetto grafico (colonne).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Colleghiamo le colonne agli attributi della classe Utente.
        // La stringa ("nome") deve corrispondere al metodo getNome() nel model.
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Opzionale: Messaggio se la tabella è vuota
        tabellaUtenti.setPlaceholder(new Label("Nessun utente presente in archivio."));

    // Questo fa sì che le colonne si allighino per riempire tutto lo spazio
        tabellaUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        /* Modifica campi Utente con doppio click sulla cella */
        tabellaUtenti.setEditable(true);

        colNome.setCellFactory(TextFieldTableCell.forTableColumn());
        colNome.setOnEditCommit(event -> {
            Utente u = event.getRowValue();
            u.setNome(event.getNewValue());
        });

        colCognome.setCellFactory(TextFieldTableCell.forTableColumn());
        colCognome.setOnEditCommit(event -> {
            Utente u = event.getRowValue();
            u.setCognome(event.getNewValue());
        });

        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            Utente u = event.getRowValue();
            u.setEmail(event.getNewValue());
        });

        colMatricola.setEditable(false);

    }

    /**
     * Metodo chiamato dal MainController per passare i dati.
     * Qui avviene il collegamento logico vero e proprio.
     */
    public void setBiblioteca(Biblioteca manager) {
        this.manager = manager;

        // --- GESTIONE RICERCA E ORDINAMENTO ---
        
        // 1. Si avvolge la lista originale in una FilteredList (inizialmente mostra tutto)
        FilteredList<Utente> filteredData = new FilteredList<>(manager.getUtenti(), p -> true);

        // 2. Si aggiunge un listener alla barra di ricerca
        // Ogni volta che scrivo una lettera, il filtro si aggiorna
        txtRicerca.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(utente -> {
                // Se la barra è vuota, mostra tutti
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Confronto (case-insensitive) con Nome, Cognome o Matricola
                String lowerCaseFilter = newValue.toLowerCase();

                if (utente.getCognome().toLowerCase().contains(lowerCaseFilter)) return true;
                if (utente.getNome().toLowerCase().contains(lowerCaseFilter)) return true;
                if (utente.getMatricola().toLowerCase().contains(lowerCaseFilter)) return true;
                /* Se non trovato */
                return false;
            });
        });

        // 3. Avvolgo la FilteredList in una SortedList (per ordinare cliccando le colonne)
        SortedList<Utente> sortedData = new SortedList<>(filteredData);

        // 4. Collego il comparatore della tabella alla lista ordinata
        sortedData.comparatorProperty().bind(tabellaUtenti.comparatorProperty());

        // 5. Metto i dati finali nella tabella
        tabellaUtenti.setItems(sortedData);
    }

    /**
     * Gestisce il click sul bottone "Aggiungi".
     */
    @FXML
    public void onAggiungi() {
       /* Lettura dei dati */
        String nome = txtNome.getText();
        String cognome = txtCognome.getText();
        String matricola = txtMatricola.getText();
        String email = txtEmail.getText();

        /* Validazione di quanto letto */
        if (matricola.isEmpty() || cognome.isEmpty() || nome.isEmpty()) {
            mostraAlert(Alert.AlertType.WARNING, "Dati mancanti", "Nome, Cognome e Matricola sono obbligatori.");
            return;
        }

        /* Creazione di un nuovo utente */
        Utente nuovoUtente = new Utente(nome, cognome, matricola, email);
        
       /* Aggiunta utente: il manager gestisce i duplicati e il salvataggio su file .csv */
        manager.aggiungiUtente(nuovoUtente);

        /* Pulizia dei campi */
        pulisciCampi();
    }
    
    /**
     * Eliminare un utente selezionato
     */
    @FXML
    public void onElimina() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        
        if (selezionato != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma eliminazione");
            alert.setContentText("Vuoi davvero eliminare " + selezionato.getCognome() + "?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                /* Il manager gestisce automaticamente il salvataggio del file .csv dopo la rimozione dell'utente */

                manager.getUtenti().remove(selezionato);
                manager.saveAll(); 
            }
        } else {
            mostraAlert(Alert.AlertType.INFORMATION, "Nessuna selezione", "Seleziona un utente dalla tabella per eliminarlo.");
        }
    }

    // --- Metodi Helper privati ---

    private void pulisciCampi() {
        txtNome.clear();
        txtCognome.clear();
        txtMatricola.clear();
        txtEmail.clear();
    }

    private void mostraAlert(Alert.AlertType tipo, String titolo, String contenuto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
}

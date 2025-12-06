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
import org.aletane04.exceptions.UtenteGiaPresenteException;
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
        /* Collego le colonne agli attributi della classe Utente */
        /* nome -> getNome; cognome -> getCognome; In generale: xxx -> getXxx */
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        /* Se la tabella è vuota, viene mostrato questo messaggio "di servizio" */
        tabellaUtenti.setPlaceholder(new Label("Nessun utente presente in archivio."));

        /* Le colonne si adattano per occupare tutto lo spazio disponibile */
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
        
        /* Si avvolge la lista originale in una FilteredList, che inizialmente mostra tutto */
        FilteredList<Utente> filteredData = new FilteredList<>(manager.getUtenti(), p -> true);

        /* Listener per la barra di ricerca */
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

        /* Pattern Decorator: avvolgo la FilteredList in una SortedList, in modo tale da riordinare quando clicco sulle colonne */
        SortedList<Utente> sortedData = new SortedList<>(filteredData);

        /* Collego il comparatore della tabella alla lista ordinata */
        sortedData.comparatorProperty().bind(tabellaUtenti.comparatorProperty());

        /* Inserimento dei dati finali nella tabella */
        tabellaUtenti.setItems(sortedData);
    }

    /**
     * Gestisce il click sul bottone "Aggiungi".
     */

    @FXML
    public void onAggiungi() {
        // 1. Leggo i dati
        String nome = txtNome.getText();
        String cognome = txtCognome.getText();
        String matricola = txtMatricola.getText();
        String email = txtEmail.getText();

        // 2. Validazione base
        if (matricola.isEmpty() || cognome.isEmpty() || nome.isEmpty()) {
            mostraAlert(Alert.AlertType.WARNING, "Dati mancanti", "Nome, Cognome e Matricola sono obbligatori.");
            return;
        }

        try {
            // 3. Creo e aggiungo
            Utente nuovoUtente = new Utente(nome, cognome, matricola, email);
            manager.aggiungiUtente(nuovoUtente);

            // 4. Successo
            mostraAlert(Alert.AlertType.INFORMATION, "Successo", "Utente aggiunto correttamente.");

            // 5. Pulisco usando il tuo metodo
            pulisciCampi();

        } catch(UtenteGiaPresenteException e)
        {
            mostraAlert(Alert.AlertType.ERROR, "Errore inserimento nuovo Utente", e.getMessage());
        }
        catch (Exception e)
        {
            // Gestione di eventuali errori imprevisti
            mostraAlert(Alert.AlertType.ERROR, "Errore", "Impossibile aggiungere l'utente.");
        }
    }
    
    /**
     * Eliminare un utente selezionato
     */
    @FXML
    public void onElimina() {
        // 1. Recupero la selezione
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();

        if (selezionato == null) {
            // Uso il tuo metodo helper
            mostraAlert(Alert.AlertType.WARNING, "Attenzione", "Seleziona un utente dalla tabella per eliminarlo.");
            return;
        }

        // 2. Chiedo conferma (Questo DEVE essere fatto a mano per leggere la risposta)
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione");
        conferma.setHeaderText(null);
        conferma.setContentText("Vuoi davvero eliminare l'utente " + selezionato.getCognome() + "?");

        // Mostro e aspetto
        Optional<ButtonType> result = conferma.showAndWait();

        // 3. Se conferma, procedo
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Chiamo il metodo SICURO del manager
                manager.rimuoviUtente(selezionato);

                // Successo: Uso il tuo metodo con icona INFORMATION
                mostraAlert(Alert.AlertType.INFORMATION, "Operazione completata", "Utente rimosso con successo.");

            } catch (Exception e) {
                // Errore (es. Utente ha libri): Uso il tuo metodo con icona ERROR
                mostraAlert(Alert.AlertType.ERROR, "Errore di rimozione", e.getMessage());
            }
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

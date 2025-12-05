package org.aletane04.controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.aletane04.data.Biblioteca;
import org.aletane04.model.Libro;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LibriController implements Initializable {

    @FXML private TableView<Libro> tabellaLibri;
    @FXML private TableColumn<Libro, String> colTitolo, colAutore, colIsbn;
    @FXML private TableColumn<Libro, Integer> colCopie;
    @FXML private TableColumn<Libro, LocalDate> colAnno;
    @FXML private TextField txtTitolo, txtAutore, txtIsbn, txtCopie, txtRicerca;
    @FXML private DatePicker pickerAnno;

    private Biblioteca manager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. SETUP GRAFICO (Colonne) - Lo faccio subito!
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("codiceISBN"));
        colCopie.setCellValueFactory(new PropertyValueFactory<>("numeroCopieDisponibili"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        
        // RISOLUZIONE PROBLEMA: Impostiamo la policy via codice Java
        // Questo fa sì che le colonne si allighino per riempire tutto lo spazio
        tabellaLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void setBiblioteca(Biblioteca manager) 
    {
        this.manager = manager;

        // 2. SETUP DATI - Lo faccio solo quando arriva il manager
        FilteredList<Libro> filteredData = new FilteredList<>(manager.getLibri(), b -> true);

        txtRicerca.textProperty().addListener((obs, oldV, newV) -> {
            filteredData.setPredicate(libro -> {
                if (newV == null || newV.isEmpty()) return true;
                String lower = newV.toLowerCase();
                return libro.getTitolo().toLowerCase().contains(lower) ||
                       libro.getAutori().toLowerCase().contains(lower) ||
                       libro.getCodiceISBN().toLowerCase().contains(lower);
            });
        });

        SortedList<Libro> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabellaLibri.comparatorProperty());
        tabellaLibri.setItems(sortedData);
    }

    @FXML
    public void onAggiungi() {
        try {
            String titolo = txtTitolo.getText();
            String autore = txtAutore.getText();
            String isbn = txtIsbn.getText();
            int copie = Integer.parseInt(txtCopie.getText());
            LocalDate data = pickerAnno.getValue() != null ? pickerAnno.getValue() : LocalDate.now();

            if (titolo.isEmpty() || isbn.isEmpty()) 
            {
                mostraErrore("Campi obbligatori mancanti!");
                return;
            }

            manager.aggiungiLibro(new Libro(titolo, autore, data, isbn, copie));
            pulisciCampi();

        } catch (NumberFormatException e) {
            mostraErrore("Il numero copie deve essere un intero!");
        }
    }
    @FXML
public void onRimuovi() {
    // 1. Capisco quale riga l'utente ha selezionato
    Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();

    // 2. Se non ha selezionato nulla, lo sgrido gentilmente
    if (selezionato == null) {
        mostraErrore("Seleziona prima un libro dalla tabella!");
        return;
    }

    // 3. Provo a cancellare
    try {
        manager.rimuoviLibro(selezionato);
        
        // Se arrivo qui, non c'è stata eccezione -> Successo!
        mostraInfo("Libro eliminato con successo.");
        
    } catch (Exception e) {
        // Se il manager lancia l'eccezione (libro in prestito), la catturo qui
        mostraErrore(e.getMessage());
    }
}

    private void pulisciCampi() {
        txtTitolo.clear(); txtAutore.clear(); txtIsbn.clear(); txtCopie.clear();
        pickerAnno.setValue(null);
    }

    private void mostraErrore(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
    private void mostraInfo(String msg) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
    alert.showAndWait();
}
}
package org.aletane04.controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.aletane04.data.Biblioteca;
import org.aletane04.exceptions.LibroGiaPresenteException;
import org.aletane04.model.Libro;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

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
        /* Setup grafico delle colonne */
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("codiceISBN"));
        colCopie.setCellValueFactory(new PropertyValueFactory<>("numeroCopieDisponibili"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));

        tabellaLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /* La tabella diventa modificabile */
        tabellaLibri.setEditable(true);

        colTitolo.setCellFactory(TextFieldTableCell.forTableColumn());
        colTitolo.setOnEditCommit(event -> {
            /* Salvo il riferimento del libro il cui titolo è stato modificato */
            Libro libro = event.getRowValue();
            /* Aggiorno il valore del campo Titolo */
            libro.setTitolo(event.getNewValue());
        });

        colAutore.setCellFactory(TextFieldTableCell.forTableColumn());
        colAutore.setOnEditCommit(event -> {
            Libro libro = event.getRowValue();
            libro.setAutori(event.getNewValue());
        });


        colCopie.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCopie.setOnEditCommit(event -> {
            Libro libro = event.getRowValue();
            libro.setNumeroCopieDisponibili(event.getNewValue());
        });

        colAnno.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        colAnno.setOnEditCommit(event -> {
            Libro libro = event.getRowValue();
            libro.setAnnoPubblicazione(event.getNewValue());
        });
        /* ISBN NON MODIFICABILE */
        colIsbn.setEditable(false);

        pickerAnno.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

               /* Verifico che la data inseria sia successiva a quella del giorno odierno */
                if (date.isAfter(LocalDate.now())) {
                    /* Disabilito la selezione della data */
                    setDisable(true);

                }
            }
        });
        /* Rendo il campo non editabile manualmente(via tastiera) */
        pickerAnno.setEditable(false);
    }

    public void setBiblioteca(Biblioteca manager) 
    {
        this.manager = manager;

        /* Setup dei dati, una volta ricevuto il manager */
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


            if (data.isAfter(LocalDate.now())) {
                mostraErrore("La data di pubblicazione non può essere nel futuro!");
                return;
            }
            if (titolo.isEmpty() || isbn.isEmpty()) 
            {
                mostraErrore("Campi obbligatori mancanti!");
                return;
            }

            manager.aggiungiLibro(new Libro(titolo, autore, data, isbn, copie));
            mostraSuccesso("Libro aggiunto correttamente!");
            pulisciCampi();

        }
            catch (NumberFormatException e)
            {
                mostraErrore("Il numero copie deve essere un intero!");
            }
            catch (LibroGiaPresenteException e)
            {
                mostraErrore(e.getMessage());
            }
            catch(Exception e)
            {
                mostraErrore("Errore imprevisto: " + e.getMessage());
            }
    }
    @FXML
public void onRimuovi() {
   /* Riga selezionata dall'utente */
    Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();

    /* Se l'utente non ha selezionato alcuna riga, viene avvisato */
    if (selezionato == null) {
        mostraErrore("Seleziona prima un libro dalla tabella!");
        return;
    }

    /* Provo a cancellare il libro */
    try {
        manager.rimuoviLibro(selezionato);
        
        // Se arrivo qui, non c'è stata eccezione -> Successo!
        mostraSuccesso("Libro eliminato con successo.");
        
    } catch (Exception e) {
        // Se il manager lancia l'eccezione (libro in prestito), la catturo qui
        mostraErrore(e.getMessage());
    }
}

    private void pulisciCampi()
    {
        txtTitolo.clear(); txtAutore.clear(); txtIsbn.clear(); txtCopie.clear();
        pickerAnno.setValue(null);
    }

    /* Metodi Helper */

    /**
     * Mostra un messaggio di successo (Icona Blu/Informazione)
     */
    private void mostraSuccesso(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operazione Completata");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Mostra un messaggio di errore (Icona Rossa)
     */
    private void mostraErrore(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText("Si è verificato un problema");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.softeng.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.softeng.data.Biblioteca;
import org.softeng.model.*;
import org.softeng.exceptions.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class PrestitiController implements Initializable {

    @FXML private TableView<Prestito> tabellaPrestiti;
    @FXML private TableColumn<Prestito, String> colUtente, colLibro, colStato, colRestituzione;
    @FXML private TableColumn<Prestito, LocalDate> colFine;
    @FXML private ComboBox<Utente> comboUtenti;
    @FXML private ComboBox<Libro> comboLibri;
    @FXML private DatePicker dateFine;


    private Biblioteca manager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* SetUp grafico */
        colUtente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUtente().toString()));
        colLibro.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLibro().getTitolo()));
        colFine.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dataFine"));
        colStato.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatoPrestito().toString()));
        colRestituzione.setCellValueFactory(c ->
        {
            LocalDate dataRientro = c.getValue().getDataRestituzioneEffettiva();
            if (dataRientro == null)
            {
                /* Se non è ancora tornato dal prestito, la cella è vuota */
                return new SimpleStringProperty("");
            }
                else
            {
                /* Se il testo è tornato, mostro la data */
                return new SimpleStringProperty(dataRientro.toString());
            }
        });

       /* Colorazione delle righe */
        tabellaPrestiti.setRowFactory(tv -> new TableRow<Prestito>() {
            @Override
            protected void updateItem(Prestito p, boolean empty) {
                super.updateItem(p, empty);
                /* Se la riga è vuota, pulisco lo stile */
                if (p == null || empty)
                {
                    setStyle("");
                } else {
                    /* Se la riga è selezionata, i colori personalizzati non agiranno */
                    if (isSelected()) {
                        setStyle("");
                    }
                    /* Se la riga non è selezionata, si applicano le personalizzazioni */
                    else {
                        switch (p.getStatoPrestito()) {
                            case SCADUTO:
                                setStyle("-fx-background-color: #ffcccc;"); /* Colore rosso chiaro */
                                break;
                            case IN_SCADENZA:
                                setStyle("-fx-background-color: #ffffcc;"); /* Colore giallo chiaro */
                                break;
                            case ATTIVO:
                                setStyle(""); /* Colore biando: di default */
                                break;
                            case RESTITUITO:
                                setStyle("-fx-background-color: #ccffcc;"); /* Colore verde chiaro */
                                break;
                        }
                    }
                }
            }


            /* Questo metodo si aziona quando l'utente clicca sulla riga.
            e forza il ricalcolo dello stile (chiamando updateItem) per applicare il blu o il rosso. */
            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                updateItem(getItem(), isEmpty());
            }
        });
        dateFine.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                /* Se la data precede quella del giorno odierno */
                if (date.isBefore(LocalDate.now())) {
                    /* La disabilito */
                    setDisable(true);

                }
            }
        });
        dateFine.setEditable(false);
        tabellaPrestiti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        /* Deselezionare premendo ESC */
        tabellaPrestiti.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                tabellaPrestiti.getSelectionModel().clearSelection();
            }
        });

        /* Deselezionare cliccando su uno spazio vuoto */
        tabellaPrestiti.setOnMouseClicked(event -> {
            /* Viene verificato che il click è avvenuto su uno spazio vacuo */
            if (event.getTarget() instanceof javafx.scene.Node) {
                javafx.scene.Node nodo = (javafx.scene.Node) event.getTarget();

                /* Risalita della gerarchia */
                while (nodo != null && nodo != tabellaPrestiti) {
                    if (nodo instanceof TableRow && ((TableRow) nodo).getItem() != null) {
                        return; /* Riga valida -> esco senza far nulla */
                    }
                    nodo = nodo.getParent();
                }

                /* È stato cliccato fuori dalle righe -> si procede con la pulizia della selezione */
                tabellaPrestiti.getSelectionModel().clearSelection();
            }
        });


    }

    public void setBiblioteca(Biblioteca manager) {
        this.manager = manager;

        /* SetUp dei dati */
        // In questo metodo, vengono popolate le ComboBox e la Tabella solo ora che si hanno le liste.
        comboUtenti.setItems(manager.getUtenti());
        comboLibri.setItems(manager.getLibri());
        tabellaPrestiti.setItems(manager.getPrestiti());
    }

    @FXML
    public void onRegistra() {
        try {
            Utente u = comboUtenti.getValue();
            Libro l = comboLibri.getValue();
            LocalDate fine = dateFine.getValue();

            if (u == null || l == null || fine == null) {
                mostraMsg("Attenzione", "Seleziona Utente, Libro e Data!");
                return;
            }

            if (fine.isBefore(LocalDate.now()))
            {
                mostraMsg("Data non valida", "La data di restituzione non può essere nel passato!");
                return;
            }

            manager.registraPrestito(u, l, fine);
            comboUtenti.getSelectionModel().clearSelection();
            comboLibri.getSelectionModel().clearSelection();

        } catch (LibroNonDisponibileException | LimitePrestitiSuperatoException e) {
            mostraMsg("Impossibile procedere", e.getMessage());
        }
    }

    @FXML
    public void onRestituisci() {
        Prestito p = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (p != null)
        {
            if (p.getStatoPrestito() == StatoPrestito.RESTITUITO)
            {
                mostraMsg("Operazione non necessaria", "Questo prestito è stato già chiuso e il libro è stato restituito.");
                return;
            }
            manager.restituisciPrestito(p);
            tabellaPrestiti.refresh();
            mostraMsg("Successo", "Libro restituito correttamente.");
        }
            else
        {
            mostraMsg("Info", "Seleziona una riga da restituire.");
        }
    }

    private void mostraMsg(String titolo, String txt)
    {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titolo);
        a.setContentText(txt);
        a.showAndWait();
    }
}

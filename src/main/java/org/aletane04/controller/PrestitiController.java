/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.aletane04.data.Biblioteca;
import org.aletane04.model.*;
import org.aletane04.exceptions.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PrestitiController implements Initializable {

    @FXML private TableView<Prestito> tabellaPrestiti;
    @FXML private TableColumn<Prestito, String> colUtente, colLibro, colStato;
    @FXML private TableColumn<Prestito, LocalDate> colFine;
    @FXML private ComboBox<Utente> comboUtenti;
    @FXML private ComboBox<Libro> comboLibri;
    @FXML private DatePicker dateFine;

    private Biblioteca manager;

    
    /**
    * @brief Inizializza il controller e configura la TableView per la visualizzazione dei prestiti.
    *
    * Questa funzione è chiamata automaticamente dal framework JavaFX. Configura le colonne della 
    * tabellaPrestiti per associare i dati degli oggetti Prestito e delle loro 
    * relazioni (Utente, Libro). Inoltre, imposta una RowFactory per applicare colorazioni di sfondo 
    * alle righe basate sullo stato del prestito (SCADUTO, IN_SCADENZA), fornendo un feedback visivo immediato.
    * Infine, imposta la politica di ridimensionamento della tabella.
    *
    * @pre I componenti della GUI colUtente, colLibro, colFine, colStato, tabellaPrestiti devono essere stati iniettati.
    * @post Tutte le colonne sono associate alle rispettive proprietà, alcune tramite PropertyValueFactory e altre tramite espressioni lambda per gestire dati annidati.
    * @post Viene applicata una RowFactory personalizzata alla tabellaPrestiti per colorare le righe dei prestiti in base al loro stato.
    * @post La tabellaPrestiti è configurata per utilizzare la politica di ridimensionamento CONSTRAINED_RESIZE_POLICY.
    *
    * @param[in] location L'URL utilizzato per risolvere i percorsi relativi.
    * @param[in] resources Le risorse utilizzate per la localizzazione.
    * 
    */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /* SetUp grafico */
        colUtente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUtente().toString()));
        colLibro.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLibro().getTitolo()));
        colFine.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dataFine"));
        colStato.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatoPrestito().toString()));

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



    }
    /**
    * @brief Imposta il manager della biblioteca e popola gli elementi della GUI con i dati iniziali.
    *
    * Questo metodo viene chiamato per associare il controller al modello dati principale (Biblioteca). 
    * Una volta che l'istanza del manager è disponibile, il metodo popola immediatamente la 
    * tabella dei prestiti (tabellaPrestiti) e le combo box per la selezione degli utenti 
    * e dei libri (comboUtenti e comboLibri).
    *
    * @pre Il manager della biblioteca (manager) non deve essere null e deve contenere liste valide di oggetti Utente, Libro e Prestito.
    * @pre I componenti GUI (comboUtenti, comboLibri, tabellaPrestiti) devono essere stati iniettati.
    * @post L'istanza del manager è memorizzata nella variabile di classe this.manager.
    * @post Le combo box comboUtenti e comboLibri vengono popolate con le rispettive liste di oggetti, rendendole selezionabili.
    * @post La tabellaPrestiti viene popolata con la lista di prestiti attiva nel sistema.
    *
    * @param[in] manager L'istanza della classe {@code Biblioteca} che fornisce l'accesso ai dati (liste di Utenti, Libri, Prestiti). 
    * 
    **/
    public void setBiblioteca(Biblioteca manager) {
        this.manager = manager;

        /* SetUp dei dati */
        // In questo metodo, vengono popolate le ComboBox e la Tabella solo ora che si hanno le liste.
        comboUtenti.setItems(manager.getUtenti());
        comboLibri.setItems(manager.getLibri());
        tabellaPrestiti.setItems(manager.getPrestiti());
    }
    
    
    /**
    * @brief Registra un nuovo prestito nel sistema.
    * 
    * Questa funzione recupera l'Utente, il Libro e la Data di Fine Prestito
    * selezionati nell'interfaccia. Esegue una validazione di base per
    * assicurarsi che tutti i campi siano stati selezionati. In caso di successo,
    * tenta di registrare il prestito tramite il gestore e pulisce le selezioni.
    *
    * @pre I componenti FXML comboUtenti, comboLibri e
    * dateFine devono essere stati inizializzati e contenere i dati.
    * @post Se l'operazione ha successo, il prestito è registrato nel gestore, e
    * le combo box {@code comboUtenti} e {@code comboLibri} sono resettate.
    * In caso di errore, viene mostrato un messaggio all'utente.
    * @param[in] comboUtenti Il ComboBox per la selezione dell'Utente.
    * @param[in] comboLibri Il ComboBox per la selezione del Libro.
    * @param[in] dateFine Il DatePicker per la selezione della data di fine prestito.
    * 
    * 
    */
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

            manager.registraPrestito(u, l, fine);
            comboUtenti.getSelectionModel().clearSelection();
            comboLibri.getSelectionModel().clearSelection();

        } catch (LibroNonDisponibileException | LimitePrestitiSuperatoException e) {
            mostraMsg("Impossibile procedere", e.getMessage());
        }
    }
    
    
    /**
    * @brief Gestisce l'evento di restituzione di un prestito selezionato.
    * 
    * Recupera l'oggetto Prestito selezionato nella tabella tabellaPrestiti.
    * Se una riga è effettivamente selezionata, procede alla registrazione della
    * restituzione tramite il gestore manager. In caso contrario,
    * mostra un messaggio informativo all'utente.
    *
    * @pre Il componente FXML tabellaPrestiti deve essere stato inizializzato e
    * contenere i dati dei prestiti attivi.
    * @post Se l'operazione ha successo, lo stato del prestito selezionato viene aggiornato
    * (tipicamente viene contrassegnato come restituito) e il libro associato
    * diventa nuovamente disponibile. Se nessuna riga è selezionata, viene mostrato
    * un messaggio di avviso.
    * @param[in] tabellaPrestiti La TableView che contiene l'elenco dei prestiti.
    * 
    */
    @FXML
    public void onRestituisci() {
        Prestito p = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (p != null) {
            manager.restituisciPrestito(p);
        } else {
            mostraMsg("Info", "Seleziona una riga da restituire.");
        }
    }

    
    /**
    * @brief Mostra un messaggio di avviso (Alert) all'utente.
    * Crea un oggetto Alert di tipo INFORMATION, impostando il titolo
    * e il testo del contenuto forniti come parametri, e lo mostra attendendo
    * la risposta dell'utente.
    *
    * @post Viene mostrata una finestra di dialogo informativa all'utente con il
    * titolo e il testo specificati, e l'esecuzione del programma viene sospesa
    * fino alla chiusura della finestra.
    * 
    * @param[in] titolo La stringa da usare come titolo della finestra di dialogo.
    * @param[in] txt La stringa da usare come testo del contenuto del messaggio.
    */
    private void mostraMsg(String titolo, String txt) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titolo);
        a.setContentText(txt);
        a.showAndWait();
    }
}

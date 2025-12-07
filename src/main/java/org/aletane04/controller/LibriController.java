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

    /**
    * @brief Inizializza il controller e configura la TableView per la visualizzazione dei libri.
    *
    * Questa funzione è chiamata automaticamente dal framework JavaFX dopo che l'interfaccia 
    * utente è stata completamente caricata. Il suo scopo è associare le colonne della tabella 
    * alle proprietà del modello dati (Libro) e impostare la politica di ridimensionamento.
    *
    * @pre Il file FXML corrispondente deve essere caricato e gli elementi devono essere stati iniettati.
    * @post Tutte le colonne della tabella sono associate alle rispettive proprietà del modello dati, 
    * garantendo che le colonne riempiano l'intero spazio orizzontale disponibile.
    *
    * @param[in] location L'URL utilizzato per risolvere i percorsi relativi per l'oggetto radice.
    * @param[in] resources Le risorse utilizzate per localizzare l'oggetto radice.
    */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. SETUP GRAFICO (Colonne) 
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("codiceISBN"));
        colCopie.setCellValueFactory(new PropertyValueFactory<>("numeroCopieDisponibili"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));
        
        // RISOLUZIONE PROBLEMA: Impostiamo la policy via codice Java
        // Questo fa sì che le colonne si allighino per riempire tutto lo spazio
        tabellaLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    /**
    * @brief Imposta il manager della biblioteca e inizializza la logica di visualizzazione e ricerca dei dati.
    *
    * Questo metodo viene chiamato per associare il controller al modello dati principale ({@code Biblioteca}) 
    * e stabilire i meccanismi di filtro e ordinamento per la tabella. Stabilisce l'ascoltatore (listener) 
    * sul campo di testo di ricerca per aggiornare dinamicamente i dati visualizzati.
    *
    * @pre Il manager della biblioteca non deve essere null e deve contenere 
    * una lista valida di oggetti Libro tramite il metodo getLibri().
    * @pre Il campo di testo per la ricerca e la tabella devono essere stati iniettati.
    * @post L'istanza del manager è memorizzata nella variabile di classe this.manager.
    * @post I dati della tabella sono configurati con una FilteredList e una SortedList, consentendo la ricerca dinamica.
    * @post Un listener è attivo sulla proprietà di testo di txtRicerca per filtrare i libri in base a titolo, autore o ISBN.
    *
    * @param[in] manager L'istanza della classe {@code Biblioteca} che fornisce i dati e la logica di business. 
    */
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

    /**
    * @brief Gestisce l'evento di aggiunta di un nuovo libro alla biblioteca
    *
    * Questo metodo recupera i dati dai campi di input del moduolo, esegue una validazione di base, (controllo dei campi obbligatori e formato numerico) e, 
    * in caso di successo aggiunge un nuovo oggetto della classe Libro al modello dati
    *
    * @pre il manager della biblioteca deve essere inizializzato e disponibile per invocare il metodo
    * @pre il testo dei campi txtTitolo e txtISBN non deve essere vuoto@
    * @pre il testo del campo txtCopie deve essere un valore numerico intero valido
    * @post se ha successo, un nuovo oggetto della classe Libro viene aggiunto al modello dati@
    * @post i campi di input vengono puliti utilizzando pulisciCampi(@)
    * @post se si verifica un errore validazione o di formato, viene mostrato un messaggio di errore all'utente tramite mostraErrore() 
    * e l'operazione di aggiunta viene annullatar
    * 
    **/
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
    
    
    
    
    
    /**
    * @brief Gestisce l'evento di rimozione di un libro selezionato dalla tabella.
    * 
    * Questo metodo verifica se un elemento è stato selezionato nella tabellaLibri. 
    * Se la selezione è valida, tenta di eliminare il libro dal modello dati tramite il manager. 
    * Il metodo gestisce gli errori, sia quelli derivanti dalla mancanza di selezione, 
    * sia quelli sollevati dal manager durante il tentativo di rimozione (e.g., libro in prestito).
    * 
    * @pre Il manager della biblioteca ({@code manager}) deve essere stato inizializzato e in grado 
    * di eseguire l'operazione {@code rimuoviLibro()}.
    * @pre La {@code tabellaLibri} deve essere popolata con dati validi.
    * @post Se l'utente non ha selezionato alcun libro, viene mostrato un messaggio di errore.
    * @post Se la rimozione ha successo (il manager non solleva eccezioni), il libro viene rimosso 
    * dal modello dati e viene mostrato un messaggio di successo all'utente tramite {@code mostraInfo()}.
    * @post Se il manager solleva un'eccezione (e.g., violazione di vincoli di integrità), 
    * viene mostrato un messaggio di errore all'utente con il messaggio dell'eccezione.
    * 
    *  
    **/
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

    /**
    * @brief Resetta il contenuto di tutti i campi di input del modulo di aggiunta libro.
    *
    * Questo è un metodo di utilità interno che pulisce i campi di testo e resetta 
    * il selettore di data, preparando l'interfaccia utente per un nuovo inserimento 
    * di dati. Viene tipicamente chiamato dopo un'operazione di aggiunta completata 
    * con successo.
    *
    * @pre I campi di testo txtTitolo, txtAutore, txtIsbn, txtCopie e il selettore di data {@code pickerAnno} devono essere stati iniettati.
    * @post Tutti i campi di testo specificati sono vuoti (il loro contenuto è stato cancellato).
    * 
    **/
    private void pulisciCampi() {
        txtTitolo.clear(); txtAutore.clear(); txtIsbn.clear(); txtCopie.clear();
        pickerAnno.setValue(null);
    }
    
    /**
     * @brief Visualizza un messaggio di errore modale all'utente.
     *
     * Questo metodo di utilità crea e mostra una finestra di dialogo modale di tipo "Alert" (JavaFX) 
     * con icona di errore. L'esecuzione del programma viene sospesa fino a quando l'utente non 
     * interagisce con l'avviso, garantendo che il messaggio venga letto.
     *
     * @pre Nessuna precondizione specifica, il metodo è una funzione di utilità della GUI.
     * @post Viene visualizzata una finestra di dialogo modale di errore contenente il messaggio {@code msg}.
     * @post L'esecuzione del thread corrente è bloccata fino alla chiusura dell'Alert.
     *
     * @param[in] msg Il messaggio di errore dettagliato da mostrare all'utente.
    **/
    private void mostraErrore(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
    
    /**
     * @brief Visualizza un messaggio informativo modale all'utente.
     *
     * Questo metodo di utilità crea e mostra una finestra di dialogo modale di tipo "Alert" (JavaFX) 
     * con icona informativa. Viene utilizzato per notificare all'utente il successo di un'operazione 
     * o per fornire informazioni non critiche. L'esecuzione è bloccata finché l'utente non chiude l'avviso.
     *
     * 
     * @post Viene visualizzata una finestra di dialogo modale informativa contenente il messaggio {@code msg}.
     * @post L'esecuzione del thread corrente è bloccata fino alla chiusura dell'Alert, garantendo 
     * la ricezione del messaggio da parte dell'utente.
     *
     * @param[in] msg Il messaggio informativo o di successo da mostrare all'utente.
     * 
     **/
    private void mostraInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }
}
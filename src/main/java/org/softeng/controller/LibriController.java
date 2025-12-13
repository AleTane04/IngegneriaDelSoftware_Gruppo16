/** @file LibriController.java
 *  @brief Il file contiene l'implementazione della classe 'LibriController'
 *  
 *  informazioni sul file e il suo ruolo nel progetto
 */
package org.softeng.controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.softeng.data.Biblioteca;
import org.softeng.exceptions.LibroGiaPresenteException;
import org.softeng.model.Libro;
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
        ///< Setup grafico delle colonne 
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("codiceISBN"));
        colCopie.setCellValueFactory(new PropertyValueFactory<>("numeroCopieDisponibili"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("annoPubblicazione"));

        tabellaLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ///< La tabella diventa modificabile 
        tabellaLibri.setEditable(true);

        colTitolo.setCellFactory(TextFieldTableCell.forTableColumn());
        colTitolo.setOnEditCommit(event -> {
            ///< Salvo il riferimento del libro il cui titolo è stato modificato 
            Libro libro = event.getRowValue();
            ///< Aggiorno il valore del campo Titolo 
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
            Integer numCopie = event.getNewValue();
            if(numCopie==null || numCopie<0)
            {
                ///< Mostro Alert per avvisare l'utente
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Valore non valido");
                alert.setContentText("Il numero di copie non può essere negativo!");
                alert.showAndWait();

                ///< Refresh tabella
                tabellaLibri.refresh();
            }
                else
            {
                libro.setNumeroCopieDisponibili(numCopie);
            }
        });

        colAnno.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        colAnno.setOnEditCommit(event -> {
            if(event.getNewValue().isAfter(LocalDate.now()))
            {
                mostraErrore("Impossibile inserire una data di pubblicazione successiva a quella odierna");
                tabellaLibri.refresh();
            }
            else
            {
                Libro libro = event.getRowValue();
                libro.setAnnoPubblicazione(event.getNewValue());
            }

        });
        ///< ISBN NON MODIFICABILE 
        colIsbn.setEditable(false);

        pickerAnno.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

               ///< Verifico che la data inserita sia successiva a quella del giorno odierno 
                if (date.isAfter(LocalDate.now())) {
                    ///< Disabilito la selezione della data 
                    setDisable(true);

                }
            }
        });
        ///< Rendo il campo non editabile manualmente(via tastiera) 
        pickerAnno.setEditable(false);

        ///< Deselezionare premendo ESC 
        tabellaLibri.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                tabellaLibri.getSelectionModel().clearSelection();
            }
        });

        ///< Deselezionare cliccando su uno spazio vuoto 
        tabellaLibri.setOnMouseClicked(event -> {
            ///< Viene verificato che il click è avvenuto su uno spazio vacuo 
            if (event.getTarget() instanceof javafx.scene.Node) {
                javafx.scene.Node nodo = (javafx.scene.Node) event.getTarget();

                ///< Risalita della gerarchia 
                while (nodo != null && nodo != tabellaLibri) {
                    if (nodo instanceof TableRow && ((TableRow) nodo).getItem() != null) {
                        return; ///< Riga valida -> esco senza far nulla 
                    }
                    nodo = nodo.getParent();
                }

                ///< È stato cliccato fuori dalle righe -> si procede con la pulizia della selezione 
                tabellaLibri.getSelectionModel().clearSelection();
            }
        });
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

        ///< Setup dei dati, una volta ricevuto il manager 
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
    * @pre il testo dei campi txtTitolo e txtISBN non devono essere vuoti
    * @pre il testo del campo txtCopie deve essere un valore numerico intero valido
    * @post se ha successo, un nuovo oggetto della classe Libro viene aggiunto al modello dati@
    * @post i campi di input vengono puliti utilizzando pulisciCampi()
    * @post se si verifica un errore validazione o di formato, viene mostrato un messaggio di errore all'utente tramite mostraErrore() 
    * e l'operazione di aggiunta viene annullatar
    * 
    */
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

   /**
    * @brief Gestisce l'evento di rimozione di un libro selezionato dalla tabella.
    * 
    * Questo metodo verifica se un elemento è stato selezionato nella tabellaLibri. 
    * Se la selezione è valida, tenta di eliminare il libro dal modello dati tramite il manager. 
    * Il metodo gestisce gli errori, sia quelli derivanti dalla mancanza di selezione, 
    * sia quelli sollevati dal manager durante il tentativo di rimozione (e.g., libro in prestito).
    * 
    * @pre Il manager della biblioteca (manager) deve essere stato inizializzato e in grado 
    * di eseguire l'operazione rimuoviLibro().
    * @pre La tabellaLibri deve essere popolata con dati validi.
    * @post Se l'utente non ha selezionato alcun libro, viene mostrato un messaggio di errore.
    * @post Se la rimozione ha successo (il manager non solleva eccezioni), il libro viene rimosso 
    * dal modello dati e viene mostrato un messaggio di successo all'utente tramite mostraInfo().
    * @post Se il manager solleva un'eccezione (e.g., violazione di vincoli di integrità), 
    * viene mostrato un messaggio di errore all'utente con il messaggio dell'eccezione.
    * 
    *  
    */
    @FXML
    public void onRimuovi() {
       ///< Riga selezionata dall'utente 
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();

        ///< Se l'utente non ha selezionato alcuna riga, viene avvisato 
        if (selezionato == null) {
            mostraErrore("Seleziona prima un libro dalla tabella!");
            return;
        }

        ///< Provo a cancellare il libro 
         try {
            manager.rimuoviLibro(selezionato);
        
            ///< Se arrivo qui, non c'è stata eccezione -> Successo!
            mostraSuccesso("Libro eliminato con successo.");
        
        } catch (Exception e) {
            ///< Se il manager lancia l'eccezione (libro in prestito), la catturo qui
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
    * @pre I campi di testo txtTitolo, txtAutore, txtIsbn, txtCopie e il selettore di data pickerAnno devono essere stati iniettati.
    * @post Tutti i campi di testo specificati sono vuoti (il loro contenuto è stato cancellato).
    * 
    **/
    private void pulisciCampi()
    {
        txtTitolo.clear(); txtAutore.clear(); txtIsbn.clear(); txtCopie.clear();
        pickerAnno.setValue(null);
    }

    ///< Metodi Helper 

    /**
     * @brief Visualizza un messaggio di successo modale all'utente.
     *
     * Questo metodo crea e mostra una finestra di dialogo modale di tipo Alert
     * con icona di successo. Viene utilizzato per notificare all'utente il successo di un'operazione. 
     * L'esecuzione è bloccata finché l'utente non chiude l'avviso.
     *
     * 
     * @post Viene visualizzata una finestra di successo modale informativa contenente il messaggio msg.
     * @post L'esecuzione del thread corrente è bloccata fino alla chiusura dell'Alert, garantendo 
     * la ricezione del messaggio da parte dell'utente.
     *
     * @param[in] msg Il messaggio di successo da mostrare all'utente.
     * 
     */
    private void mostraSuccesso(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operazione Completata");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * @brief Visualizza un messaggio di errore modale all'utente.
     *
     * Questo metodo crea e mostra una finestra di dialogo modale di tipo Alert
     * con icona di errore. L'esecuzione del programma viene sospesa fino a quando l'utente non 
     * interagisce con l'avviso, garantendo che il messaggio venga letto.
     *
     * 
     * @post Viene visualizzata una finestra di dialogo modale di errore contenente il messaggio {@code msg}.
     * @post L'esecuzione del thread corrente è bloccata fino alla chiusura dell'Alert.
     *
     * @param[in] msg Il messaggio di errore dettagliato da mostrare all'utente.
     */
    private void mostraErrore(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText("Si è verificato un problema");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

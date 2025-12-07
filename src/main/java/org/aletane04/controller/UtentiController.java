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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UtentiController implements Initializable {

    // --- ELEMENTI GRAFICI (View) ---
    @FXML private TableView<Utente> tabellaUtenti;
    
    // Colonne della tabella
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colEmail;

    // Campi di input e ricerca
    @FXML private TextField txtNome;
    @FXML private TextField txtCognome;
    @FXML private TextField txtMatricola;
    @FXML private TextField txtEmail;
    @FXML private TextField txtRicerca; // La barra di ricerca in alto

    // Riferimento al "Cervello" dell'app
    private Biblioteca manager;

    /**
    * @brief Inizializza il controller e configura il binding delle colonne della tabella Utenti.
    *
    * Questo metodo, richiesto dall'interfaccia Initializable di JavaFX,
    * viene chiamato automaticamente dopo che tutti i componenti FXML
    * (come la tabella e le sue colonne) sono stati caricati e iniettati.
    * Configura il meccanismo di associazione tra le colonne della tabella
    * tabellaUtenti e le proprietà della classe Utente.
    *
    * @pre Tutti i componenti FXML (colonne e tabella) devono essere stati iniettati
    * correttamente dal FXMLLoader.
    * @post Le colonne della tabella sono associate alle proprietà della classe Utente
    * tramite PropertyValueFactory. Il testo placeholder è impostato
    * per le tabelle vuote e la politica di ridimensionamento è impostata su
    * CONSTRAINED_RESIZE_POLICY per riempire lo spazio disponibile.
    *
    * @param[in] location La posizione relativa dell'oggetto radice (non utilizzato).
    * @param[in] resources Le risorse utilizzate per la localizzazione dell'oggetto radice (non utilizzato).
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
    * @brief Associa il gestore della biblioteca (modello) al controller e inizializza il meccanismo di filtro e ordinamento per la tabella Utenti.
    *
    * Questo metodo stabilisce il legame tra il controller e l'istanza di Biblioteca
    * che funge da modello (manager). Successivamente, imposta il **data binding dinamico**
    * per la tabella degli utenti (tabellaUtenti), permettendo sia il filtraggio
    * tramite la barra di ricerca (txtRicerca) sia l'ordinamento
    * automatico quando l'utente clicca sulle intestazioni delle colonne.
    * L'implementazione segue il pattern JavaFX:
    * 1. La lista originale degli utenti viene avvolta in una FilteredList.
    * 2. Un listener viene aggiunto alla proprietà del testo per aggiornare il predicato del filtro.
    * 3. La FilteredList viene avvolta in una SortedList.
    * 4. La proprietà del comparatore della SortedList viene legata (bind) alla proprietà
    * *  del comparatore della TableView.
    * 5. I dati finali (filtrati e ordinati) sono impostati come sorgente della tabella.
    *
    * @pre Il manager Biblioteca deve essere inizializzato e la tabella
    * tabellaUtenti e il campo di testo txtRicerca devono essere
    * stati iniettati correttamente dal FXML.
    * @post L'istanza di Biblioteca è memorizzata nel campo manager.
    * Il binding tra i dati della tabella e le liste dinamiche (filtrate e ordinate)
    * è attivo, consentendo la ricerca per Nome, Cognome o Matricola e l'ordinamento
    * cliccando sulle colonne.
    *
    * @param[in] manager L'istanza di Biblioteca che contiene i dati e la logica di business.
    * 
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
    * @brief Gestisce l'evento di aggiunta di un nuovo utente nel sistema.
    *
    * Questa funzione recupera i dati dell'utente (Nome, Cognome, Matricola ed Email)
    * dai campi di testo FXML. Esegue una **validazione di base** per assicurare
    * che i campi obbligatori (Nome, Cognome e Matricola) non siano vuoti.
    * In caso di validazione positiva, crea un nuovo oggetto Utente,
    * lo passa al gestore manager per l'aggiunta (che gestisce la logica
    * di business, inclusi i duplicati) e, infine, pulisce i campi di input.
    *
    * @pre I campi di testo FXML (txtNome, txtCognome, txtMatricola,
    * txtEmail) devono essere stati iniettati e il manager manager
    * deve essere inizializzato.
    * @post Se i dati obbligatori sono presenti, un nuovo Utente è passato al
    * gestore per l'aggiunta al sistema, e tutti i campi di input vengono puliti
    * tramite pulisciCampi(). Se mancano dati obbligatori, viene mostrato un
    * avviso all'utente.
    *
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
    * @brief Gestisce l'evento di eliminazione dell'utente selezionato dalla tabella.
    *
    * Questa funzione recupera l'oggetto Utente selezionato nella tabella
    * tabellaUtenti. Se è stata effettuata una selezione, mostra una
    * finestra di dialogo di conferma all'utente. Se l'utente conferma
    * l'operazione (ButtonType.OK), l'utente viene rimosso direttamente dalla lista
    * gestita dal manager e i dati vengono salvati su file tramite manager.saveAll().
    * Se nessuna riga è selezionata, viene mostrato un messaggio di avviso informativo.
    *
    * @pre La tabella tabellaUtenti deve essere stata popolata e il manager deve essere inizializzato.
    * @post Se l'utente conferma l'eliminazione, l'oggetto Utente selezionato
    * viene rimosso dalla lista interna del manager e i dati vengono salvati su disco.
    * In caso di mancata selezione, viene mostrato un avviso.
    *
    * @param[in] tabellaUtenti La TableView da cui viene recuperata la selezione.
    * @param[in] selezionato L'oggetto Utente selezionato (recuperato internamente).
    *
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
    
    /**
    * @brief Pulisce il contenuto testuale di tutti i campi di input dell'utente.
    *
    * Questa funzione è un metodo di utilità interno al controller.
    * Rimuove tutti i dati inseriti dall'utente nei campi di testo FXML
    * (txtNome, txtCognome, txtMatricola, txtEmail), riportandoli a uno stato vuoto.
    * 
    * @pre I campi di testo FXML devono essere stati iniettati e non nulli.
    * @post Tutti i campi di testo (Nome, Cognome, Matricola, Email) sono vuoti.
    */
    private void pulisciCampi() {
        txtNome.clear();
        txtCognome.clear();
        txtMatricola.clear();
        txtEmail.clear();
    }

    
    /**
    * @brief Mostra una finestra di dialogo di tipo Alert all'utente.
    *
    * Crea un oggetto Alert con il tipo specificato (es. INFORMATION, WARNING, ERROR),
    * impostando il titolo e il testo del contenuto. L'intestazione (Header Text)
    * della finestra di dialogo viene esplicitamente impostata a null per
    * mostrare solo il titolo e il contenuto. La finestra viene visualizzata e
    * l'esecuzione viene bloccata finché l'utente non la chiude.
    *
    * @post Viene visualizzata una finestra di dialogo modale di tipo Alert
    * con le informazioni specificate, bloccando l'esecuzione del thread corrente
    * finché l'utente non interagisce con essa.
    * 
    * @param[in] tipo Il tipo di alert che definisce l'icona della finestra.
    * @param[in] titolo La stringa da usare come titolo della finestra di dialogo.
    * @param[in] contenuto La stringa da usare come testo principale del messaggio.
    * 
    */
    private void mostraAlert(Alert.AlertType tipo, String titolo, String contenuto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
}

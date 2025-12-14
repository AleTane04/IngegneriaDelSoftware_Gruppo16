/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @file Biblioteca.java
 * @brief Il file contiene l'implementazione della classe 'Biblioteca'
 *
 * informazioni sul file e il suo ruolo nel progetto
 */
package org.softeng.data;

import javafx.collections.ObservableList;
import org.softeng.model.*;
import org.softeng.exceptions.*;
import java.time.LocalDate;

/**
 *
 * @author angel
 */
public class Biblioteca
{
    
    private static final String FILE_LIBRI = "elenco_libri.csv";
    private static final String FILE_UTENTI = "elenco_utenti.csv";
    private static final String FILE_PRESTITI = "elenco_prestiti.csv";
    
    private ObservableList<Utente> listaUtenti;
    private ObservableList<Libro> listaLibri;
    private ObservableList<Prestito> listaPrestiti;
    private final BibliotecaFileManager bibliotecaFileManager;


    /**
     * @brief Costruttore predefinito per la classe Biblioteca.
     *
     * Inizializza il gestore file interno e carica lo stato completo della
     * biblioteca dai file di persistenza.
     *
     * @post L'oggetto 'bibliotecaFileManager' è stato inizializzato.
     * @post La lista di 'listaLibri' è stata popolata con tutti i Libri presenti nel FILE_LIBRI.
     * @post La lista di 'listaUtenti' è stata popolata con tutti gli Utenti presenti nel FILE_UTENTI.
     * @post La lista di 'listaPrestiti' è stata popolata con tutti i Prestiti presenti nel FILE_PRESTITI,
     * associando gli Utenti e i Libri caricati in precedenza.
     */
    public Biblioteca()
    {
        this.bibliotecaFileManager = new BibliotecaFileManager();
        
        ///< Carico le entità "indipendenti", ossia la lista di Libri e Utenti
        this.listaLibri = bibliotecaFileManager.caricaLibriDaFile(FILE_LIBRI);
        this.listaUtenti = bibliotecaFileManager.caricaUtentiDaFile(FILE_UTENTI);

        /** Per caricare i prestiti, gli passo al BibliotecaFileManager
         *  le liste appena popolate
         */

        this.listaPrestiti = bibliotecaFileManager.caricaPrestitiDaFile(FILE_PRESTITI, listaUtenti, listaLibri);

    }

    ///< Metodi Getter


    /**
     * @brief Restituisce la lista di tutti i libri presenti nella biblioteca.
     *
     * Fornisce un accesso alla collezione interna dei libri gestiti dall'oggetto
     * Biblioteca.
     *
     *
     * @post Viene restituita la collezione interna dei libri della biblioteca.
     * @return Un oggetto ObservableList contenente tutti gli oggetti Libro
     * attualmente presenti nella biblioteca.
     */
    public ObservableList<Libro> getLibri()
    {
        return listaLibri;
    }


    /**
     * @brief Restituisce la lista di tutti gli utenti presenti nella biblioteca.
     *
     * Fornisce un accesso alla collezione interna degli utenti gestiti dall'oggetto
     * Biblioteca.
     *
     *
     * @post Viene restituita la collezione interna degli utenti della biblioteca.
     * @return Un oggetto ObservableList contenente tutti gli oggetti Utente
     * attualmente presenti nella biblioteca.
     */
    public ObservableList<Utente> getUtenti()
    {
        return listaUtenti;
    }


    /**
     * @brief Restituisce la lista di tutti i prestiti presenti nella biblioteca.
     *
     * Fornisce un accesso alla collezione interna dei prestiti gestiti dall'oggetto
     * Biblioteca.
     *
     *
     * @post Viene restituita la collezione interna dei prestiti della biblioteca.
     * @return Un oggetto ObservableList contenente tutti gli oggetti Prestito
     * attualmente presenti nella biblioteca.
     */
    public ObservableList<Prestito> getPrestiti()
    {
        return listaPrestiti;
    }

    ///< Metodi concernenti i Libri

    /**
     * @brief Aggiunge un nuovo libro alla lista, se non è già presente.
     * Questa funzione tenta di inserire un oggetto Libro nella collezione.
     * Se il libro (identificato dalla sua chiave unica, l'ISBN
     * contenuto in newLibro) è già presente, viene sollevata un'eccezione.
     *
     *
     * @pre Il parametro newLibro non deve essere nullo e deve contenere
     * un identificatore (ISBN) valido e unico.
     * @post Se l'operazione ha successo, la lista dei libri contiene newLibro
     * e la sua dimensione è aumentata di uno. L'oggetto newLibro viene aggiunto
     * solo se non è già presente nella lista.
     * @param[in] newLibro L'oggetto Libro da aggiungere
     *
     */
    public void aggiungiLibro(Libro newLibro) throws LibroGiaPresenteException
    {

        if(listaLibri.contains(newLibro) == false) {
            listaLibri.add(newLibro);
        }
            else
        {
           ///< Lancio una eccezione
             throw new LibroGiaPresenteException("Esiste già un libro con ISBN " + newLibro.getCodiceISBN() + ".\nRiprovare, o, in alternativa, aggiornare il numero di copie.");

        }
    }

    ///< Rimozione di un libro
    /**
     * @brief Rimuove un libro dalla lista di libri gestiti.
     *
     * Questa funzione rimuove l'oggetto Libro specificato dalla lista
     * se e solo se non è attualmente associato ad alcun prestito.
     *
     * @pre Il libro specificato non deve essere attualmente in prestito.
     * @post Se l'operazione ha successo (il libro non era in prestito),
     * il libro viene rimosso dalla lista, e la dimensione della lista si
     * riduce di uno.
     * @param[in] libroDaRimuovere L'oggetto Libro da rimuovere dalla collezione.
     *
     */
    public void rimuoviLibro(Libro libroDaRimuovere) throws Exception
    {
        for (Prestito p : listaPrestiti) {
        if (p.getLibro().equals(libroDaRimuovere))
        {
            if (p.getStatoPrestito() != StatoPrestito.RESTITUITO)
            {
                throw new Exception("Impossibile eliminare: il libro è attualmente in prestito da un utente!");
            }

        }
    }

    ///< 3. SE È LIBERO -> LO CANCELLO
    ///< La lista si aggiorna, e grazie a ObservableList anche la tabella sparirà da sola
    listaLibri.remove(libroDaRimuovere);
    }




    /**
     * @brief Rimuove un utente dalla lista degli utenti gestiti.
     *
     * Questa funzione rimuove l'oggetto Utente specificato dalla lista
     * se e solo se non ha prestiti attivi in corso (StatoPrestito diverso da RESTITUITO).
     * La rimozione non ha impatto sullo storico dei prestiti conclusi
     * (listaPrestiti), rendendo i record orfani.
     *
     *
     *
     * @pre L'utente specificato non deve avere prestiti attivi in corso (ovvero, tutti
     * i prestiti a lui associati devono avere StatoPrestito.RESTITUITO).
     * @post Se l'operazione ha successo (nessun prestito attivo), l'utente viene
     * rimosso dalla lista, e la dimensione della lista si riduce di uno. I record
     * dei prestiti conclusi associati a questo utente vengono mantenuti.
     *
     * @param[in] utenteDaRimuovere L'oggetto Utente da rimuovere.
     *
     */
    public void rimuoviUtente(Utente utenteDaRimuovere) throws Exception {

        ///< Verifica che l'utente abbia prestiti attivi
        boolean haPrestitiAttivi = false;

        for (Prestito p : listaPrestiti) {
            ///< Verifica che l'utente corrisponda
            if (p.getUtente().equals(utenteDaRimuovere)) {

                ///< Se il prestito non è concluso, mi fermo
                if (p.getStatoPrestito() != StatoPrestito.RESTITUITO) {
                    haPrestitiAttivi = true;
                    break; ///< Libro non restituito -> mi fermo
                }
            }
        }

        ///< Se ci sono prestiti attivi, lancio una eccezione
        if (haPrestitiAttivi) {
            throw new Exception("Impossibile eliminare: l'utente ha ancora dei libri da restituire!");
        }

        ///< Prestiti conclusi -> cancellazione

        /** Scelta progettuale:
         *  Desideriamo lo storico dei prestiti nel file CSV anche dopo aver cancellato l'utente,
         *  dunque non tocchiamo minimamente la listaPrestiti.
         *  Così facendo, i vecchi prestiti "verdi" rimarranno orfani (punteranno a una matricola che non c'è più nell'elenco utenti).

         *  Riassumendo:
         *  Per mantenere la consistenza, se un utente viene rimosso fisicamente, il sistema al riavvio scarta i prestiti
         *  orfani non potendo più risalire ai dati anagrafici del richiedente.
         */


        listaUtenti.remove(utenteDaRimuovere);


    }


    ///< Metodo concernente gli Utenti

    /**
     * @brief Aggiunge un nuovo utente alla lista, se non è già presente.
     *
     * Questa funzione tenta di inserire un oggetto Utente nella collezione.
     * Se l'utente (identificato in base al criterio di uguaglianza dell'oggetto, tramite la matricola)
     * è già presente, viene sollevata un'eccezione.
     *
     * @pre Il parametro newUtente non deve essere nullo e deve contenere
     * una matricola valida.
     * @post Se l'operazione ha successo, la lista degli utenti contiene newUtente
     * e la sua dimensione è aumentata di uno.
     *
     * @param[in] newUtente L'oggetto Utente da aggiungere.
     */
    public void aggiungiUtente(Utente newUtente) throws UtenteGiaPresenteException
    {

        if(listaUtenti.contains(newUtente) == false) {
            listaUtenti.add(newUtente);
        } else {

            throw new UtenteGiaPresenteException("Esiste già un utente con Matricola: " + newUtente.getMatricola());

        }
    }

    ///< Inserimento di un nuovo prestito
    /**
     * @brief Registra un nuovo prestito di un libro a un utente.
     *
     * Questa funzione crea un nuovo record di prestito con la data di inizio odierna,
     * a condizione che ci siano copie disponibili del libro e che l'utente non abbia
     * superato il limite massimo di 3 prestiti attivi.
     *
     *
     *
     * @pre Il libro deve avere almeno una copia disponibile (NumeroCopieDisponibili > 0).
     * @pre L'utente deve avere meno di 3 prestiti attivi.
     * @post Se l'operazione ha successo
     * viene creato e aggiunto alla listaPrestiti un nuovo oggetto Prestito
     * con la data di inizio odierna e la data di fine specificata.
     * @post Il numero di copie disponibili del libro viene decrementato di uno.
     * @post La vista della lista libri viene aggiornata per riflettere il decremento.
     *
     *
     */
    public void registraPrestito(Utente u, Libro l, LocalDate dataFine)
            throws LibroNonDisponibileException, LimitePrestitiSuperatoException
    {
        ///< Eseguo il controllo del numero di copie a disposizione
        if (l.getNumeroCopieDisponibili() <= 0)
        {
            throw new LibroNonDisponibileException("Copie esaurite per il libro: " + l.getTitolo());
        }

        ///< Eseguo il controllo del numero di Prestiti associati a un Testo

        int numeroPrestitiAttivi = 0;

        for(Prestito p : listaPrestiti)
        {
            if(p.getUtente().equals((u)))
                numeroPrestitiAttivi++;
        }

        if (numeroPrestitiAttivi >= 3)
        {
            throw new LimitePrestitiSuperatoException("L'utente " + u.getNome() + " ha già 3 prestiti attivi.");
        }

        ///< Creazione di un nuovo prestito, la cui data di inizio è quella odierna
        Prestito nuovoPrestito = new Prestito(u, l, LocalDate.now(), dataFine);

        /**
         * Aggiornamento dello Stato: si aggiunge il prestito alla Lista e si decrementa
         * il numero di copie del volume associato
         */

        listaPrestiti.add(nuovoPrestito);
        l.decrementaNumeroCopieDisponibili(); ///< Metodo nel model che fa copie--



        /** Si forza il refresh della lista libri per aggiornare,
         *  in modo immediato, il numero di copie visualizzato
         *  sulla tabella
         */
        int i = listaLibri.indexOf(l);
        if(i >= 0) listaLibri.set(i, l);
    }


    /**
     * @brief Registra la restituzione di un prestito e aggiorna la disponibilità del libro.
     *
     * Questa funzione registra la data di restituzione effettiva per un prestito in corso,
     * impostandola alla data odierna, e aumenta di conseguenza il numero di copie
     * disponibili del libro associato.
     *
     *
     *
     * @pre Il prestito deve essere attualmente in corso (StatoPrestito diverso da RESTITUITO).
     * Se il prestito è già restituito, la funzione termina senza effettuare modifiche.
     *
     * @post Se l'operazione ha avuto luogo (il prestito non era già RESTITUITO):
     * La data di restituzione effettiva del prestito viene impostata alla data odierna.
     * Lo stato del prestito è aggiornato (implicito dal cambiamento della data/stato).
     * Il numero di copie disponibili del libro associato viene incrementato di uno.
     * Le liste di libri e prestiti vengono aggiornate per riflettere le modifiche.
     *
     */
    public void restituisciPrestito(Prestito p)
    {
        ///< Controllo di sicurezza: se la data esiste, esco
        if (p.getStatoPrestito() == StatoPrestito.RESTITUITO)
            return;

        ///< Logica di Business: setto come data di restituzione quella odierna
        p.setDataRestituzioneEffettiva(LocalDate.now());

        Libro l = p.getLibro();
        l.incrementaNumeroCopieDisponibili();

        ///< Aggiorno il numero di copie
        int indexLibro = listaLibri.indexOf(l);
        if(indexLibro >= 0) {
            listaLibri.set(indexLibro, l);
        }

        /**
         *  Aggiornamento lista prestiti (per notificare il cambiamento di stato):
         *  Necessario per far "capire" a JavaFX che quel prestito è cambiato.
         */
        int indexPrestito = listaPrestiti.indexOf(p);
        if(indexPrestito >= 0) {
            listaPrestiti.set(indexPrestito, p);
        }
    }

   /**
     * @brief Salva lo stato attuale della biblioteca su file di persistenza.
     *
     * Questa funzione delega al gestore dei file (bibliotecaFileManager) il compito di
     * scrivere tutte le liste principali (libri, utenti e prestiti) nei rispettivi
     * file di persistenza (in formato CSV).
     *
     * @post Tutte le modifiche apportate alle liste (listaLibri, listaUtenti, listaPrestiti)
     * sono state salvate in modo permanente sui file esterni.
     */
    public void saveAll()
    {
        ///< Delega al FileManager la scrittura fisica
        bibliotecaFileManager.salvaLibri(listaLibri, FILE_LIBRI);
        bibliotecaFileManager.salvaUtenti(listaUtenti, FILE_UTENTI);
        bibliotecaFileManager.salvaPrestiti(listaPrestiti, FILE_PRESTITI);
        System.out.println("Salvataggio CSV completato.");
    }
}

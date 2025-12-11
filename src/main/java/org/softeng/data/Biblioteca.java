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
    
    public ObservableList<Utente> getUtenti() 
    {
        return listaUtenti;
    }
    
    public ObservableList<Prestito> getPrestiti() 
    {
        return listaPrestiti;
    }
    
    /* Metodi concernenti i Libri */
    public void aggiungiLibro(Libro newLibro) throws LibroGiaPresenteException
    {
    
        if(listaLibri.contains(newLibro) == false) {
            listaLibri.add(newLibro);
        }
            else
        {
           /* Lancio una eccezione */
             throw new LibroGiaPresenteException("Esiste già un libro con ISBN " + newLibro.getCodiceISBN() + ".\nRiprovare, o, in alternativa, aggiornare il numero di copie.");
            
        }
    }
    
    /* Rimozione di un libro */
    public void rimuoviLibro(Libro libroDaRimuovere) throws Exception 
    {
        
    
    // 1. CONTROLLO: Qualcuno ha questo libro in prestito?
    // Scorro la lista dei prestiti e cerco se c'è quel libro
    boolean inPrestito = false;
    for (Prestito p : listaPrestiti) {
        if (p.getLibro().equals(libroDaRimuovere)) {
            inPrestito = true;
            break;
        }
    }

    // 2. SE È IN PRESTITO -> BLOCCO TUTTO
    if (inPrestito) {
        throw new Exception("Impossibile cancellare: il libro è attualmente in prestito");
    }

    // 3. SE È LIBERO -> LO CANCELLO
    // La lista si aggiorna, e grazie a ObservableList anche la tabella sparirà da sola
    listaLibri.remove(libroDaRimuovere);
    }

    public void rimuoviUtente(Utente utenteDaRimuovere) throws Exception {

        /* Verifica che l'utente abbia prestiti attivi */
        boolean haPrestitiAttivi = false;

        for (Prestito p : listaPrestiti) {
            /* Verifica che l'utente corrisponda */
            if (p.getUtente().equals(utenteDaRimuovere)) {

                /* Se il prestito non è concluso, mi fermo */
                if (p.getStatoPrestito() != StatoPrestito.RESTITUITO) {
                    haPrestitiAttivi = true;
                    break; /* Libro non restituito -> mi fermo */
                }
            }
        }

        /* Se ci sono prestiti attivi, lancio una eccezione */
        if (haPrestitiAttivi) {
            throw new Exception("Impossibile eliminare: l'utente deve restituire dei libri!");
        }

        /* Prestiti conclusi -> cancellazione */

        /* Scelta progettuale:
        Desideriamo lo storico dei prestiti nel file CSV anche dopo aver cancellato l'utente,
        dunque non tocchiamo minimamente la listaPrestiti.
        Così facendo, i vecchi prestiti "verdi" rimarranno orfani (punteranno a una matricola che non c'è più nell'elenco utenti).

        Riassumendo:
        Per mantenere la consistenza, se un utente viene rimosso fisicamente, il sistema al riavvio scarta i prestiti
        orfani non potendo più risalire ai dati anagrafici del richiedente.


        */


        listaUtenti.remove(utenteDaRimuovere);


    }

    
    /* Metodo concernente gli Utenti */
    
    public void aggiungiUtente(Utente newUtente) throws UtenteGiaPresenteException
    {
    
        if(listaUtenti.contains(newUtente) == false) {
            listaUtenti.add(newUtente);
        } else {

            throw new UtenteGiaPresenteException("Esiste già un utente con Matricola: " + newUtente.getMatricola());
            
        }
    }
    
    /* Inserimento di un nuovo prestito */
    public void registraPrestito(Utente u, Libro l, LocalDate dataFine) 
            throws LibroNonDisponibileException, LimitePrestitiSuperatoException 
    {
        /* Eseguo il controllo del numero di copie a disposizione */
        if (l.getNumeroCopieDisponibili() <= 0) 
        {
            throw new LibroNonDisponibileException("Copie esaurite per il libro: " + l.getTitolo());
        }

        /* Eseguo il controllo del numero di Prestiti associati a un Testo */
        
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

        /* Creazione di un nuovo prestito, la cui data di inizio è quella odierna */
        Prestito nuovoPrestito = new Prestito(u, l, LocalDate.now(), dataFine);
        
        /* Aggiornamento dello Stato: si aggiunge il prestito alla Lista e si decrementa
        il numero di copie del volume associato */
        
        listaPrestiti.add(nuovoPrestito);
        l.decrementaNumeroCopieDisponibili(); // Metodo nel model che fa copie--
        

        
        /* Si forza il refresh della lista libri per aggiornare,
            in modo immediato, il numero di copie visualizzato
            sulla tabella */
        int i = listaLibri.indexOf(l);
        if(i >= 0) listaLibri.set(i, l);
    }

    public void restituisciPrestito(Prestito p)
    {
        /* Controllo di sicurezza: se la data esiste, esco */
        if (p.getStatoPrestito() == StatoPrestito.RESTITUITO)
            return;

        /* Logica di Business: setto come data di restituzione quella odierna */
        p.setDataRestituzioneEffettiva(LocalDate.now());

        Libro l = p.getLibro();
        l.incrementaNumeroCopieDisponibili();

        /* Aggiorno il numero di copie */
        int indexLibro = listaLibri.indexOf(l);
        if(indexLibro >= 0) {
            listaLibri.set(indexLibro, l);
        }

        /* Aggiornamento lista prestiti (per notificare il cambiamento di stato):
            Necessario per far "capire" a JavaFX che quel prestito è cambiato.
         */
        int indexPrestito = listaPrestiti.indexOf(p);
        if(indexPrestito >= 0) {
            listaPrestiti.set(indexPrestito, p);
        }
    }

   
    public void saveAll()
    {
        // Delega al FileManager la scrittura fisica
        bibliotecaFileManager.salvaLibri(listaLibri, FILE_LIBRI);
        bibliotecaFileManager.salvaUtenti(listaUtenti, FILE_UTENTI);
        bibliotecaFileManager.salvaPrestiti(listaPrestiti, FILE_PRESTITI);
        System.out.println("Salvataggio CSV completato.");
    }
}

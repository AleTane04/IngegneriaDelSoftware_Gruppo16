/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.aletane04.data;

import javafx.collections.ObservableList;
import org.aletane04.model.*;
import org.aletane04.exceptions.*;
import java.time.LocalDate;

/**
 *
 * @author angel
 */
public class Biblioteca {
    
    private static final String FILE_LIBRI = "elenco_libri.csv";
    private static final String FILE_UTENTI = "elenco_utenti.csv";
    private static final String FILE_PRESTITI = "elenco_libri.csv";
    
    private ObservableList<Utente> listaUtenti;
    private ObservableList<Libro> listaLibri;
    private ObservableList<Prestito> listaPrestiti;
    private final BibliotecaFileManager bibliotecaFileManager;
    
    public Biblioteca()
    {
        this.bibliotecaFileManager = new BibliotecaFileManager();
        
        /* Carico le entità "indipendenti", ossia la lista di Libri e Utenti */
        this.listaLibri = bibliotecaFileManager.caricaLibriDaFile(FILE_LIBRI);
        this.listaUtenti = bibliotecaFileManager.caricaUtentiDaFile(FILE_LIBRI);
        
        /* Per caricare i prestiti, gli passo al BibliotecaFileManager
           le liste appena popolate
        */
        
        this.listaPrestiti = bibliotecaFileManager.caricaPrestitiDaFile(FILE_PRESTITI, listaUtenti, listaLibri);
        
    }
    
    /* Metodi Getter */
    
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
    public void aggiungiLibro(Libro newLibro) 
    {
    
        if(listaLibri.contains(newLibro) == false) {
            listaLibri.add(newLibro);
        } else {
            //Lancio una eccezione;
            //gestioneio
            System.out.println("Errore nell'aggiunta del Testo");
            
        }
    }
    
    public void updateLibro() 
    {
        saveAll();
    }
    
    /* Metodo concernente gli Utenti */
    
    public void aggiungiUtente(Utente newUtente) 
    {
    
        if(listaUtenti.contains(newUtente) == false) {
            listaUtenti.add(newUtente);
        } else {
            //Lancio una eccezione;
            //gestioneio
            System.out.println("Errore nell'aggiunta dell'Utente");
            
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
        
        /* Salvataggio dell'Azione immediato */
        saveAll();
        
        /* Si forza il refresh della lista libri per aggiornare,
            in modo immediato, il numero di copie visualizzato
            sulla tabella */
        int i = listaLibri.indexOf(l);
        if(i >= 0) listaLibri.set(i, l);
    }

    public void restituisciPrestito(Prestito p) {
        // Rimuovo dalla lista (il prestito cessa di esistere)
        if (listaPrestiti.remove(p)) {
            
            // Incremento copie del libro associato
            Libro l = p.getLibro();
            l.incrementaNumeroCopieDisponibili(); // Metodo nel model che fa copie++
            
            // Aggiorno tabella libri e salvo
            int index = listaLibri.indexOf(l);
            if(index >= 0) listaLibri.set(index, l);
            
            saveAll();
        }
    }

   
    public void saveAll() {
        // Delega al FileManager la scrittura fisica
        bibliotecaFileManager.salvaLibri(listaLibri, FILE_LIBRI);
        bibliotecaFileManager.salvaUtenti(listaUtenti, FILE_UTENTI);
        bibliotecaFileManager.salvaPrestiti(listaPrestiti, FILE_PRESTITI);
        System.out.println("Salvataggio CSV completato.");
    }
   
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.softeng.data;

import org.softeng.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author 39392
 */
public class BibliotecaFileManager 
{
    public void scriviSuFile(String fileName, List<?> myList, String myFileHeader) 
    {
        /* Uso il try con le risorse */
        try(PrintWriter pw = new PrintWriter(new FileWriter(fileName))) 
        {
            /* Scrivo l'intestazione del mio file */
            pw.println(myFileHeader);
            
            for(Object obj : myList) 
            {
                /* Scrivo l'header */
                if(obj instanceof Libro)
                    pw.println(((Libro) obj).toCSV());
                else
                if(obj instanceof Utente)
                    pw.println(((Utente) obj).toCSV());
                else
                if(obj instanceof Prestito)
                    pw.println(((Prestito) obj).toCSV());
            }
        }
        catch(IOException e) 
        {
            System.err.println("Errore nella scrittura del file: "+ fileName + "; Errore: "+ e.getMessage());
        }
    }
    
    public void salvaLibri(List<Libro> libri, String fileName) 
    {
        scriviSuFile(fileName, libri,"TITOLO;AUTORI;DATA;ISBN;COPIE");
    }
    
    public void salvaUtenti(List<Utente> utenti, String fileName) 
    {
        scriviSuFile(fileName, utenti,"NOME;COGNOME;MATRICOLA;EMAIL");
    }
    
    public void salvaPrestiti(List<Prestito> prestiti, String fileName) 
    {
        scriviSuFile(fileName, prestiti,"MATRICOLA_UTENTE;ISBN_LIBRO;DATA_INIZIO;DATA_FINE;DATA_RESTITUZIONE");
    }
    
    public ObservableList<Libro> caricaLibriDaFile(String fileName) 
    {
        ObservableList<Libro> libri = FXCollections.observableArrayList();
        File f = new File(fileName);
        if (!f.exists()) 
            return libri;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) 
        {
            /* Lettura a vuoto, salto l'intestazione */
            br.readLine(); 
            String riga;
            while ((riga = br.readLine()) != null) 
            {
                if(riga.isEmpty())
                    continue;
                /* Aggiungo un nuovo Libro dalla lettura della riga */
                try
                {
                    libri.add(new Libro(riga));
                }
                catch(Exception e)
                {
                    System.err.println("La riga " + riga + " è corrotta: ignorata.");
                }

            }
        } catch (IOException e) {
            System.err.println("Errore lettura libri: " + e.getMessage());
        }
        return libri;
    }
    
    public ObservableList<Utente> caricaUtentiDaFile(String nomeFile) 
    {
        ObservableList<Utente> utenti = FXCollections.observableArrayList();
        File f = new File(nomeFile);
        if (!f.exists())
            return utenti;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) 
        {
            /* Lettura a vuoto, salto l'intestazione */
            br.readLine();
            String riga;
            while ((riga = br.readLine()) != null) 
            {
                if(riga.isEmpty())
                    continue;
                /* Aggiungo un nuovo utente dalla lettura della riga */
                try
                {
                    /*  Uso il costruttore CSV */
                    utenti.add(new Utente(riga));
                }
                catch(Exception e)
                {
                    System.err.println("La riga " + riga + " è corrotta: ignorata.");
                }
            }
        } catch (IOException e) {
            System.err.println("Errore lettura utenti: " + e.getMessage());
        }
        return utenti;
    }
    

    /* Questo metodo ricollega gli ID (ISBN/Matricola) agli oggetti veri. Carica i prestiti e ricostruisce i collegamenti tra gli oggetti*/
    public ObservableList<Prestito> caricaPrestitiDaFile(String nomeFile, ObservableList<Utente> utenti, ObservableList<Libro> libri) 
    {
        ObservableList<Prestito> listaPrestiti = FXCollections.observableArrayList();
        File f = new File(nomeFile);
        if (!f.exists())
            return listaPrestiti;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) 
        {
            /* Lettura a vuoto, salto l'intestazione */
            br.readLine(); 
            String riga;
            while ((riga = br.readLine()) != null) 
            {
                if (riga.isEmpty()) 
                    continue;
        /* Ogni volta che viene trovato un ";" 
        spezzo la stringa e inserisco il contenuto in un array di Stringhe */
                String[] chunks = riga.split(";");
                if (chunks.length < 4) continue;

                String matricolaCercata = chunks[0];
                String isbnCercato = chunks[1];
                LocalDate dataInizio = LocalDate.parse(chunks[2]);
                LocalDate dataFine = chunks.length > 3 ? LocalDate.parse(chunks[3]) : LocalDate.now();

                // ---------------------------------------------------------
                // 1. RICERCA DELL'UTENTE (Sostituzione Stream -> Ciclo For)
                // ---------------------------------------------------------
                Utente utenteTrovato = null;

                // Scorro la lista degli utenti caricata in memoria
                for (Utente u : utenti) {
                    // Se la matricola dell'utente corrente corrisponde a quella scritta nel file prestiti...
                    if (u.getMatricola().equals(matricolaCercata)) {
                        utenteTrovato = u; // ...ho trovato l'oggetto giusto!
                        break;             // Interrompo il ciclo, inutile continuare
                    }
                }

                // ---------------------------------------------------------
                // 2. RICERCA DEL LIBRO (Sostituzione Stream -> Ciclo For)
                // ---------------------------------------------------------
                Libro libroTrovato = null;

                // Scorro la lista dei libri caricata in memoria
                for (Libro l : libri) {
                    // Se l'ISBN del libro corrente corrisponde a quello scritto nel file prestiti...
                    if (l.getCodiceISBN().equals(isbnCercato)) {
                        libroTrovato = l; // ...ho trovato l'oggetto giusto!
                        break;            // Interrompo il ciclo
                    }
                }

                // 3. Se esistono entrambi, ricreo il prestito
                try {
                        if (utenteTrovato != null && libroTrovato != null)
                        {
                            Prestito myPrestito = new Prestito(utenteTrovato, libroTrovato, dataInizio, dataFine);

                            if(chunks.length >= 5)
                            {
                                String dataRestString = chunks[4];
                                /* Se la data è diversa da "null", allora è valida */
                                if (!dataRestString.equals("null") && !dataRestString.isEmpty())
                                {
                                    myPrestito.setDataRestituzioneEffettiva(LocalDate.parse(dataRestString));
                                }
                            }

                            listaPrestiti.add(myPrestito);
                        }
                    }
                catch (Exception e)
                {
                    System.err.println("La riga " + riga + " è corrotta: ignorata.");
                }
            }
        } catch (Exception e) {
            System.err.println("Errore lettura prestiti: " + e.getMessage());
        }
        return listaPrestiti;
    }
    
    
    
 
}

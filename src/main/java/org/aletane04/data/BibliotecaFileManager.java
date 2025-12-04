/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.data;

import org.aletane04.model.*;
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
        scriviSuFile(fileName, prestiti,"MATRICOLA_UTENTE;ISBN_LIBRO;DATA_INIZIO;DATA_FINE");
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
                /* Aggiungo un nuovo Libro dalla lettura della riga */
                if (!riga.isEmpty()) 
                    libri.add(new Libro(riga)); 
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
                if (!riga.isEmpty()) 
                    utenti.add(new Utente(riga)); // Uso il costruttore CSV
            }
        } catch (IOException e) {
            System.err.println("Errore lettura utenti: " + e.getMessage());
        }
        return utenti;
    }
    
    // QUESTO È IL METODO PIÙ IMPORTANTE
    // Deve ricollegare gli ID (ISBN/Matricola) agli oggetti veri
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

                String matricola = chunks[0];
                String isbn = chunks[1];
                LocalDate dataInizio = LocalDate.parse(chunks[2]);
                LocalDate dataFine = chunks.length > 3 ? LocalDate.parse(chunks[3]) : LocalDate.now();

                // 1. Cerco l'Utente vero
                Utente u = utenti.stream()
                        .filter(utente -> utente.getMatricola().equals(matricola))
                        .findFirst()
                        .orElse(null);

                // 2. Cerco il Libro vero
                Libro l = libri.stream()
                        .filter(libro -> libro.getCodiceISBN().equals(isbn))
                        .findFirst()
                        .orElse(null);

                // 3. Se esistono entrambi, ricreo il prestito
                if (u != null && l != null) {
                    listaPrestiti.add(new Prestito(u, l, dataInizio, dataFine));
                }
            }
        } catch (Exception e) {
            System.err.println("Errore lettura prestiti: " + e.getMessage());
        }
        return listaPrestiti;
    }
    
    
    
 
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @file BibliotecaFileManager.java
 * @brief Il file contiene l'implementazione della classe 'BibliotecaFileManager'
 *
 * informazioni sul file e il suo ruolo nel progetto
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

    /**
     * @brief Scrive il contenuto di una lista su un file, preceduto da una riga di intestazione.
     *
     * Questa funzione scrive l'intestazione specificata, e poi itera sulla lista fornita, 
     * delegando la formattazione di ciascun oggetto (Libro, Utente o Prestito)
     * al rispettivo metodo toCSV().
     *
     * @pre Il parametro myList deve contenere oggetti che implementano il metodo toCSV() 
     * o essere vuoto.
     * @post I dati contenuti in myList e l'intestazione myFileHeader sono stati scritti 
     * nel file specificato da fileName. In caso di successo, il file viene chiuso.
     *
     * @param[in] fileName Il nome del file su cui scrivere i dati.
     * @param[in] myList La lista generica contenente gli oggetti da salvare 
     * @param[in] myFileHeader La stringa da utilizzare come intestazione del file.
     *
     */
    public void scriviSuFile(String fileName, List<?> myList, String myFileHeader) 
    {
        ///< try-with-resources
        try(PrintWriter pw = new PrintWriter(new FileWriter(fileName)))
        {
            
            pw.println(myFileHeader);

            for(Object obj : myList)
            {
                
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


    /**
     * @brief Salva la lista di tutti gli oggetti Libro su un file specificato.
     *
     * Questa funzione è un metodo specifico che delega l'operazione di scrittura
     * al metodo generico scriviSuFile(), fornendo l'intestazione standard
     * per il file dei libri in formato CSV.
     *
     *
     * @pre La lista libri deve contenere oggetti Libro validi.
     * @post Il file specificato (fileName) è stato creato o sovrascritto, e contiene
     * l'intestazione "TITOLO;AUTORI;DATA;ISBN;COPIE" seguita dai dati di ciascun libro
     * in formato CSV.
     * @param[in] libri La lista di oggetti Libro da rendere persistente.
     * @param[in] fileName Il nome del file su cui salvare i dati.
     */
    public void salvaLibri(List<Libro> libri, String fileName)
    {
        scriviSuFile(fileName, libri,"TITOLO;AUTORI;DATA;ISBN;COPIE");
    }


    /**
     * @brief Salva la lista di tutti gli oggetti Utente su un file specificato.
     *
     * Questa funzione è un metodo specifico che delega l'operazione di scrittura
     * al metodo generico scriviSuFile(), fornendo l'intestazione standard
     * per il file degli utenti in formato CSV.
     *
     * @pre La lista utenti deve contenere oggetti Utente validi.
     * @post Il file specificato (fileName) è stato creato o sovrascritto, e contiene
     * l'intestazione "NOME;COGNOME;MATRICOLA;EMAIL" seguita dai dati di ciascun utente
     * in formato CSV.
     * @param[in] utenti La lista di oggetti Utente da rendere persistente.
     * @param[in] fileName Il nome del file su cui salvare i dati.
     */
    public void salvaUtenti(List<Utente> utenti, String fileName)
    {
        scriviSuFile(fileName, utenti,"NOME;COGNOME;MATRICOLA;EMAIL");
    }


    /**
     * @brief Salva la lista di tutti gli oggetti Prestito su un file specificato.
     *
     * Questa funzione è un metodo specifico che delega l'operazione di scrittura
     * al metodo generico scriviSuFile(), fornendo l'intestazione standard
     * per il file dei libri in formato CSV.
     *
     *
     * @pre La lista prestiti deve contenere oggetti Prestito validi.
     * @post Il file specificato (fileName) è stato creato o sovrascritto, e contiene
     * l'intestazione "MATRICOLA_UTENTE;ISBN_LIBRO;DATA_INIZIO;DATA_FINE;DATA_RESTITUZIONE" seguita dai dati di ciascun prestito
     * in formato CSV.
     * @param[in] prestiti La lista di oggetti Prestito da rendere persistente.
     * @param[in] fileName Il nome del file su cui salvare i dati.
     */
    public void salvaPrestiti(List<Prestito> prestiti, String fileName)
    {
        scriviSuFile(fileName, prestiti,"MATRICOLA_UTENTE;ISBN_LIBRO;DATA_INIZIO;DATA_FINE;DATA_RESTITUZIONE");
    }


    /**
     * @brief Carica e deserializza gli oggetti Libro da un file di testo.
     *
     * Questa funzione legge il file specificato, saltando la prima riga (intestazione),
     * e tenta di creare un nuovo oggetto Libro per ogni riga successiva.
     * Le righe corrotte (che sollevano un'eccezione durante la costruzione del Libro)
     * vengono ignorate e segnalate su System.err.
     *
     *
     *
     * @pre Il file specificato deve esistere. Se non esiste, viene restituita una lista vuota.
     * @pre Il file, se esistente, deve essere un file di testo con la prima riga come intestazione.
     *
     * @return Una lista osservabile contenente tutti gli oggetti Libro
     * caricati con successo dal file. La lista è vuota se il file non esiste o è vuoto.
     *
     */
    public ObservableList<Libro> caricaLibriDaFile(String fileName)
    {
        ObservableList<Libro> libri = FXCollections.observableArrayList();
        File f = new File(fileName);
        if (!f.exists())
            return libri;

        try (BufferedReader br = new BufferedReader(new FileReader(f)))
        {
            
            br.readLine();
            String riga;
            while ((riga = br.readLine()) != null)
            {
                if(riga.isEmpty())
                    continue;
                ///< Aggiunta di un nuovo libro dalla lettura di una riga
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



    /**
     * @brief Carica e deserializza gli oggetti Utente da un file di testo.
     *
     * Questa funzione legge il file specificato, saltando la prima riga (intestazione),
     * e tenta di creare un nuovo oggetto Utente per ogni riga successiva.
     * Le righe corrotte (che sollevano un'eccezione durante la costruzione del Libro)
     * vengono ignorate e segnalate su System.err.
     *
     *
     *
     * @pre Il file specificato deve esistere. Se non esiste, viene restituita una lista vuota.
     * @pre Il file, se esistente, deve essere un file di testo con la prima riga come intestazione.
     *
     * @return Una lista osservabile contenente tutti gli oggetti Utente
     * caricati con successo dal file. La lista è vuota se il file non esiste o è vuoto.
     *
     */
    public ObservableList<Utente> caricaUtentiDaFile(String nomeFile)
    {
        ObservableList<Utente> utenti = FXCollections.observableArrayList();
        File f = new File(nomeFile);
        if (!f.exists())
            return utenti;

        try (BufferedReader br = new BufferedReader(new FileReader(f)))
        {
            
            br.readLine();
            String riga;
            while ((riga = br.readLine()) != null)
            {
                if(riga.isEmpty())
                    continue;
                ///< Aggiunta di un nuovo utente dalla lettura di una riga
                try
                {
                    
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


    ///< Questo metodo ricollega gli ID (ISBN/Matricola) agli oggetti veri. Carica i prestiti e ricostruisce i collegamenti tra gli oggetti
    /**
     * @brief Carica e deserializza gli oggetti Prestito da un file di testo.
     *
     * Questa funzione legge il file specificato, saltando la prima riga (intestazione),
     * e tenta di creare un nuovo oggetto Prestito per ogni riga successiva.
     * Le righe corrotte (che sollevano un'eccezione durante la costruzione del Libro)
     * vengono ignorate e segnalate su System.err.
     *
     *
     *
     * @pre Il file specificato deve esistere. Se non esiste, viene restituita una lista vuota.
     * @pre Il file, se esistente, deve essere un file di testo con la prima riga come intestazione.
     *
     * @return Una lista osservabile contenente tutti gli oggetti Prestito
     * caricati con successo dal file. La lista è vuota se il file non esiste o è vuoto.
     *
     */
    public ObservableList<Prestito> caricaPrestitiDaFile(String nomeFile, ObservableList<Utente> utenti, ObservableList<Libro> libri)
    {
        ObservableList<Prestito> listaPrestiti = FXCollections.observableArrayList();
        File f = new File(nomeFile);
        if (!f.exists())
            return listaPrestiti;

        try (BufferedReader br = new BufferedReader(new FileReader(f)))
        {
            
            br.readLine();
            String riga;
            while ((riga = br.readLine()) != null)
            {
                if (riga.isEmpty())
                    continue;
        /**
         * Ogni volta che viene trovato un ";"
         * la stringa è spezzata e viene inserito il contenuto in un array di Stringhe
         */
                String[] chunks = riga.split(";");
                if (chunks.length < 4) continue;

                String matricolaCercata = chunks[0];
                String isbnCercato = chunks[1];
                LocalDate dataInizio = LocalDate.parse(chunks[2]);
                LocalDate dataFine = chunks.length > 3 ? LocalDate.parse(chunks[3]) : LocalDate.now();


                ///< RICERCA DELL'UTENTE

                Utente utenteTrovato = null;

                ///< la lista degli utenti caricata in memoria viene visitata
                for (Utente u : utenti)
                {
                    ///< Se la matricola dell'utente corrente corrisponde a quella scritta nel file prestiti viene salavata e il ciclo è interrotto
                    if (u.getMatricola().equals(matricolaCercata))
                    {
                        utenteTrovato = u; 
                        break;
                    }
                }

                ///< Se un utente è stato rimosso, ne viene creato uno nuovo fittizio per non perdere lo storico dei prestiti ad esso associato.
                if (utenteTrovato == null)
                {
                    utenteTrovato = new Utente("Utente", "Rimosso", matricolaCercata, "N/A");
                }

                ///< RICERCA DEL LIBRO

                Libro libroTrovato = null;

                ///< Si scorre la lista dei libri caricata in memoria
                for (Libro l : libri) {
                    ///< Se l'ISBN del libro corrente corrisponde a quello scritto nel file prestiti, il libro viene salvato e il ciclo interrotto
                    if (l.getCodiceISBN().equals(isbnCercato)) {
                        libroTrovato = l; 
                        break;
                    }
                }

                ///< Se il libro è stato rimosso dalla biblioteca, ne viene creato uno fittizio per lo storico
                if (libroTrovato == null) {
                    libroTrovato = new Libro("RIMOSSO (ISBN: " + isbnCercato + ")", "N/A", LocalDate.of(1900, 1, 1), isbnCercato, 0);
                }

                ///< Se esistono entrambi, si ricrea il prestito
                try {
                        if (utenteTrovato != null && libroTrovato != null)
                        {
                            Prestito myPrestito = new Prestito(utenteTrovato, libroTrovato, dataInizio, dataFine);

                            if(chunks.length >= 5)
                            {
                                String dataRestString = chunks[4];
                                ///< Se la data è diversa da null, allora è valida 
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @file Prestito.java
 * @brief Il file contiene l'implementazione della classe 'Prestito' per la gestione di prestiti bibliotecari
 *
 * Questo file contiene la definizione della classe Prestito, che funge da entità
 * di associazione tra un Utente e un Libro. La classe è responsabile della gestione
 * del ciclo di vita del prestito, memorizzando la data di inizio e la data di scadenza prevista.
 * Include inoltre la logica necessaria per calcolare dinamicamente lo stato
 * del prestito (Attivo, In Scadenza o Scaduto) confrontando la data di scadenza con la data odierna.
 *
 * @author [angel]
 * @date 08 Dicembre 2025
 * @version 1.0
 */
package org.aletane04.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author 39392
 */
public class Prestito {
    
    private Utente myUtente;
    private Libro myLibro;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    
    public Prestito(Utente u, Libro l, LocalDate dI, LocalDate dF) 
    {

        this.myUtente = u;
        this.myLibro = l;
        this.dataInizio = dI;
        this.dataFine = dF;

    }
    
    /* ASSENZA COSTRUTTORE PER PARSING DA .csv:
        Non è possibile ricostruire un prestito avendo a disposizione unicamente
        le stringhe matricola,ISBN e date, ad esempio.
        Il file .csv non contiene gli oggetti Libro e Utente completi, ma solo gli identificativi.
        É necessario cercare l'utente e il libro negli elenchi appositi.
        É compito del BibliotecaFileManager ricostruire 
        un prestito dal file .csv (non contiene tutti i dati necessari)  
    */
    
    /* Metodi Getter */

    /**
     * @brief Restituisce l'utente che ha effettuato il prestito
     *
     * @pre L'oggetto Prestito è stato inizializzato
     * @post Il valore restituito corrisponde all'attributo interno 'myUtente'
     *
     * @return L'oggetto Utente associato al prestito
     */
    public Utente getUtente() 
    {
        return myUtente;
    }

    /**
     * @brief Restituisce il libro oggetto del prestito
     *
     * @pre L'oggetto Prestito è stato inizializzato
     * @post Il valore restituito corrisponde all'attributo interno 'myLibro
     *
     * @return L'oggetto Libro prestato
     */
    public Libro getLibro() 
    {
        return myLibro;
    }

    /**
     * @brief Restituisce la data in cui il prestito ha avuto inizio
     *
     * @post Il valore restituito è la data di inizio (può essere null se non ancora impostata)
     *
     * @return La data di inizio prestito
     */
    public LocalDate getDataInizio() 
    {
        return dataInizio;
    }

    /**
     * @brief Restituisce la data prevista per la fine del prestito.
     * @return La data di scadenza del prestito.
     */
    public LocalDate getDataFine() 
    {
        return dataFine;
    }

    /**
     * @brief Calcola e restituisce lo stato attuale del prestito.
     * * Lo stato viene determinato dinamicamente confrontando la data odierna
     * con la data di fine prestito:
     * - <b>SCADUTO</b>: Se la data odierna è successiva alla data di fine.
     * - <b>IN_SCADENZA</b>: Se mancano 7 giorni o meno alla scadenza (inclusa).
     * - <b>ATTIVO</b>: In tutti gli altri casi.
     * * @return Il valore dell'enum StatoPrestito corrispondente alla situazione attuale.
     */
    public StatoPrestito getStatoPrestito() 
    {
        LocalDate giornoOdierno = LocalDate.now();
        
        if(giornoOdierno.isAfter(dataFine)) 
        {
            return StatoPrestito.SCADUTO;
        }
        else /* Calcola la quantità di tempo tra i due oggetti passati e verifica
                che sia minore o uguale di 7 */
        if(ChronoUnit.DAYS.between(giornoOdierno, dataFine) <= 7) 
        {
            return StatoPrestito.IN_SCADENZA;
        }
            else
        {
            return StatoPrestito.ATTIVO;
        }
        
    }
    
    /* Metodi Setter */

    /**
     * @brief Imposta l'utente associato al prestito.
     * @param u Il nuovo oggetto Utente da associare.
     */
    public void setUtente(Utente u) 
    {
        this.myUtente = u;
    }

    /**
     * @brief Imposta il libro oggetto del prestito.
     * @param l Il nuovo oggetto Libro da associare.
     */
    public void setLibro(Libro l) 
    {
        this.myLibro = l;
    }

    /**
     * @brief Imposta la data di inizio del prestito.
     * @param dI La nuova data di inizio.
     */
    public void setDataInizio(LocalDate dI) 
    {
        this.dataInizio = dI;
    }

    /**
     * @brief Imposta la data di fine (scadenza) del prestito.
     * @param dF La nuova data di fine.
     */
    public void setDataFine(LocalDate dF) 
    {
        this.dataFine = dF;
    }
    
    /* Altri metodi */

    /**
     * @brief Restituisce i dati del prestito formattati per file CSV.
     * * La stringa contiene i riferimenti chiave del prestito separati da punto e virgola (;).
     * Ordine dei campi: Matricola Utente; ISBN Libro; Data Inizio; Data Fine.
     * * @return Una stringa formattata pronta per la scrittura su file.
     */
    public String toCSV() 
    {
        return this.myUtente.getMatricola()+";"+this.myLibro.getCodiceISBN()+";"+dataInizio+";"+dataFine;
    } 
    
}

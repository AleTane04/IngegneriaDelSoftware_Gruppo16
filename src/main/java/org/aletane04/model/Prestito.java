/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    public Utente getUtente() 
    {
        return myUtente;
    }
    
    public Libro getLibro() 
    {
        return myLibro;
    }
    
    public LocalDate getDataInizio() 
    {
        return dataInizio;
    }
    
    public LocalDate getDataFine() 
    {
        return dataFine;
    }
    
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
    public void setUtente(Utente u) 
    {
        this.myUtente = u;
    }
    
    public void setLibro(Libro l) 
    {
        this.myLibro = l;
    }
    
    public void setDataInizio(LocalDate dI) 
    {
        this.dataInizio = dI;
    }
    
    public void setDataFine(LocalDate dF) 
    {
        this.dataFine = dF;
    }
    
    /* Altri metodi */
    
    /* Metodo per restituire le proprietà di un libro 
    in una stringa adatta per il formato file .csv, i cui campi sono separati da ;  */
    /* Salvo: Maricola;ISBN_Libro,DataInizio;DataFine */
    public String toCSV() 
    {
        return this.myUtente.getMatricola()+";"+this.myLibro.getCodiceISBN()+";"+dataInizio+";"+dataFine;
    } 
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.model;

import java.time.LocalDate;

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
    
    public String getStatoPrestito() 
    {
        
        
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
    
}

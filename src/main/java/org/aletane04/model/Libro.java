/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author angel
 */
public class Libro implements Serializable{
    private String titolo;
    private String autori;
    private LocalDate annoPublicazione;
    private final String codiceISBN;
    private int quantità;
    

    public Libro(String titolo, String autori, int annoPub, int mesePub, int giornoPub, String codiceISBN, int quantità){

        this.titolo=titolo;
        this.autori=autori;
        this.annoPublicazione=LocalDate.of(annoPub, mesePub, giornoPub);
        this.codiceISBN=codiceISBN;
        this.quantità=quantità;
    }
    
    //metodi getter
    public String getTitolo(){
        return titolo;
    }
    
    public String getAutori(){
        return autori;
    }
    
    public LocalDate getAnnoPublicazione(){
        return annoPublicazione;
    }
    
    public String getCodice(){
        return codiceISBN;
    }
    
    public int getQuantità(){
        return quantità;
    }
    
    
    //metodi setter
    public void setTitolo(String titolo){
        this.titolo=titolo;
    }
    
    public void setAutori(String autori){
        this.autori=autori;
    }
    
    public void setAnnoPublicazione(int annoPub, int mesePub, int giornoPub){
        this.annoPublicazione=LocalDate.of(annoPub, mesePub, giornoPub);
    }
    
    public void setQuantità(int quantità){
        this.quantità=quantità;
    }
    
    @Override
    public boolean equals(Object obj) {
        /* Verifica dei casi degeneri */
        if (obj == null) return false;
        if (this == obj) return true;

        /* Verifica che appartengano alla stessa classe */
        if (this.getClass() != obj.getClass()) 
            return false;
        /* Downcast sicuro; in obj ho un riferimento di un libro */
        Libro myLibro = (Libro) obj;

        /* Essendo all'interno della stessa classe, accedo all'attributo "codice", che è privato, di myLibro */
        return this.codiceISBN == myLibro.codiceISBN;
    }

    @Override
    public int hashCode(){
        return codiceISBN == null ? 0 : codiceISBN.hashCode();

       
    }
    
    @Override
    public String toString(){
        return "Titolo = " + getTitolo() + ", Autori = " + getAutori() + ", Anno di Publicazione = " + getAnnoPublicazione() + ", Codice ISBN = " + getCodice() + ", Copie disponibili = " + getQuantità() + "\n";
    }
    
}

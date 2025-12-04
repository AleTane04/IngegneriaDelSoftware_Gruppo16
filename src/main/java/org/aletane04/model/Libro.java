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
public class Libro{
    private String titolo;
    private String autori;
    private LocalDate annoPubblicazione;
    private final String codiceISBN;
    private int numeroCopieDisponibili;
    

    public Libro(String titolo, String autori, int annoPub, int mesePub, int giornoPub, String codiceISBN, int numeroCopie){

        this.titolo=titolo;
        this.autori=autori;
        this.annoPubblicazione=LocalDate.of(annoPub, mesePub, giornoPub);
        this.codiceISBN=codiceISBN;
        this.numeroCopieDisponibili=numeroCopie;
    }
    
    /* Data una riga del file .csv in input, la spezzo in un array di stringhe, converto 
    i valori nei tipi giusti e popolo i campi del mio costruttore */
    public Libro(String rigaCSV) 
    {
        /* Ogni volta che viene trovato un ";" 
        spezzo la stringa e inserisco il contenuto in un array di Stringhe */
        String[] arrayChunks = rigaCSV.split(";");
        
        this.titolo=arrayChunks[0];
        this.autori=arrayChunks[1];
        /* Da Stringa a LocalDate */
        this.annoPubblicazione = LocalDate.parse(arrayChunks[2]);
        this.codiceISBN=arrayChunks[3];
        /* Da Stringa a intero */
        this.numeroCopieDisponibili=Integer.parseInt(arrayChunks[4]);
        
    
    }
    
    //metodi getter
    public String getTitolo(){
        return titolo;
    }
    
    public String getAutori(){
        return autori;
    }
    
    public LocalDate getAnnoPubblicazione(){
        return annoPubblicazione;
    }
    
    public String getCodiceISBN(){
        return codiceISBN;
    }
    
    public int getNumeroCopieDisponibili(){
        return numeroCopieDisponibili;
    }
    
    
    //metodi setter
    public void setTitolo(String titolo){
        this.titolo=titolo;
    }
    
    public void setAutori(String autori){
        this.autori=autori;
    }
    
    public void setAnnoPubblicazione(int annoPub, int mesePub, int giornoPub){
        this.annoPubblicazione=LocalDate.of(annoPub, mesePub, giornoPub);
    }
    
    public void setNumeroCopieDisponibili(int q){
        this.numeroCopieDisponibili=q;
    }
    
    /* Altri metodi */
    
    public void decrementaNumeroCopieDisponibili() 
    {
        if(this.numeroCopieDisponibili > 0)
            this.numeroCopieDisponibili--;
    }
    
    public void incrementaNumeroCopieDisponibili() 
    {
        this.numeroCopieDisponibili++;
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
        return "Titolo = " + getTitolo() + ", Autori = " + getAutori() + ", Anno di Publicazione = " + getAnnoPubblicazione() + ", Codice ISBN = " + getCodiceISBN() + ", Copie disponibili = " + getNumeroCopieDisponibili() + "\n";
    }
    
    /* Metodo per trasformare un oggetto libro in una stringa, i cui campi sono separati da ;  */
    
    public String toCSV() {
        return titolo+";"+autori+";"+annoPubblicazione.toString()+";"+codiceISBN+";"+numeroCopieDisponibili;
    }
    
    
}

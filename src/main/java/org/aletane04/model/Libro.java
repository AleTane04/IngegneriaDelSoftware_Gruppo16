/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.aletane04.model;


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
    

    public Libro(String titolo, String autori, LocalDate anno, String codiceISBN, int numeroCopie){

        this.titolo=titolo;
        this.autori=autori;
        this.annoPubblicazione=anno;
        this.codiceISBN=codiceISBN;
        this.numeroCopieDisponibili=numeroCopie;
    }
    
    /** 
    *@brief passa una riga di un file .csv al costruttore
    *
    *Data una riga del file .csv in input, la spezzo in un array di stringhe, converto 
    *i valori nei tipi giusti e popolo i campi del mio costruttore 
    *
    *@param[inout] rigaCSV la riga del file .csv passata
    */
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
    /**
     * @brief Restituisce il titolo del libro.
     * * @return Il titolo del libro.
     */
    public String getTitolo(){
        return titolo;
    }

    /**
     * @brief Restituisce gli autori del libro.
     * * @return Una stringa contenente i nomi degli autori.
     */
    public String getAutori(){
        return autori;
    }

    /**
     * @brief Restituisce la data di pubblicazione del libro.
     * * @return La data di pubblicazione (oggetto LocalDate).
     */
    public LocalDate getAnnoPubblicazione(){
        return annoPubblicazione;
    }

    /**
     * @brief Restituisce il codice ISBN del libro.
     * * @return Il codice ISBN.
     */
    public String getCodiceISBN(){
        return codiceISBN;
    }

    /**
     * @brief Restituisce il numero attuale di copie disponibili.
     * * @return Il numero di copie disponibili.
     */
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
    
    public void setAnnoPubblicazione(LocalDate data){
        this.annoPubblicazione=data;
    }
    
    public void setNumeroCopieDisponibili(int q){
        this.numeroCopieDisponibili=q;
    }
    
    /* Altri metodi */
    /**
    *@brief decrementa il numero di copie di un libro disponibili
    *
    *Decrementa il numero di copie di un libro disponibili solo se esso è maggiore di 0 
    */
    public void decrementaNumeroCopieDisponibili() 
    {
        if(this.numeroCopieDisponibili > 0)
            this.numeroCopieDisponibili--;
    }

    /**
    *@brief incrementa il numero di copie di un libro disponibili 
    */
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
        return java.util.Objects.equals(this.codiceISBN, myLibro.codiceISBN);
    }

    @Override
    public int hashCode(){
        return codiceISBN == null ? 0 : codiceISBN.hashCode();

       
    }
    
    @Override
    public String toString(){
        return "Titolo = " + getTitolo() + ", Autori = " + getAutori() + ", Anno di Publicazione = " + getAnnoPubblicazione() + ", Codice ISBN = " + getCodiceISBN() + ", Copie disponibili = " + getNumeroCopieDisponibili() + "\n";
    }
    
    /* Metodo per restituire le proprietà di un libro 
    in una stringa adatta per il formato file .csv, i cui campi sono separati da ;  */
    
    public String toCSV() {
        return titolo+";"+autori+";"+annoPubblicazione.toString()+";"+codiceISBN+";"+numeroCopieDisponibili;
    }
    
    
}

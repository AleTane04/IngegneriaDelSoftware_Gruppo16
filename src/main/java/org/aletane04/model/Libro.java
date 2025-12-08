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
     * @brief passa una riga di un file .csv al costruttore
     *
     * Data una riga del file .csv in input, la spezzo in un array di stringhe, converto 
     * i valori nei tipi giusti e popolo i campi del mio costruttore 
     *
     * @pre La stringa non deve essere null
     * @pre rigaCSV deve contenere esattamente 5 campi separati da ';'
     * @pre Il campo data deve essere nel formato ISO (YYYY-MM-DD) 
     * @post Una nuova istanza di Libro è inizializzata con i rispettivi campi popolati dai valori estratti dalla stringa 
     *
     * @param[in] rigaCSV la riga del file .csv passata
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
     * @brief Restituisce il titolo del libro
     *
     * @pre L'oggetto deve essere istanziato
     * @post Il valore restituito corrisponde all'attributo interno 'titolo'
     *
     * @return Il titolo del libro.
     */
    public String getTitolo(){
        return titolo;
    }

    /**
     * @brief Restituisce gli autori del libro
     *
     * @post Il valore restituito corrisponde all'attributo interno 'autori'
     *
     * @return Una stringa contenente i nomi degli autori
     */
    public String getAutori(){
        return autori;
    }

    /**
     * @brief Restituisce la data di pubblicazione del libro
     *
     * @post Il valore restituito corrisponde all'attributo interno 'annoPubblicazione'
     *
     * @return La data di pubblicazione 
     */
    public LocalDate getAnnoPubblicazione(){
        return annoPubblicazione;
    }

    /**
     * @brief Restituisce il codice ISBN del libro
     *
     * @post Il valore restituito corrisponde all'attributo interno 'codiceISBN'
     *
     * @return Il codice ISBN
     */
    public String getCodiceISBN(){
        return codiceISBN;
    }

    /**
     * @brief Restituisce il numero attuale di copie disponibili
     *
     * @post Il valore restituito corrisponde all'attributo interno 'numeroCopieDisponibili'
     *
     * @return Il numero di copie disponibili
     */
    public int getNumeroCopieDisponibili(){
        return numeroCopieDisponibili;
    }
    
    
    //metodi setter
    
     /**
     * @brief Imposta o aggiorna il titolo del libro
     * 
     * @pre titolo non deve essere null
     * @post this.titolo è uguale al parametro 'titolo'
     * 
     * @param[in] titolo Il nuovo titolo da assegnare al libro
     */
    public void setTitolo(String titolo){
        this.titolo=titolo;
    }

    /**
     * @brief Imposta o aggiorna gli autori del libro
     *
     * @pre autori non deve essere null
     * @post this.autori è uguale al parametro 'autori'
     *
     * @param[in] autori La stringa contenente i nuovi autori
     */
    public void setAutori(String autori){
        this.autori=autori;
    }

    /**
     * @brief Imposta la data di pubblicazione del libro
     *
     * @pre data non deve essere null
     * @post this.annoPubblicazione è uguale al parametro 'data'
     *
     * @param[in] data La nuova data di pubblicazione
     */
    public void setAnnoPubblicazione(LocalDate data){
        this.annoPubblicazione=data;
    }

    /**
     * @brief Sovrascrive il numero di copie disponibili
     * * @param q La nuova quantità di copie da impostare
     */
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

    /**
     * @brief Verifica l'uguaglianza tra questo libro e un altro oggetto
     * 
     * Due libri sono considerati uguali se possiedono lo stesso codice ISBN
     * Viene effettuato un controllo preliminare sui riferimenti e sulla classe di appartenenza
     *
     * @param[in] obj L'oggetto da confrontare con l'istanza corrente
     * @return true se gli oggetti sono uguali (stesso ISBN), false altrimenti
     */
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


    /**
     * @brief Calcola il codice hash per il libro.
     *
     * Il calcolo è basato esclusivamente sul codice ISBN per garantire la coerenza 
     * con il metodo equals(). Se l'ISBN è null, restituisce 0.
     *
     * @return Un intero che rappresenta il codice hash dell'oggetto.
     */
    @Override
    public int hashCode(){
        return codiceISBN == null ? 0 : codiceISBN.hashCode();

       
    }

    /**
     * @brief Restituisce una rappresentazione testuale completa del libro.
     * 
     * La stringa include titolo, autori, anno di pubblicazione, ISBN e numero di copie.
     *
     * @return Una stringa descrittiva leggibile dall'utente.
     */
    @Override
    public String toString(){
        return "Titolo = " + getTitolo() + ", Autori = " + getAutori() + ", Anno di Publicazione = " + getAnnoPubblicazione() + ", Codice ISBN = " + getCodiceISBN() + ", Copie disponibili = " + getNumeroCopieDisponibili() + "\n";
    }
    
    
    /**
     * @brief Restituisce i dati del libro formattati per file .csv.
     *
     *I campi sono concatenati e separati da un punto e virgola (;).
     * Ordine campi: titolo;autori;anno;ISBN;copie.
     *
     * @return Una stringa pronta per essere scritta in un file .csv.
     */
    public String toCSV() {
        return titolo+";"+autori+";"+annoPubblicazione.toString()+";"+codiceISBN+";"+numeroCopieDisponibili;
    }
    
    
}

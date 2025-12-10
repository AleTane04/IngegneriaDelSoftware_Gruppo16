/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.softeng.model;

/**
 *
 * @author angel
 */
/**
 * @file Utente.java
 * @brief Il file contiene l'implementazione della classe 'Utente'
 *
 * informazioni sul file e il suo ruolo nel progetto 
 */
public class Utente{
    private String nome;
    private String cognome;
    private final String matricola;
    private String email;
    
    /** @brief Costruttore della classe Utente
     *  Inizializza una nuova istanza della classe Utente.
     *  @param[in] nome Il nome dell'utente
     *  @param[in] cognome il cognome dell'utente
     *  @param[in] matricola La matricola dell'utente
     *  @param[in] email L'email istituzionale dell'utente
     *  @return new Utente
     *  @post L'oggetto creato della classe Utente è in uno stato coerente e valido
     */
    public Utente(String nome, String cognome, String matricola, String email){
        this.nome=nome;
        this.cognome=cognome;
        this.matricola=matricola;
        this.email=email;
        
    }
    
    /** @brief Costruttore della classe Utente (versione con riga del file .csv)
     *  Data una riga del file .csv in input, la spezzo in un array di stringhe, converto 
     *  i valori nei tipi giusti e popolo i campi del mio costruttore.
     *  @param[in] rigaCSV La riga del file .csv
     *  @return new Utente
     *  @post L'oggetto creati della classe Utente è in uno stato coerente e valido
     */
    public Utente(String rigaCSV) 
    {
        /** Ogni volta che viene trovato un ";" 
         * spezzo la stringa e inserisco il contenuto in un array di Stringhe 
         */
        String[] arrayChunks = rigaCSV.split(";");
        
        this.nome=arrayChunks[0];
        this.cognome=arrayChunks[1];
        this.matricola = arrayChunks[2];
        this.email=arrayChunks[3];
     
    }
    
    
    ///<metodi getter
    /** @brief Restituisce il nome dell'utente
     *  
     *  @post Il valore restituito corrisponde all'attributo interno 'nome'
     *  
     *  @return nome
     */
   public String getNome(){
       return nome;
   }


    /** @brief Restituisce il cognome dell'utente
     *  
     *  @post Il valore restituito corrisponde all'attributo interno 'cognome'
     *  
     *  @return cognome
     */
   public String getCognome(){
       return cognome;
   }



    /** @brief Restituisce la matricola dell'utente
     *  
     *  @post Il valore restituito corrisponde all'attributo interno 'matricola'
     *  
     *  @return matricola
     */
   public String getMatricola(){
       return matricola;
   }


    /** @brief Restituisce l'email istituzionale dell'utente
     *  
     *  @post Il valore restituito corrisponde all'attributo interno 'email'
     *  
     *  @return email
     */
   public String getEmail(){
       return email;
   }
   
   ///<metodi setter


    /** @brief Imposta e aggiorna il nome dell'utente
     *  
     *  @pre nome non deve essere null
     *  @post this.nome è uguale al parametro 'nome'
     *
     *  @param[in] nome Il nome dell'utente
     */
   public void setNome(String nome){
       this.nome=nome;
   }
   public void setCognome(String cognome){
       this.cognome=cognome;
   }
   public void setEmail(String email){
       this.email=email;
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
        Utente myUtente = (Utente) obj;

        /* Essendo all'interno della stessa classe, accedo all'attributo "codice", che è privato, di myLibro */
        return java.util.Objects.equals(this.matricola, myUtente.matricola);
   }

    @Override
    public int hashCode(){
        return matricola == null ? 0 : matricola.hashCode();

       
    }
    
    @Override
    public String toString(){
        return "Nome = " + getNome() + ", Cognome = " + getCognome() + ", Matricola = " + getMatricola() + ", Email istituzionale = " + getEmail() + "\n";
    }
    
    /* Metodo per restituire le proprietà di un libro 
    in una stringa adatta per il formato file .csv, i cui campi sono separati da ;  */
    public String toCSV() {
        return nome+";"+cognome+";"+matricola+";"+email;
    }
}

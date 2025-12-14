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
     *  @return Un nuovo oggetto della classe Utente
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
     *  @return Un nuovo oggetto della classe Utente
     *  @post L'oggetto creato della classe Utente è in uno stato coerente e valido
     */
    public Utente(String rigaCSV) 
    {
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
     *  @return Il nome dell'utente
     */
   public String getNome(){
       return nome;
   }


    /** @brief Restituisce il cognome dell'utente
     *  
     *  @post Il valore restituito corrisponde all'attributo interno 'cognome'
     *  
     *  @return Il cognome dell'utente
     */
   public String getCognome(){
       return cognome;
   }



    /** @brief Restituisce la matricola dell'utente
     *  
     *  @post Il valore restituito corrisponde all'attributo interno 'matricola'
     *  
     *  @return La matricola dell'utente
     */
   public String getMatricola(){
       return matricola;
   }


    /** @brief Restituisce l'email istituzionale dell'utente
     *  
     *  @post Il valore restituito corrisponde all'attributo interno 'email'
     *  
     *  @return L'email istituzionale dell'utente
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

    /** @brief Imposta e aggiorna il cognome dell'utente
     *  
     *  @pre cognome non deve essere null
     *  @post this.cognome è uguale al parametro 'cognome'
     *
     *  @param[in] cognome Il cognome dell'utente
     */
   public void setCognome(String cognome){
       this.cognome=cognome;
   }

    /** @brief Imposta e aggiorna l'email dell'utente
     *  
     *  @pre email non deve essere null
     *  @post this.email è uguale al parametro 'email'
     *
     *  @param[in] email L'email istituzionale dell'utente
     */
   public void setEmail(String email){
       this.email=email;
   }


    /**
     * @brief Verifica l'uguaglianza tra questo utente e un altro oggetto
     * 
     * Due utenti sono considerati uguali se possiedono lo stesso 
     * Viene effettuato un controllo preliminare sui riferimenti e sulla classe di appartenenza
     *
     * @param[in] obj L'oggetto da confrontare con l'istanza corrente
     * @post Restituisce true se obj è un Utente e ha la stessa matricola, false altrimenti
     *
     * @return true se gli oggetti sono uguali (stessa matricola), false altrimenti
     */
   @Override
    public boolean equals(Object obj) { 
        if (obj == null) return false;
        if (this == obj) return true; 
        if (this.getClass() != obj.getClass()) 
            return false; 
        Utente myUtente = (Utente) obj; 
        return this.matricola.equals(myUtente.matricola);
   }


    /**
     * @brief Calcola il codice hash per l'utente.
     *
     * Il calcolo è basato esclusivamente sulla matricola per garantire la coerenza 
     * con il metodo equals(). Se la matricola è null, restituisce 0.
     *
     * @return Un intero che rappresenta il codice hash dell'oggetto.
     */
    @Override
    public int hashCode(){
        return matricola == null ? 0 : matricola.hashCode();


    }

    
    /**
     * @brief Restituisce una rappresentazione testuale completa dell'utente.
     * 
     * La stringa include nome, cognome, matricola e email istituzionale.
     *
     * @post La stringa restituita non è null e contiene i dati aggiornati dell'utente
     *
     * @return Una stringa descrittiva leggibile dall'utente.
     */
    @Override
    public String toString(){
        return "Nome = " + getNome() + ", Cognome = " + getCognome() + ", Matricola = " + getMatricola() + ", Email istituzionale = " + getEmail() + "\n";
    }
    
    /**
     * @brief Restituisce i dati dell'utente formattati per file .csv.
     *
     * I campi sono concatenati e separati da un punto e virgola (;).
     * Ordine campi: nome;cognome;matricola;email.
     *
     * @post La stringa contiene 4 campi separati da ';'
     *
     * @return Una stringa pronta per essere scritta in un file .csv.
     */
    public String toCSV()
    {
        String safeNome = (nome != null) ? nome.replace(";", "") : "";
        String safeCognome = (cognome != null) ? cognome.replace(";", "") : "";
        String safeMatricola = (matricola != null) ? matricola.replace(";", "") : "";
        String safeEmail = (email != null) ? email.replace(";", "") : "";

        return safeNome + ";" + safeCognome + ";" + safeMatricola + ";" + safeEmail;
    }
}

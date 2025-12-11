/** 
 * @file UtenteGiaPresenteException.java
 * @brief Il file contiene l'implementazione dell'eccezione UtenteGiaPresenteException
 * 
 * informazioni sul file
 */

package org.softeng.exceptions;

public class UtenteGiaPresenteException extends Exception
{
    /**
     * @brief Costruttore predefinito per UtenteGiaPresenteException.
     *
     * Crea una nuova istanza di UtenteGiaPresenteException senza un
     * messaggio di dettaglio. Questa eccezione è sollevata quando si tenta
     * di registrare o aggiungere un nuovo utente alla biblioteca, 
     * ma un utente con la stessa matricola è già presente nel sistema.
     */
    public UtenteGiaPresenteException()
    {

    }

    /**
     * @brief Costruttore predefinito per UtenteGiaPresenteException.
     *
     * Crea una nuova istanza di UtenteGiaPresenteException con un
     * messaggio di dettaglio. Questa eccezione è sollevata quando si tenta
     * di registrare o aggiungere un nuovo utente alla biblioteca, 
     * ma un utente con la stessa matricola è già presente nel sistema.
     *
     * param[in] message Il messaggio di dettaglio
     */
    public UtenteGiaPresenteException(String message)
    {
        super(message);
    }
}

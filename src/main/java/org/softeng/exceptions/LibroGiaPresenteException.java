/** 
 * @file LibroGiaPresenteException.java
 * @brief Il file contiene l'implementazione dell'eccezione LibroGiaPresenteException
 * 
 * informazioni sul file
 */

package org.softeng.exceptions;

public class LibroGiaPresenteException extends Exception
{

    /**
     * @brief Costruttore predefinito per LibroGiaPresenteException.
     *
     * Crea una nuova istanza di LibroGiaPresenteException senza un
     * messaggio di dettaglio. Questa eccezione è sollevata
     * quando si tenta di aggiungere un libro alla biblioteca, 
     * ma una copia con lo stesso codice ISBN è già stata registrata.
     */
    public LibroGiaPresenteException()
    {

    }

    /**
     * @brief Costruttore predefinito per LibroGiaPresenteException.
     *
     * Crea una nuova istanza di LibroGiaPresenteException con un
     * messaggio di dettaglio. Questa eccezione è sollevata
     * quando si tenta di aggiungere un libro alla biblioteca, 
     * ma una copia con lo stesso codice ISBN è già stata registrata.
     *
     * @param[in] message Il messaggio di dettaglio
     */
    public LibroGiaPresenteException(String message)
    {
        super(message);
    }
}

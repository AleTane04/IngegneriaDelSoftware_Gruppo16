/**
 * @file StatoPrestito.java
 * @brief Enumerazione per la gestione degli stati di un prestito.
 * @author 39392
 * @date 04 Dicembre 2025
 * @version 1.0
 */
package org.softeng.model;

/**
 * @brief Definisce i possibili stati di un prestito all'interno del sistema.
 * Questa enumerazione viene utilizzata per tracciare il ciclo di vita di un prestito
 * (dall'apertura alla chiusura) e per determinare la colorazione o la visualizzazione
 * nelle tabelle (TableView) della UI realizzata in JavaFX.
 */
public enum StatoPrestito 
{
    ATTIVO, ///< Il prestito è in corso e la data di scadenza non è ancora prossima.
    IN_SCADENZA, ///< Il prestito è in corso ma la data di scadenza è ormai prossima (es. entro 7 giorni).
    SCADUTO, ///< La data di scadenza è stata superata e il libro non è stato ancora restituito.
    RESTITUITO; ///< Il libro è stato restituito correttamente e il prestito è considerato concluso. Viene mantenuto lo storico dei prestiti nella tabella.

    /**
     * @brief Restituisce una descrizione testuale leggibile dello stato del prestito.
     * Questo metodo sovrascrive il toString di default per fornire stringhe
     * più leggibili per l'utente da mostrare nella GUI,
     * invece del nome tecnico della costante in maiuscolo.
     * @return Una stringa descrittiva associata allo stato corrente (es. "Prestito attivo").
     * @post La stringa restituita non è mai null (al caso 'default' corrisponde la stringa 'Stato del Prestito').
     */
    @Override
    public String toString() 
    {
        switch(this) 
        {
            case ATTIVO: return "Prestito attivo";
            case IN_SCADENZA: return "Prestito in scadenza";
            case SCADUTO: return "Prestito scaduto";
            case RESTITUITO: return "Prestito concluso";
            default: return "Stato del Prestito";
        }
    }
    
}


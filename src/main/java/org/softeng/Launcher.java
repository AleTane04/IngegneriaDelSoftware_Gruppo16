/**
 * @file Launcher.java
 * @brief Entry point ausiliario per l'avvio dell'applicazione.
 * @author 39392 (GAF)
 * @date 05 Dicembre 2025
 * @version 1.0
 */
package org.softeng;

/**
 * @brief Classe di avvio (Launcher) per l'applicazione JavaFX.
 * Questa classe funge da punto di ingresso "secondario" per l'applicazione.
 * È necessaria per creare un file. jar eseguibile, che dunque funzioni correttamente
 * su versioni recenti di Java, senza dover configurare moduli aggiuntivi.
 * Il funzionamento si realizza grazie alla mancata estensione della classe
 * 'Application' di JavaFX; grazie a tale stratagemma, la JVM può caricare
 * le librerie necessarie prima di lanciare la classe Main.
 */
public class Launcher 
{
    /**
     * @brief Metodo principale (Main).
     * Reindirizza immediatamente l'esecuzione al metodo main della classe principale dell'applicazione.
     * @param[in] args Argomenti da riga di comando passati all'avvio del programma.
     */
    public static void main(String[] args) 
    {
        ///< Chiamo il main della classe che estende Application 
        Main.main(args);
    }
    
}

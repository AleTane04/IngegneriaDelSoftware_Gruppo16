package org.softeng.model;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrestitoTest {

    private Prestito prestito;
    private Utente utente;
    private Libro libro;
    private LocalDate inizio;
    private LocalDate fine;

    @BeforeAll
    public static void setUpClass() {
        System.out.println("Inizio test classe PrestitoTest");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("Fine test classe PrestitoTest");
    }

    @BeforeEach
    public void setUp() {
        
        utente = new Utente("Mario;Rossi;12345;mariorossi@studenti.unisa.it");

      
        libro = new Libro("Delitto e castigo", "Fedor Dostoevskij",LocalDate.of(1866, 1, 1), "9781234567890", 5);

       
        inizio = LocalDate.of(2025, 12, 1);
        fine = LocalDate.of(2025, 12, 14);

        prestito = new Prestito(utente, libro, inizio, fine);
    }

    @AfterEach
    public void tearDown() {
        prestito = null;
        utente = null;
        libro = null;
    }

    

    @Test
    public void testGetUtente() {
        assertEquals(utente, prestito.getUtente());
    }

    @Test
    public void testGetLibro() {
        assertEquals(libro, prestito.getLibro());
    }

    @Test
    public void testGetDataInizio() {
        assertEquals(inizio, prestito.getDataInizio());
    }

    @Test
    public void testGetDataFine() {
        assertEquals(fine, prestito.getDataFine());
    }

    @Test
    public void testGetDataRestituzioneEffettiva() {
        
        assertNull(prestito.getDataRestituzioneEffettiva());

        
        LocalDate restituzione = LocalDate.of(2025, 12, 10);
        prestito.setDataRestituzioneEffettiva(restituzione);
        assertEquals(restituzione, prestito.getDataRestituzioneEffettiva());
    }

    @Test
    public void testGetStatoPrestito() {
        
    LocalDate oggi = LocalDate.now();

    prestito.setDataInizio(oggi.minusDays(1));
    prestito.setDataFine(oggi.plusDays(10)); // stato ATTIVO
    assertEquals(StatoPrestito.ATTIVO, prestito.getStatoPrestito());

    prestito.setDataFine(oggi.plusDays(5)); // stato IN_SCADENZA
    assertEquals(StatoPrestito.IN_SCADENZA, prestito.getStatoPrestito());

    prestito.setDataFine(oggi.minusDays(1)); // stato SCADUTO
    assertEquals(StatoPrestito.SCADUTO, prestito.getStatoPrestito());

    prestito.setDataRestituzioneEffettiva(oggi); // stato RESTITUITO
    assertEquals(StatoPrestito.RESTITUITO, prestito.getStatoPrestito());
    }

    

    @Test
    public void testSetUtente() {
        Utente nuovoUtente = new Utente("Fabrizio;Verdi;34576;fabriziorossi@studenti.unisa.it");
        prestito.setUtente(nuovoUtente);
        assertEquals(nuovoUtente, prestito.getUtente());
    }

    @Test
    public void testSetLibro() {
        Libro nuovoLibro = new Libro("I Promessi sposi", "Alessandro Manzoni",
                LocalDate.of(1827, 1, 1), "12345679876543", 3);
        prestito.setLibro(nuovoLibro);
        assertEquals(nuovoLibro, prestito.getLibro());
    }

    @Test
    public void testSetDataInizio() {
        LocalDate nuovaData = LocalDate.of(2025, 12, 2);
        prestito.setDataInizio(nuovaData);
        assertEquals(nuovaData, prestito.getDataInizio());
    }

    @Test
    public void testSetDataFine() {
        LocalDate nuovaFine = LocalDate.of(2025, 12, 16);
        prestito.setDataFine(nuovaFine);
        assertEquals(nuovaFine, prestito.getDataFine());
    }

    @Test
    public void testSetDataRestituzioneEffettiva() {
        LocalDate restituzione = LocalDate.of(2025, 12, 10);
        prestito.setDataRestituzioneEffettiva(restituzione);
        assertEquals(restituzione, prestito.getDataRestituzioneEffettiva());
    }


    @Test
    public void testToCSV() {
        String expected = utente.getMatricola() + ";" + libro.getCodiceISBN() + ";" + inizio + ";" + fine + ";null";
        assertEquals(expected, prestito.toCSV());

        
        LocalDate restituzione = LocalDate.of(2025, 12, 10);
        prestito.setDataRestituzioneEffettiva(restituzione);
        String expectedRest = utente.getMatricola() + ";" + libro.getCodiceISBN() + ";" + inizio + ";" + fine + ";" + restituzione;
        assertEquals(expectedRest, prestito.toCSV());
    }
}

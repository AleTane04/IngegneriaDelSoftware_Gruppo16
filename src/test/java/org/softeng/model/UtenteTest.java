package org.softeng.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtenteTest {
    
    private Utente utente;

    // ---- COSTRUTTORI ----
    
    @Test
    public void testCostruttore() {
        Utente u = new Utente("Luigi", "Rossi", "12345", "luigirossi@studenti.unisa.it");

        assertEquals("Luigi", u.getNome());
        assertEquals("Rossi", u.getCognome());
        assertEquals("12345", u.getMatricola());
        assertEquals("luigirossi@studenti.unisa.it", u.getEmail());
    }

    @Test
    public void testCostruttoreCSV() {
        String rigaCSV = "Luigi;Rossi;12345;luigirossi@studenti.unisa.it";
        Utente u = new Utente(rigaCSV);

        assertEquals("Luigi", u.getNome());
        assertEquals("Rossi", u.getCognome());
        assertEquals("12345", u.getMatricola());
        assertEquals("luigirossi@studenti.unisa.it", u.getEmail());
    }

    // ---- BEFORE / AFTER ----

    @BeforeAll
    public static void setUpClass() {
        System.out.println("Inizio test classe Utente");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("Fine test classe Utente");
    }

    @BeforeEach
    public void setUp() {
        utente = new Utente("Luigi", "Rossi", "12345", "luigirossi@studenti.unisa.it");
    }

    @AfterEach
    public void tearDown() {
        utente = null;
    }

    // ---- GETTER ----

    @Test
    public void testGetNome() {
        assertEquals("Luigi", utente.getNome());
    }

    @Test
    public void testGetCognome() {
        assertEquals("Rossi", utente.getCognome());
    }

    @Test
    public void testGetMatricola() {
        assertEquals("12345", utente.getMatricola());
    }

    @Test
    public void testGetEmail() {
        assertEquals("luigirossi@studenti.unisa.it", utente.getEmail());
    }

    // ---- SETTER ----

    @Test
    public void testSetNome() {
        utente.setNome("Mario");
        assertEquals("Mario", utente.getNome());
    }

    @Test
    public void testSetCognome() {
        utente.setCognome("Bianchi");
        assertEquals("Bianchi", utente.getCognome());
    }

    @Test
    public void testSetEmail() {
        utente.setEmail("mariobianchi@studenti.unisa.it");
        assertEquals("mariobianchi@studenti.unisa.it", utente.getEmail());
    }

    // ---- EQUALS ----

    @Test
    public void testEqualsTrue() {
        Utente altro = new Utente("Mario", "Bianchi", "12345", "mbianchi@studenti.unisa.it");
        assertTrue(utente.equals(altro)); // stessa matricola => UGUAGLIANZA
    }

    @Test
    public void testEqualsFalse() {
        Utente altro = new Utente("Luigi", "Rossi", "99999", "luigirossi@studenti.unisa.it");
        assertFalse(utente.equals(altro));
    }

    // ---- HASHCODE ----

    @Test
    public void testHashCode() {
        assertEquals("12345".hashCode(), utente.hashCode());
    }

    // ---- TO STRING ----

    @Test
    public void testToString() {
        String s = utente.toString();
        assertTrue(s.contains("Luigi"));
        assertTrue(s.contains("Rossi"));
        assertTrue(s.contains("12345"));
        assertTrue(s.contains("luigirossi@studenti.unisa.it"));
    }

    // ---- TO CSV ----

    @Test
    public void testToCSV() {
        assertEquals("Luigi;Rossi;12345;luigirossi@studenti.unisa.it", utente.toCSV());
    }
}

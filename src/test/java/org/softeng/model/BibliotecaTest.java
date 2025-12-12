package org.softeng.model;

import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.time.Month;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BibliotecaTest{
  private Biblioteca biblioteca;

  @Before
  public void setUp(){
    Biblioteca b = new Biblioteca();

    biblioteca.getLibri().clear();
    biblioteca.getUtenti().clear();
    biblioteca.getPrestiti().clear();
    
  }


  //*< Test 1: Aggiungi libro
  @Test
  public void testAggiungiLibro() throws LibroGiaPresenteException{
    Libro newLibro = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);

    biblioteca.aggiungiLibro(newLibro);


    asserTrue("La lista dovrebbe contenere il libro aggiunto", biblioteca.getLibri().contains(newLibro));
    assertEquals("La lista dovrebbe avere dimensione 1", 1, biblioteca.getLibri().size());
    
  }


  @Test
  public void testAggiungiLibroDuplicato() throws LibroGiaPresenteException{
    Libro libro1 = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);
    biblioteca.aggiungiLibro(libro1);

    Libro libro2 = new Libro("Struttura e progetto dei calcolatori", "Patterson", LocalDate.of(2005,3,4), "978-8871929385", 4);
    biblioteca.aggiungiLibro(libro2);
  }

  @Test
  public void testRegistraPrestito() throws Exception{
    Utente utente = new Utente("Mario", "Rossi", "0612708872", "m.rossi@studenti.unisa.it");
    biblioteca.aggiungiUtente(utente);

    Libro libro = new Libro("Reti di Calcolatori", "Kurose", LocalDate.of(2022,1,21), "978-8871929385", 5);
    biblioteca.aggiungiLibro(libro);

    biblioteca.registraPrestito(utente, libro, LocalDate.now().plusDays(30));

    assertEquals("Almeno 1 prestito registrato", 1, biblioteca.getPrestiti().size());
    assertEquals("Le copie devono scendere a 0", 0, libro.getNumeroCopieDisponibili());
  }

  @Test
  public void testRimuoviUtenteConPrestitoAttivo() throws Exception{
    Utente u = new Utente("Mario", "Rossi", "0612708872", "m.rossi@studenti.unisa.it");
    Libro l = new Libro("Struttura e progetto dei calcolatori", "Patterson", LocalDate.of(2005,3,4), "978-8871929385", 4);

    biblioteca.registraPrestito(u, l, LocalDate.now().plusDays(15));

    try{
      biblioteca.rimuoviUtente(u);
      fail("Avrebbe dovuto lanciare un'eccezione perché l'utente ha prestiti attivi");
    }catch(Exception ex){
      assertTrue(ex.getMessage().contains("Impossibile eliminare"));
    }
  }
}

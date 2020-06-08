/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.ejb.sb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.foi.nwtis.hsostaric.ejb.eb.Airports;
import org.foi.nwtis.hsostaric.ejb.eb.Korisnici;
import org.foi.nwtis.hsostaric.ejb.eb.Myairports;

/**
 * Klasa koja služi kao upravljač za drugu aktivnost. Kreirana je radi bolje
 * preglednosti i iskoristivosti koda.
 *
 * @author Hrvoje-PC
 */
@Stateless
@LocalBean
public class UpravljanjeDodavanjemAerodroma {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;

    @EJB
    AirportsFacadeLocal airportsFacadeLocal;

    @EJB
    MyairportsFacadeLocal myairportsFacadeLocal;

    public UpravljanjeDodavanjemAerodroma() {
    }

    /**
     * Metoda koja dohvaća sve korisnike iz baze podataka.
     *
     * @return korisnici baze podataka.
     */
    public List<Korisnici> dohvatiKorisnikeBazePodataka() {
        return korisniciFacadeLocal.findAll();
    }

    /**
     * Metoda koja dohvaća nazive prema nazivu aerodroma.Također ovisno o gumbu
     * ovisi oče li se izvršiti JPQL upit ili CApi.
     *
     * @param naziv naziv za koji se filtrira
     * @param jpql vrsta traženog upita.
     * @return lista aerodroma
     */
    public List<Airports> dohvatiAerodromePremaNazivu(String naziv, boolean jpql) {
        List<Airports> aerodromi = new ArrayList<>();
        if (naziv != null) {
            if (!naziv.equals("")) {
                if (jpql == true) {
                    aerodromi = airportsFacadeLocal.findByName(naziv);
                } else {
                    aerodromi = airportsFacadeLocal.findByNameCApi(naziv);
                }
            }
        }
        return aerodromi;
    }

    /**
     * Metoda koja dohvaća zadnje ažuriranje zapisa aerodroma koji se prate.
     * @return 
     */
    public String dajZadnjeAzuriranje() {
        Myairports zapis = myairportsFacadeLocal.dajZadnjeAzuriranje();
        int broj = myairportsFacadeLocal.brojAerodromaKojePratimo();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        return "Broj aerodroma koji se prate: " + broj + "."
                + "\nZadnje ažuriranje je: " + sdf.format(zapis
                        .getStored());
    }
/**
 *Metoda koja dodaje aerodrom u kolekciju MyAirports.
     * @param poruka poruka korisnika.
 */
    public void dodajNoviAerodromUMoje(String poruka) {
        String polje[] = poruka.split(";");
        String username = polje[0];
        String ident = polje[1];
        System.out.println("Username: " + username);
        System.out.println("Ident: " + ident.trim());
        Airports aerodrom = airportsFacadeLocal.find(ident.trim());
        Korisnici odabraniKorisnik = korisniciFacadeLocal.find(username.trim());
        List<Myairports> brojZapisa = myairportsFacadeLocal.brojZapisa(ident, username);
        if (brojZapisa == null || brojZapisa.isEmpty()) {
            Myairports noviZapis = new Myairports();
            noviZapis.setIdent(aerodrom);
            noviZapis.setStored(new Date());
            noviZapis.setUsername(odabraniKorisnik);
            myairportsFacadeLocal.create(noviZapis);
            System.out.println("Zapis je dodan");
        } else {
            System.out.println("Zapis postoji.");
        }

    }

    public int brojAerodroma() {
        return airportsFacadeLocal.count();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.web.zrna;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.hsostaric.ejb.eb.Airports;
import org.foi.nwtis.hsostaric.ejb.eb.Korisnici;
import org.foi.nwtis.hsostaric.ejb.eb.Myairports;
import org.foi.nwtis.hsostaric.ejb.eb.Myairportslog;
import org.foi.nwtis.hsostaric.ejb.sb.AirportsFacadeLocal;
import org.foi.nwtis.hsostaric.ejb.sb.KorisniciFacadeLocal;
import org.foi.nwtis.hsostaric.ejb.sb.MyairportsFacadeLocal;
import org.foi.nwtis.hsostaric.ejb.sb.MyairportslogFacadeLocal;

/**
 * Klasa koja je podražavajuće zrno za prvu aktivnost.
 *
 * @author Hrvoje-PC
 */
@Named(value = "korisniciAerodroma")
@ViewScoped
public class KorisniciAerodroma implements Serializable {

    @EJB
    KorisniciFacadeLocal korisniciFacadeLocal;
    @EJB
    AirportsFacadeLocal airportsFacadeLocal;
    @EJB
    MyairportsFacadeLocal myairportsFacadeLocal;

    @EJB
    MyairportslogFacadeLocal myairportslogFacadeLocal;
    @Getter
    @Setter
    Korisnici korisnik = null;
    @Getter
    @Setter
    Airports odabraniAerodrom = null;
    @Getter
    List<Korisnici> korisnici = new ArrayList<>();
    @Getter
    List<Myairports> aerodromiKorisnika = new ArrayList<>();

    @Getter
    @Setter
    String odabraniKorisnik = null;

    @Getter
    @Setter
    private String vidljivostDrugogBloka;
    @Getter
    @Setter
    private String vidljivostTrecegBloka;
    @Getter
    @Setter
    private String poruka = "";
    @Getter
    @Setter
    private List<Myairportslog> evidentiraniZapisi = new ArrayList<>();

    @Getter
    @Setter
    private String prazniLetovi = "";

    public KorisniciAerodroma() {
    }

    /**
     * Postavlja varijable nakon inicijalizacije.
     */
    @PostConstruct
    private void postaviVarijable() {
        sakrijDrugiBlok();
        sakrijTreciBlok();
        korisnici = korisniciFacadeLocal.findAll();
    }

    /**
     * Metoda koja dohvaća aerodroma za odabranog korisnika.
     *
     * @return
     */
    public String dohvatiAerodromeKorisnika() {

        korisnik = korisniciFacadeLocal.find(odabraniKorisnik);
        aerodromiKorisnika = new ArrayList<>();
        aerodromiKorisnika = korisnik.getMyairportsList();
        if (aerodromiKorisnika.isEmpty()) {
            poruka = "Korisnik ne prati nikakve aerodrome !";
            sakrijDrugiBlok();
            sakrijTreciBlok();
        } else {
            poruka = "";
            prikaziDrugiBlok();
            sakrijTreciBlok();
        }
        return "";
    }

    private void sakrijDrugiBlok() {
        vidljivostDrugogBloka = "skriven";
    }

    private void prikaziDrugiBlok() {
        vidljivostDrugogBloka = "vidljiv";
    }

    private void sakrijTreciBlok() {
        vidljivostTrecegBloka = "skriven";
    }

    private void prikaziTreciBlok() {
        vidljivostTrecegBloka = "vidljiv";
    }

    /**
     * Metoda koja formatira podatke za prikaz u tablici zapisa letova.
     *
     * @param lista
     * @return
     */
    public String formatirajLetove(List<Myairportslog> lista) {
        if (!lista.isEmpty()) {
            evidentiraniZapisi = new ArrayList<>(lista);
            prazniLetovi = "";
            prikaziTreciBlok();
        } else {
            prazniLetovi = "Odabrani aerodorm nema evidentiranih letova.";
            sakrijTreciBlok();
        }
        return "";
    }

    /**
     * Metoda koja formatira datum za prikaz u odgovarajućem prikazu.
     *
     * @param datumPreuzimanja
     * @return
     */
    public String formatirajDanPreuzimanja(Date datumPreuzimanja) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        return sdf.
                format(datumPreuzimanja);
    }

    /**
     * Metoda koja formatira datum u vrijeme.
     *
     * @param vrijemePreuzimanja
     * @return
     */
    public String formatirajVrijemePreuzimanja(Date vrijemePreuzimanja) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        return sdf
                .format(vrijemePreuzimanja);
    }

    /**
     * Metoda koja uklanja odabrani let iz evidencije.
     *
     * @param zapis
     * @return
     */
    @Remove
    public String obrisiZapis(Myairportslog zapis) {
        String ident = zapis.getAirports()
                .getIdent();
        evidentiraniZapisi.remove(zapis);
        myairportslogFacadeLocal.remove(zapis);
        odabraniAerodrom = airportsFacadeLocal.find(ident);
        odabraniAerodrom.setMyairportslogList(evidentiraniZapisi);
        airportsFacadeLocal.edit(odabraniAerodrom);
        azurirajZapisListeMojihAerodroma();
        return "";
    }

    /**
     * Metoda koja ažurira zapise nakon brisanja kako bi se osvježio ekran.
     *
     * @return
     */
    public String azurirajZapisListeMojihAerodroma() {
        korisnik = korisniciFacadeLocal.find(odabraniKorisnik);
        aerodromiKorisnika = new ArrayList<>();
        aerodromiKorisnika = korisnik.getMyairportsList();
        return "";
    }

    /**
     * Metoda koja skriva poglede za gumb .
     * @return 
     */
    public String sakrijPoglede() {
        evidentiraniZapisi.clear();
        aerodromiKorisnika.clear();
        sakrijDrugiBlok();
        sakrijTreciBlok();
        return "";
    }
}

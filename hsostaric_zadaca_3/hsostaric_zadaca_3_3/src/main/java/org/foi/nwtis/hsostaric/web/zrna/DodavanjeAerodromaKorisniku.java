package org.foi.nwtis.hsostaric.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.hsostaric.ejb.eb.Airports;
import org.foi.nwtis.hsostaric.ejb.eb.Korisnici;
import org.foi.nwtis.hsostaric.ejb.sb.Meteorolog;
import org.foi.nwtis.hsostaric.ejb.sb.UpravljanjeDodavanjemAerodroma;
import org.foi.nwtis.hsostaric.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 * Klasa koja je podržavajuće zrno za pogled dodavanjeAerodromaKorisniku.
 */
@Named(value = "dodavanjeAerodromaKorisniku")
@ViewScoped
public class DodavanjeAerodromaKorisniku implements Serializable {

    @Inject
    ServletContext context;
    @Getter
    @Setter
    BP_Konfiguracija bpk;
    @Getter
    @Setter
    private String vidljivostDrugogBloka = "";

    @Inject
    UpravljanjeDodavanjemAerodroma upravljac;
    @Inject
    Meteorolog meteorolog;
    @Getter
    @Setter
    private List<Korisnici> listaKorisnika = new ArrayList<>();
    @Getter
    @Setter
    private Korisnici odabraniKorisnik;

    @Getter
    @Setter
    private String porukaOdabranogKorisnika = "";

    @Getter
    @Setter
    private String nazivAerodroma = "";

    @Getter
    @Setter
    List<Airports> listaAerodroma = new ArrayList();

    @Getter
    @Setter
    private String porukaNemaAerodroma = "";
    @Setter
    @Getter
    private String apiKeyOWM = "";
    @Getter
    @Setter
    private MeteoPodaci meteoPodaci = new MeteoPodaci();

    @Getter
    @Setter
    private String porukaZaSlanje = "";

    public DodavanjeAerodromaKorisniku() {
    }

    /**
     * Metoda koja se izvršava nakon inicijalizacije zrna.
     */
    @PostConstruct
    public void init() {
        sakrijDrugiBlok();
        dohvatiKorisnike();
        ucitajKonfiguraciju();
        System.out.println("");
    }

    private void sakrijDrugiBlok() {
        vidljivostDrugogBloka = "skriven";
    }

    private void prikaziDrugiBlok() {
        vidljivostDrugogBloka = "vidljiv";
    }

    private void dohvatiKorisnike() {
        listaKorisnika = upravljac.dohvatiKorisnikeBazePodataka();
    }

    /**
     * Metoda koja postavlja aktivnog korisnika u varijablu.
     *
     * @param korisnik
     * @return
     */
    public String postaviKorisnika(Korisnici korisnik) {
        odabraniKorisnik = korisnik;
        ispisiKorisnika();
        return "";
    }

    private void ispisiKorisnika() {
        porukaOdabranogKorisnika = "Odabran je korisnik: " + odabraniKorisnik.getIme() + " " + odabraniKorisnik.getPrezime();
    }

    /**
     * Metoda koja dohvaća aerodrome prema vrsti upita i nazivu.
     *
     * @param jpql
     * @return
     */
    public String pretraziAerodrome(boolean jpql) {
        if (odabraniKorisnik != null) {
            listaAerodroma = upravljac.dohvatiAerodromePremaNazivu(nazivAerodroma, jpql);
            if (listaAerodroma.isEmpty()
                    || listaAerodroma == null) {
                sakrijDrugiBlok();
                porukaNemaAerodroma = "Nisu pronađeni nikakvi rezultati.";
            } else {
                dohvatiKorisnike();
                prikaziDrugiBlok();
                porukaNemaAerodroma = "";
            }
        } else {
            porukaNemaAerodroma = "Morate odabrati korisnika !";
        }

        return "";
    }

    /**
     * Metoda koja učitava konfiguraciju.
     */
    private void ucitajKonfiguraciju() {
        bpk = (BP_Konfiguracija) context.getAttribute("BP_Konfig");
        apiKeyOWM = bpk.getKonfig()
                .dajPostavku("OpenWeatherMap.apikey");

    }
/**
 *Metoda koja dohvaća temperaturu aerodroma za određeni aerodrom.
     * @param aerodrom
     * @return 
 */
    public String dajTemperaturuAerodroma(Airports aerodrom) {
        return meteorolog
                .dohvatiTemperaturu(apiKeyOWM, aerodrom);

    }
/**
 *Metoda koja dohvaća vlagu aerodroma.
     * @param aerodrom
     * @return 
 */
    public String dajVlaguAerodroma(Airports aerodrom) {
        return meteorolog.dohvatiVlagu(apiKeyOWM, aerodrom);

    }

}

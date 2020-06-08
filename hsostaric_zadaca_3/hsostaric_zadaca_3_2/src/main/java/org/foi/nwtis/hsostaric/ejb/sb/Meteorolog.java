package org.foi.nwtis.hsostaric.ejb.sb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.foi.nwtis.hsostaric.ejb.eb.Airports;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 * Klasa koja služi za dohvat meteo podataka za određeni aerodrom.
 *
 * @author Hrvoje-PC
 */
@Stateless
@LocalBean
public class Meteorolog {

    private OWMKlijent klijent;
    private MeteoPodaci meteoPodaci = new MeteoPodaci();
    private String apiKeyOWM;

    /**
     * Dohvača meteo podatke za primljeni aerodrom.
     */
    private void dohvatiMeteoPodatke(Airports aerodrom) {
        klijent = new OWMKlijent(apiKeyOWM);
        String koordinate = aerodrom.getCoordinates();
        String polje[] = koordinate.split(",");
        Lokacija lokacija = new Lokacija();
        lokacija.setLatitude(polje[1]
                .trim());
        lokacija.setLongitude(polje[0]
                .trim());
        meteoPodaci = klijent.getRealTimeWeather(lokacija.getLatitude(),
                lokacija.getLongitude());
    }

    /**
     *Metoda koja dohvaća trenutnu temperaturu aerodroma.
     * @param apiKeyOWM api ključ za servis
     * @param aerodrom odabrani aserodrom
     * @return temperatura aerodroma
     */
    public String dohvatiTemperaturu(String apiKeyOWM, Airports aerodrom) {
        this.apiKeyOWM = apiKeyOWM;
        dohvatiMeteoPodatke(aerodrom);
        return meteoPodaci.getTemperatureValue()
                .toString() + " " + meteoPodaci.getTemperatureUnit();
    }

    /**
     *Metoda koja dohvaća trenutni postotak vlage u zraku za primljeni aerodrom.
     * @param apiKeyOWM api ključ za servis
     * @param aerodrom odabrani aerodrom
     * @return postotak vlage za aerodrom
     */
    public String dohvatiVlagu(String apiKeyOWM, Airports aerodrom) {
        this.apiKeyOWM = apiKeyOWM;
        dohvatiMeteoPodatke(aerodrom);
        return meteoPodaci.getHumidityValue()
                .toString() + " " + meteoPodaci.getHumidityUnit();
    }

}

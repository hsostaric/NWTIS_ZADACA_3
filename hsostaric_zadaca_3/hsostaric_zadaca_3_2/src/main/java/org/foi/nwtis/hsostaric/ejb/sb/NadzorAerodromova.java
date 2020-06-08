/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.ejb.sb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Klasa koja je krajnja točka na poslužiteljevoj strani za WebSocket
 * komunikaciju.
 */
@ServerEndpoint("/aerodromi")
@LocalBean
public class NadzorAerodromova {

    @Inject
    UpravljanjeDodavanjemAerodroma upravljac;
    private static List<Session> korisnici = new ArrayList<>();

    public NadzorAerodromova() {
    }

    /**
     * Metoda koja registrira spajanje korisnika na vezu i dodaje ga u kolekciju
     * aktivnih korisnika.
     *
     * @param session sjednica korisnika
     */
    @OnOpen
    public void openConnection(Session session) {
        korisnici.add(session);
        String broj = String.valueOf(upravljac.dajZadnjeAzuriranje());
        try {
            session.getBasicRemote().sendText(broj);
        } catch (IOException ex) {
            System.out.println("Greska: " + ex);
        }
        System.out.println("Otvorena veza");

    }

    /**
     * Metoda koja vraća odgovor korisniku o zadnjem ažuriranju.
     *
     * @param poruka primljeni zahtjev korisnika
     */
    @OnMessage
    public void onMessage(String poruka) {
        System.out.println("Stigla poruka: " + poruka);
        upravljac.dodajNoviAerodromUMoje(poruka);
        for (Session s : korisnici) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(upravljac.dajZadnjeAzuriranje());
                } catch (IOException ex) {
                    System.out.println("Greška: " + ex);
                }
            }
        }
    }

    /**
     * Metoda koja se poziva nakon zatvaranja sjednice.
     *
     * @param session sjednica za zatvaranje
     */
    @OnClose
    public void closedConnection(Session session) {
        korisnici.remove(session);
        System.out.println("Zatvorena veza.");
    }
/**
 *Metoda koja zatvara vezu prema korisniku ukoliko je došlo do greške.
     * @param session sjednica korisnika
     * @param t greška
 */
    @OnError
    public void error(Session session, Throwable t) {
        korisnici.remove(session);
        System.out.println("Zatvorena veza zbog greške.");
    }

}

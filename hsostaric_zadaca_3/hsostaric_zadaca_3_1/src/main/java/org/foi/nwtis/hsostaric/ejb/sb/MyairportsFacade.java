/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import org.foi.nwtis.hsostaric.ejb.eb.Myairports;

/**
 *
 * @author Hrvoje-PC
 */
@Stateless
public class MyairportsFacade extends AbstractFacade<Myairports> implements MyairportsFacadeLocal {

    @PersistenceContext(unitName = "NWTiS_DZ3_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MyairportsFacade() {
        super(Myairports.class);
    }

    /**
     * Upit koji dohvaća zadnje ažuriranje u tablici MyAirports.
     *
     * @return
     */
    @Override
    public Myairports dajZadnjeAzuriranje() {
        return (Myairports) em.createQuery("SELECT m FROM Myairports m ORDER BY m.stored desc")
                .getResultList().get(0);
    }

    /**
     * Metoda koja vraća broj jedinstvenih aerodroma koji se prate.
     *
     * @return
     */
    @Override
    public int brojAerodromaKojePratimo() {
        return em.createQuery("SELECT distinct m.ident FROM Myairports m")
                .getResultList()
                .size();
    }
/**
 *Metoda koja provjerava prati li korisnik već odabrani aerodrom.
     * @param ident
     * @param korime
     * @return 
 */
    @Override
    public List<Myairports> brojZapisa(String ident, String korime) {
        return em.createQuery("Select m FROM Myairports m where m.ident.ident = :ident AND m.username.korIme = :korime")
                .setParameter("ident", ident)
                .setParameter("korime", korime)
                .getResultList();
    }

}

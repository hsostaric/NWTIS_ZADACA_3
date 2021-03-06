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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.foi.nwtis.hsostaric.ejb.eb.Airports;
import org.foi.nwtis.hsostaric.ejb.eb.Airports_;
import org.foi.nwtis.hsostaric.ejb.eb.Myairports;
import org.foi.nwtis.hsostaric.ejb.eb.Myairports_;

/**
 *
 * @author Hrvoje-PC
 */
@Stateless
public class AirportsFacade extends AbstractFacade<Airports> implements AirportsFacadeLocal {

    @PersistenceContext(unitName = "NWTiS_DZ3_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AirportsFacade() {
        super(Airports.class);
    }

    /**
     * Metoda koja pronalazi aerodorm prema nazivu uz pomoc JPQL-a.
     *
     * @param name
     * @return
     */
    @Override
    public List<Airports> findByName(String name) {
        return em.createNamedQuery("Airports.findByName")
                .setParameter("naziv", name)
                .getResultList();
    }
/**
 *Metoda koja pronalazi aerodrome prema nazivu uz CApi kreiranje upita.
     * @param name
     * @return 
 */
    @Override
    public List<Airports> findByNameCApi(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Airports> createQuery = cb.createQuery(Airports.class);
        Root<Airports> aerodromi = createQuery.from(Airports.class);
        Expression<String> naziv = aerodromi.get("name");
        createQuery.where(cb.like(naziv, name));
        return em.createQuery(createQuery)
                .getResultList();
    }
}

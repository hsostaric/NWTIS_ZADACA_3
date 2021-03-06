/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.hsostaric.ejb.sb;

import java.util.List;
import javax.ejb.Local;
import org.foi.nwtis.hsostaric.ejb.eb.Myairports;

/**
 *
 * @author Hrvoje-PC
 */
@Local
public interface MyairportsFacadeLocal {

    void create(Myairports myairports);

    void edit(Myairports myairports);

    void remove(Myairports myairports);

    Myairports find(Object id);

    List<Myairports> findAll();

    List<Myairports> findRange(int[] range);
    
    Myairports dajZadnjeAzuriranje();
    int brojAerodromaKojePratimo();
    List<Myairports> brojZapisa(String ident, String korime);
    int count();
    
}

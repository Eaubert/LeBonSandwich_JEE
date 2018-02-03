package org.lpro.boundary;

import org.lpro.entity.SandwichCommande;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class SandwichCommandeRessource {

    @PersistenceContext
    EntityManager em;

    public SandwichCommande findById(String idCmd, String idSand) {
            Query query = em.createNamedQuery("sandwichCommande.findById");
            Object obj = query.setParameter("idS", idSand).setParameter("idC", idCmd).getSingleResult();
            return this.em.find(SandwichCommande.class, obj);
    }


    public void save(SandwichCommande sc) {
        this.em.persist(sc);
    }

    public void delete(SandwichCommande sc){
        this.em.remove(sc);
    }

    public void update(SandwichCommande sc){
        this.em.merge(sc);
    }


}

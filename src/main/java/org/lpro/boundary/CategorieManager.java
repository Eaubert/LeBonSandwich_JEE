package org.lpro.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.lpro.entity.Categorie;

@Stateless
public class CategorieManager {

    @PersistenceContext
    EntityManager em;

    public Categorie findById(long id) {
        return this.em.find(Categorie.class, id);
    }

    public List<Categorie> findAll() {
        Query q = this.em.createNamedQuery("Categorie.findAll", Categorie.class);
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }

    public Categorie save(Categorie c) {
        return this.em.merge(c);
    }

    public void delete(long id) {
        try {
            Categorie ref = this.em.getReference(Categorie.class, id);
            this.em.remove(ref);
        } catch (EntityNotFoundException enfe) {
            // rien à faire   
        }
    }
}

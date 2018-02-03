package org.lpro.boundary;

import org.lpro.entity.Taille;
import org.lpro.entity.Sandwich;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Stateless
public class TailleManager {

    @PersistenceContext
    EntityManager em;

    public Taille findById(String id) {
        return this.em.find(Taille.class, id);
    }

    public List<Taille> findAll() {
        Query q = this.em.createNamedQuery("Taille.findAll", Taille.class);
        q.setHint("javax.persistance.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }

}

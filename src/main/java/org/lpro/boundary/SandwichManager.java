package org.lpro.boundary;

import org.lpro.entity.Categorie;
import org.lpro.entity.Sandwich;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Stateless
public class SandwichManager {

    @PersistenceContext
    EntityManager em;

    public List<Sandwich> findAll() {
        Query q = this.em.createNamedQuery("Sandwich.findAll", Sandwich.class);
        q.setHint("javax.persistance.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }

    public Sandwich findById(String id) {
        return this.em.find(Sandwich.class, id);
    }

    public Query createQuery(String ptype, int img) {
        String sql = "SELECT s FROM Sandwich s";

        if (ptype != null) {
            if (img == 1) {
                sql += " WHERE s.type_pain = '" + ptype + "' AND s.img != ''";
            } else {
                sql += " WHERE s.type_pain = '" + ptype + "'";
            }
        } else {
            if (img == 1) {
                sql += " WHERE s.img != ''";
            }
        }

        return this.em.createQuery(sql);
    }

    public List<Sandwich> findWithParam(Query query, int page, int nbPerPage) {
        double nbSandwichs = query.getResultList().size();

        if (page <= 0) {
            page = 1;
        }
        else if (page > Math.ceil(nbSandwichs / (double) nbPerPage)) {
            page = (int) Math.ceil(nbSandwichs / (double) nbPerPage);
        }
        query.setFirstResult((page-1) * nbPerPage);
        query.setMaxResults(nbPerPage);
        return query.getResultList();
    }

    public Sandwich addSandwich(String catId, Sandwich sand) {
        Sandwich s;
        TypedQuery<Sandwich> query = em.createQuery("SELECT s FROM Sandwich s WHERE s.nom = :n", Sandwich.class);
        query.setParameter("n", sand.getNom());
        try {
            s = query.getSingleResult();
        } catch (NoResultException e) {
            s = new Sandwich(sand);
            s.setId(UUID.randomUUID().toString());
            this.em.persist(s);
        }
        Categorie cat = this.em.find(Categorie.class, catId);
        cat.getSandwich().add(s);
        this.em.persist(cat);
        return s;
    }
  
    public Sandwich save(Sandwich s) {
        s.setId(UUID.randomUUID().toString());
        return this.em.merge(s);
    }

    public void delete(String id) {
        try {
            Sandwich ref = this.em.getReference(Sandwich.class, id);
            this.em.remove(ref);
        } catch (EntityNotFoundException e) { }
    }

    public JsonObject getMeta(long size) {
        return Json.createObjectBuilder()
                .add("count", ((size == -1) ? this.findAll().size() : size))
                .add("date",  "04-08-2018")
                .build();
    }

    public JsonObject getMetaPerPage(long size, String ptype, int img, int page, int nbPerPage) {
        return Json.createObjectBuilder()
                .add("count", ((size == -1) ? this.createQuery(ptype, img).getResultList().size() : size))
                .add("size", this.findWithParam(this.createQuery(ptype, img), page, nbPerPage).size())
                .build();
    }
}

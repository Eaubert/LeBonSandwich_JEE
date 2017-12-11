package org.lpro.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.lpro.entity.Sandwich;

@Stateless
public class SandwichManager {
    
    @PersistenceContext
    EntityManager em;
    List<Sandwich> findAllByImg;
    
    public Sandwich findById(long id){
        return this.em.find(Sandwich.class, id);
    }
    
    public List<Sandwich> findAllByType(String type){
        Query query = em.createQuery("SELECT s FROM Sandwich s WHERE s.type_pain = :type");
        query.setParameter("type", type);
        return query.getResultList();
    }
    
    public List<Sandwich> findAllByPage(int page, int size){
        
        
        Query nbElementsQuery = em.createQuery("Select count(s.id) from Sandwich s");
        long nbElements = (long) nbElementsQuery.getSingleResult();
        int nbPage = (int) (nbElements / size) +1 ; 
        if(nbPage < page){ page = nbPage; }
        Query query = this.em.createNamedQuery("Sandwich.findAll", Sandwich.class);
        query.setFirstResult((page-1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }
    
    public List<Sandwich> findAllByTypeImg(String type){
        Query query = em.createQuery("SELECT s FROM Sandwich s WHERE s.type_pain = :type AND  s.img is not NULL");
        query.setParameter("type", type);
        return query.getResultList();
    }
    
    public List<Sandwich> findAllByImg(){
        Query query = em.createQuery("SELECT s FROM Sandwich s WHERE s.img is not NULL ");
        return query.getResultList();
    } 
    
    public List<Sandwich> findAll() {
        Query q = this.em.createNamedQuery("Sandwich.findAll", Sandwich.class);
        q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        return q.getResultList();
    }

    public Sandwich save(Sandwich s) {
        return this.em.merge(s);
    }

    public void delete(long id) {
        try {
            Sandwich ref = this.em.getReference(Sandwich.class, id);
            this.em.remove(ref);
        } catch (EntityNotFoundException enfe) {
            // rien à faire   
        }
    }
}
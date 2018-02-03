package org.lpro.entity;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@Entity

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "sandwichCommande.findAll", query = "SELECT sc FROM SandwichCommande sc"),
        @NamedQuery(name = "sandwichCommande.findById", query = "SELECT sc FROM SandwichCommande sc " +
                "WHERE sandwich_id = :idS AND commande_id = :idC")
})
public class SandwichCommande implements Serializable {

    @Id
    @ManyToOne
    private Commande commande;

    @Id
    @ManyToOne
    private Sandwich sandwich;

    private String qte;

    private String taille;

    public SandwichCommande() {
    }

    public Sandwich getSandwich() {
        return sandwich;
    }

    public void setSandwich(Sandwich s) {
        this.sandwich = s;
    }

    public SandwichCommande(Commande c, Sandwich s, String qte, String taille) {
        this.commande = c;
        this.sandwich = s;
        this.qte = qte;
        this.taille = taille;
    }


    public String getTaille() {
        return this.taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getQte() {
        return qte;
    }

    public void setQte(String qte) {
        this.qte = qte;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
}

package org.lpro.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name="Tarif.findAll",query="SELECT t FROM Tarif t")
public class Tarif implements Serializable {


    @Id
    @ManyToOne
    private Sandwich sandwich;

    @Id
    @ManyToOne
    private Taille taille;

    public Sandwich getSandwich() {
        return sandwich;
    }

    public void setSandwich(Sandwich sandwich) {
        this.sandwich = sandwich;
    }

    public Taille getTaille() {
        return taille;
    }

    public void setTaille(Taille taille) {
        this.taille = taille;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Tarif() {
    }

    public Tarif(Sandwich sandwich, Taille taille, float prix) {
        this.sandwich = sandwich;
        this.taille = taille;
        this.prix = prix;

    }

    @NotNull
    private float prix;


}

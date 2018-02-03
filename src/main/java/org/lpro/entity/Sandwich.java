package org.lpro.entity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name="Sandwich.findAll",query="SELECT s FROM Sandwich s")
public class Sandwich implements Serializable{

    @Id
    private String id;
    @NotNull
    private String nom;
    @NotNull
    private String description;
    @NotNull
    private String type_pain;
    private String img;

    @ManyToMany
    private Set<Categorie> categorie = new HashSet<Categorie>();

    @OneToMany(mappedBy="sandwich")
    private Set<Tarif> tarifs = new HashSet<Tarif>();

    @OneToMany(mappedBy="sandwich")
    private Set<SandwichCommande> sandwichCommande = new HashSet<SandwichCommande>();

    public Sandwich() {
    }


    public Sandwich(Sandwich sand){
        this.id = sand.getId();
        this.nom = sand.getNom();
        this.description = sand.getDescription();
        this.type_pain = sand.getType_Pain();
        this.img = sand.getImg();
        this.categorie = new HashSet<>();
        this.sandwichCommande = new HashSet<>();
    }

    public Sandwich(String id, String nom, String description, String type_pain, String img) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.type_pain = type_pain;
        this.img = img;
        this.categorie = new HashSet<>();
    }

    public Set<SandwichCommande> getSandwichCommande() {
        return sandwichCommande;
    }

    public void setSandwichCommande(Set<SandwichCommande> sandwichCommande) {
        this.sandwichCommande = sandwichCommande;
    }

    public String getType_Pain() {
        return type_pain;
    }

    public void setType_Pain(String type_pain) {
        this.type_pain = type_pain;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public Set<Tarif> getTarifs() {
        return tarifs;
    }

    public void setTarifs(Set<Tarif> tarifs) {
        this.tarifs = tarifs;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sandwich other = (Sandwich) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.type_pain, other.type_pain)) {
            return false;
        }
        if (!Objects.equals(this.img, other.img)) {
            return false;
        }
        if (!Objects.equals(this.categorie, other.categorie)) {
            return false;
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Categorie> getCategorie() {
        return categorie;
    }

    public void setCategorie(Set<Categorie> categorie) {
        this.categorie = categorie;
    }

    public String getType_pain() {
        return type_pain;
    }

    public void setType_pain(String type_pain) {
        this.type_pain = type_pain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addCommande(SandwichCommande sc){
        this.sandwichCommande.add(sc);
    }
    
}

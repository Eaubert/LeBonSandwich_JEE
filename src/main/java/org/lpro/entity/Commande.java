package org.lpro.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name="Commande.findAll",query="SELECT c FROM Commande c")
public class Commande implements Serializable {
    
    @Id
    private String id;
    private String nom;
    private String mail;
    private String statut;
    private String prix;
    private String dateLivraison;
    private String heureLivraison;
    private String token;
    private String datePaiement;

    @OneToMany(mappedBy="commande")
    private Set<SandwichCommande> sandwichCommande = new HashSet<SandwichCommande>();


    public Commande() {
    }

    public Commande(String nom, String mail, String dateLivraison, String heureLivraison) {
        this.nom = nom;
        this.mail = mail;
        this.dateLivraison = dateLivraison;
        this.heureLivraison = heureLivraison;
        this.statut = "Attente de paiement";
        this.prix = "0";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void addSandwich(SandwichCommande sandwichCommande){
        this.sandwichCommande.add(sandwichCommande);
    }

    public Set<SandwichCommande> getSandwichCommande() {
        return sandwichCommande;
    }

    public void setSandwichCommande(Set<SandwichCommande> sandwichCommande) {
        this.sandwichCommande = sandwichCommande;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(String dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getHeureLivraison() {
        return heureLivraison;
    }

    public void setHeureLivraison(String heureLivraison) {
        this.heureLivraison = heureLivraison;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPrix() {

        this.prix = "0";

        this.sandwichCommande.forEach((sc) -> {
            Integer qte = Integer.parseInt(sc.getQte());
            String taille = sc.getTaille();
            Sandwich s = sc.getSandwich();

            s.getTarifs().forEach((t) -> {
                if (t.getTaille().getNom().equals(taille)) {
                    this.prix =  Float.toString(Float.parseFloat(this.prix) + ( qte * t.getPrix()));
                }
            });
        });

        return this.prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }
}




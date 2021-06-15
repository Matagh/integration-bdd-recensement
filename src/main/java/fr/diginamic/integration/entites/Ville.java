package fr.diginamic.integration.entites;

/** Représente une région
 *
 */
public class Ville {
    /** id unique : int */
    int id;
    /** code du département : String */
    String codeVille;
    /** nom de la ville : String */
    String nom;
    /** population totale de la ville : int */
    int population;
    /** id du département associé : int */
    int idDepartement;
    /** id de la region associée : int */
    int idRegion;

    public Ville(int id, String codeVille, String nom, int population, int idDepartement, int idRegion) {
        this.id = id;
        this.codeVille = codeVille;
        this.nom = nom;
        this.population = population;
        this.idDepartement = idDepartement;
        this.idRegion = idRegion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeVille() {
        return codeVille;
    }

    public void setCodeVille(String codeVille) {
        this.codeVille = codeVille;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(int idDepartement) {
        this.idDepartement = idDepartement;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }
}

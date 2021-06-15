package fr.diginamic.integration.entites;

/** Représente une région
 *
 */
public class Region {
    /** id : int */
    int id;
    /** code de la region : String */
    String code;
    /** nom de la region : String */
    String nom;

    public Region(int id, String codeRegion, String nomRegion) {
        this.id = id;
        this.code = codeRegion;
        this.nom = nomRegion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}

package fr.diginamic.integration.entites;

/** Représente un département
 *
 */
public class Departement {
    /** id unique : int */
    int id;
    /** code du département : String */
    String code;
    /** id de la region associée : int */
    int idRegion;

    public Departement(int id, String codeDepartement, int idRegion) {
        this.id = id;
        this.code = codeDepartement;
        this.idRegion = idRegion;
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

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }
}

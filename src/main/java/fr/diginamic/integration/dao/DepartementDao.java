package fr.diginamic.integration.dao;

import fr.diginamic.integration.entites.Departement;
import org.mariadb.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Classe DAO qui permet d'executer des requetes CRUD sur la table departement
 */
public class DepartementDao {
    Connection connection = null;
    PreparedStatement prepStatement = null;
    ResultSet cursor = null;

    public DepartementDao() {
        connectToDB();
    }

    /**
     * Methode qui établit une connection avec la database
     */
    private void connectToDB(){
        try {
            DriverManager.registerDriver(new Driver());

            ResourceBundle config = ResourceBundle.getBundle("config");
            connection = DriverManager.getConnection(config.getString("database.url"),
                    config.getString("database.user"),
                    config.getString("database.pwd"));
        } catch (SQLException throwables) {
            System.err.println("Error : connection to Data basse failed");
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error : can't close database access");
                }
            }
        }
    }

    /**
     * Methode pour fermer les ressources d'accès à la database
     */
    private void closeDBAccess(){
        try {
            if (connection!=null)
                connection.close(); // Indispensable pour certaines BDD. Exemple : Oracle
            if (prepStatement!=null)
                prepStatement.close(); // Indispensable pour certaines BDD. Exemple : Oracle
            if(cursor!=null)
                cursor.close();
        } catch (SQLException e){
            System.err.println("Erreur lors de la fermeture des resources d'accès à la BDD");
        }
    }

    /**
     * Extrait les données de la table departement et les retourne sous la forme
     * d'une liste de departement
     * @return
     */
    public List<Departement> extract(){
        List<Departement> departementList = new ArrayList<>();
        //connectToDB();
        try {
            prepStatement = connection.prepareStatement("SELECT * FROM departement");
            cursor = prepStatement.executeQuery();

            while (cursor.next()){
                int id = cursor.getInt("ID");
                String codeDpt = cursor.getString("CODE");
                int idRegion = cursor.getInt("ID_REGION");
                departementList.add(new Departement(id, codeDpt, idRegion));
            }
            return departementList;
        } catch (SQLException e){
            System.err.println("Erreur : impossible de recupérer la liste des departement");
            return null;
        } finally {
            //closeDBAccess();
        }
    }

    /**
     * Extrait un departement de la database a partir de son code
     * @param code
     * @return
     */
    public Departement extractByCode(String code){
        //connectToDB();
        try {
            prepStatement = connection.prepareStatement("SELECT * FROM departement WHERE CODE=?");
            prepStatement.setString(1, code);
            cursor = prepStatement.executeQuery();

            if (cursor.next()){
                int id = cursor.getInt("ID");
                String codeRegion = cursor.getString("CODE");
                int idRegion = cursor.getInt("ID_REGION");
                return new Departement(id, codeRegion, idRegion);
            } else {
                System.err.println("Aucun département portant ce code dans la base de données");
                return null;
            }
        } catch (SQLException e){
            System.err.println("Erreur : impossible de recupérer le departement");
            return null;
        } finally {
            //closeDBAccess();
        }
    }

    /**
     * Insère un nouveau departement dans la BDD si il n'existe pas déjà
     * @param dpt nouveau département
     */
    public void insert(Departement dpt){
        try {
            if (extractByCode(dpt.getCode())==null){
                //connectToDB(); // je faits la connexion à la base ici car l'extractByName ci dessus ferme la connection une fois qu'il a fini
                prepStatement = connection.prepareStatement("INSERT INTO departement (CODE, POPULATION, ID_REGION) values (?, ?, ?);");
                prepStatement.setString(1, dpt.getCode());
                prepStatement.setInt(3, dpt.getIdRegion());
                int res = prepStatement.executeUpdate();

            }
            else{
                System.err.println("Erreur : ce département existe déja dans la base de données");
            }

        }catch (SQLException e) {
            System.err.println("Erreur : l'insertion de departement a échoué");
        } finally {
            //closeDBAccess();
        }
    }

    public void update(){

    }

    /**
     * Supprime un departement dans la BDD
     * @param dpt : departement à supprimer
     */
    public void delete(Departement dpt){
        //connectToDB();
        int res=0;
        try {
            prepStatement = connection.prepareStatement("DELETE FROM departement WHERE CODE=?");
            prepStatement.setString(1, dpt.getCode());
            res = prepStatement.executeUpdate();

            if(res==0)
                throw new Exception("Erreur : echec de supression du departement");
        } catch (SQLException e){
            System.err.println("Erreur : problème rencontré lors de la supression du departement");
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        finally {
            //closeDBAccess();
        }
    }

}

package fr.diginamic.integration.dao;



import fr.diginamic.integration.entites.Region;
import org.mariadb.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Classe DAO qui permet d'executer des requetes CRUD sur la table region
 */
public class RegionDao {
    Connection connection = null;
    PreparedStatement prepStatement = null;
    ResultSet cursor = null;

    public RegionDao() {
        connectToDB();
    }

    /**
     * Methode qui établit une connection avec la database
     */
    public void connectToDB(){
        try {
            DriverManager.registerDriver(new Driver());

            ResourceBundle config = ResourceBundle.getBundle("config");
            connection = DriverManager.getConnection(config.getString("database.url"),
                    config.getString("database.user"),
                    config.getString("database.pwd"));
        } catch (SQLException throwables) {
            System.err.println("Error : connection to Data base failed");
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
    public void closeDBAccess(){
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
     * Extrait les données de la table region et les retourne sous la forme
     * d'une liste de region
     * @return
     */
    public List<Region> extract(){
        List<Region> regionList = new ArrayList<>();
        //connectToDB();
        try {
            prepStatement = connection.prepareStatement("SELECT * FROM region");
            cursor = prepStatement.executeQuery();

            while (cursor.next()){
                int id = cursor.getInt("ID");
                String codeRegion = cursor.getString("CODE");
                String nomRegion = cursor.getString("NOM");
                regionList.add(new Region(id, codeRegion, nomRegion));
            }
            return regionList;
        } catch (SQLException e){
            System.err.println("Erreur : impossible de recupérer la liste des departements");
            return null;
        } finally {
            //closeDBAccess();
        }
    }

    /**
     * Extrait une région de la database a partir de son nom
     * @param name
     * @return
     */
    public Region extractByName(String name){
        //connectToDB();
        try {
            prepStatement = connection.prepareStatement("SELECT * FROM region WHERE NOM=?");
            prepStatement.setString(1, name);
            cursor = prepStatement.executeQuery();

            if (cursor.next()){
                int id = cursor.getInt("ID");
                String codeRegion = cursor.getString("CODE");
                String nomRegion = cursor.getString("NOM");
                return new Region(id, codeRegion, nomRegion);
            } else {
                System.out.println("Aucune région portant ce nom dans la base de données");
                return null;
            }
        } catch (SQLException e){
            System.err.println("Erreur : impossible de recupérer la région");
            return null;
        } finally {
            //closeDBAccess();
        }
    }

    /**
     * Insère une nouvelle region dans la BDD si elle n'existe pas déjà
     * @param region : region à ajouter
     */
    public void insert(Region region){
        try {
            if (extractByName(region.getNom()) == null){ //i.e. : n'existe pas encore
                //connectToDB(); // je faits la connexion à la base ici car l'extractByName ci dessus ferme la connection une fois qu'il a fini
                prepStatement = connection.prepareStatement("INSERT INTO region (CODE, NOM) VALUES (?, ?);");
                prepStatement.setString(1, region.getCode());
                prepStatement.setString(2, region.getNom());
                prepStatement.executeUpdate();
                System.out.println("Succès de l'insert de la région");
            }
            else{
                System.out.println("Operation d'insert annulée : cette region existe déja dans la base de données");
            }

        }catch (SQLException e) {
            System.err.println("Erreur : l'insertion de la région a échoué");
            e.printStackTrace();;
        } finally {
            //closeDBAccess();
        }
    }

    /**
     * Supprime une region dans la BDD
     * @param region : region à supprimer
     */
    public void delete(Region region){
        connectToDB();
        int res=0;
        try {
            prepStatement = connection.prepareStatement("DELETE FROM region WHERE CODE=? AND NOM=?");
            prepStatement.setString(1, region.getCode());
            prepStatement.setString(2, region.getNom());
            res = prepStatement.executeUpdate();

            if(res==0)
                throw new Exception("Erreur : echec de supression du departement");
        } catch (SQLException e){
            System.err.println("Erreur problème rencontré lors de la supression du departement");
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        finally {
            closeDBAccess();
        }
    }

    /**
     * Modifie le nom d'une region
     * @param oldNom
     * @param newNom
     */
    public void update(String oldNom, String newNom){
        connectToDB();
        int res=0;

        try {
            prepStatement = connection.prepareStatement("UPDATE region SET NOM=? WHERE NOM=?;");
            prepStatement.setString(1, newNom);
            prepStatement.setString(2, oldNom);
            res = prepStatement.executeUpdate();

            if(res==0)
                System.err.println("L'update a échoué...");
        }catch (SQLException e){
            System.err.println("Erreur lors de la tentative d'update de la table region");
        } finally {
            closeDBAccess();
        }
    }
}



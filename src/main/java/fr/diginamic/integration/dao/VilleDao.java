package fr.diginamic.integration.dao;

import fr.diginamic.integration.entites.Region;
import fr.diginamic.integration.entites.Ville;
import org.mariadb.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Classe DAO qui permet d'executer des requetes CRUD sur la table VILLE
 */
public class VilleDao {
    Connection connection = null;
    PreparedStatement prepStatement = null;
    ResultSet cursor = null;

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
     * Extrait les données de la table ville et les retourne sous la forme
     * d'une liste de villes
     * @return
     */
    public List<Ville> extract(){
        List<Ville> villeList = new ArrayList<>();
        connectToDB();
        try {
            prepStatement = connection.prepareStatement("SELECT * FROM ville");
            cursor = prepStatement.executeQuery();

            while (cursor.next()){
                int id = cursor.getInt("ID");
                String codeVille = cursor.getString("CODE_VILLE");
                String nom = cursor.getString("NOM");
                int pop = cursor.getInt("POPULATION");
                int idDepartement = cursor.getInt("ID_DEPARTEMENT");
                int idRegion = cursor.getInt("ID_REGION");


                // Crée une ville à partir des elements extraits de la table puis l'ajoute à la liste
                villeList.add(new Ville(id, codeVille, nom, pop, idDepartement, idRegion));
            }
            return villeList;
        } catch (SQLException e){
            System.err.println("Erreur : impossible de recupérer la liste des villes");
            return null;
        } finally {
            closeDBAccess();
        }
    }

    /**
     * Extrait une ville de la database a partir de son nom
     * @param name
     * @return
     */
    public Ville extractByName(String name){
        connectToDB();
        try {
            prepStatement = connection.prepareStatement("SELECT * FROM region WHERE NOM=?");
            prepStatement.setString(1, name);
            cursor = prepStatement.executeQuery();

            if (cursor.next()){
                int id = cursor.getInt("ID");
                String codeVille = cursor.getString("CODE");
                String nomVille = cursor.getString("NOM");
                int population = cursor.getInt("POPULATION");
                int idDepartement = cursor.getInt("ID_DEPARTEMENT");
                int idRegion = cursor.getInt("ID_REGION");

                return new Ville(id, codeVille, nomVille, population, idDepartement, idRegion);
            } else {
                System.err.println("Aucune ville portant ce nom dans la base de données");
                return null;
            }
        } catch (SQLException e){
            System.err.println("Erreur : impossible de recupérer la ville");
            return null;
        } finally {
            closeDBAccess();
        }
    }

    /**
     * Insère une nouvelle ville dans la BDD si elle n'existe pas déjà
     * @param ville : ville à ajouter
     */
    public void insert(Ville ville){
        try {
            if (extractByName(ville.getNom())==null){
                connectToDB(); // je faits la connexion à la base ici car l'extractByName ci dessus ferme la connection une fois qu'il a fini
                prepStatement = connection.prepareStatement("INSERT INTO ville (CODE_VILLE, NOM, POPULATION, ID_DEPARTEMENT, ID_REGION) values (?, ?, ?, ?, ?);");
                prepStatement.setString(1, ville.getCodeVille());
                prepStatement.setString(2, ville.getNom());
                prepStatement.setInt(3, ville.getPopulation());
                prepStatement.setInt(4, ville.getIdDepartement());
                prepStatement.setInt(5, ville.getIdRegion());
                prepStatement.executeUpdate();
            }
            else{
                System.err.println("Erreur : cette ville existe déja dans la base de données");
            }

        }catch (SQLException e) {
            System.err.println("Erreur : l'insertion de ville a échoué");
        } finally {
            closeDBAccess();
        }
    }

    /**
     * Modifie le nom d'un ville dans la Base de données
     * @param oldNom : nom de la ville à modifier
     * @param newNom : nouveau nom
     */
    public void update(String oldNom, String newNom){
        connectToDB();
        int res = 0;
        try {
            prepStatement = connection.prepareStatement("UPDATE ville SET NOM=? WHERE NOM=?");
            prepStatement.setString(1, newNom);
            prepStatement.setString(1, oldNom);
            res = prepStatement.executeUpdate();

            if(res==0)
                throw new Exception("Echec de l'update");
        } catch (SQLException e){
            System.err.println("Erreur : impossible de réaliser l'update de données");
        } catch (Exception e){
            System.err.println(e.getMessage());
        } finally {
            closeDBAccess();
        }
    }

    /**
     * Supprime une ville de la base de donnée
     * @param ville
     */
    public void delete(Ville ville){
        connectToDB();
        int res = 0;
        try {
            prepStatement = connection.prepareStatement("DELETE FROM ville WHERE CODE_VILLE=?");
            prepStatement.setString(1, ville.getCodeVille());
            res = prepStatement.executeUpdate();

            if(res==0)
                throw new Exception("Erreur : problème rencontré lors de la supression de la ville");
        } catch (SQLException e){
            e.getStackTrace();
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        finally {
            closeDBAccess();
        }
    }
}

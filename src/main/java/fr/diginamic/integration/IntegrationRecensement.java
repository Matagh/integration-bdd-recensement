package fr.diginamic.integration;

import fr.diginamic.integration.dao.DepartementDao;
import fr.diginamic.integration.dao.RegionDao;
import fr.diginamic.integration.dao.VilleDao;
import fr.diginamic.integration.entites.Departement;
import fr.diginamic.integration.entites.Region;
import fr.diginamic.integration.entites.Ville;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IntegrationRecensement {
    public static void main(String[] args) {
        List<String> lignesFichier = readFile();
        //insertEntry(lignesFichier.get(0));
        for (int i=0; i < 10000; i++) {
            insertEntry(lignesFichier.get(i));
        }
    }

    /**
     * Lis le fichier recensement et stocke chaque ligne dans une liste
     * @return
     */
    private static List<String> readFile(){
        InputStream inputStream = IntegrationRecensement.class.getClassLoader().getResourceAsStream("recensement.csv");
        List<String> allLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            allLines.remove(0); // On supprime la 1ère ligne d'entéte avec les noms des colonnes
            return allLines;
        } catch (IOException ioe) {
            System.err.println("Erreur lors de la lecture du fichier recensement");
            return null;
        }
    }

    /**
     * A partir d'une ligne du fichier recensement, insère les données adéquates en base
     * @param line
     */
    private static void insertEntry(String line){
        // 1- Parsing de la ligne
        String[] morceaux = line.split(";");
        String codeRegion = morceaux[0];
        String nomRegion = morceaux[1];
        String codeDepartement = morceaux[2];
        String codeCommune = morceaux[5];
        String nomCommune = morceaux[6];
        String population = morceaux[7];
        int populationTotale = Integer.parseInt(population.replace(" ", "").trim());

        // 2- On instancie les classes DAO
        RegionDao daoRegion = new RegionDao();
        DepartementDao daoDepartement = new DepartementDao();
        VilleDao daoVille = new VilleDao();

        // 3- Insertion dans la table region
        Region region = new Region(0, codeRegion, nomRegion);
        daoRegion.insert(region); // la methode insert vérifie déja si la region n'existe pas déja en base avant de l'insérer
        Region regionBase = daoRegion.extractByName(nomRegion);

        /*
        // 4- Insertion dans la table departement
        Departement departement = new Departement(0, codeDepartement, regionBase.getId());
        daoDepartement.insert(departement);
        Departement departementBase = daoDepartement.extractByCode(codeDepartement); // permet de récupérer une Région avec l'id de la base (qui est auto-incrémenté)

        // 5- Insertion dans la table ville
        Ville ville = new Ville(0, codeCommune, nomCommune, populationTotale, departementBase.getId(), regionBase.getId());
        daoVille.insert(ville);

         */
    }
}
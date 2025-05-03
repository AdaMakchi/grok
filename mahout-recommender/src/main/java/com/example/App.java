package com.example;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            // Charger les données depuis le fichier CSV
            DataModel model = new FileDataModel(new File("data.csv"));

            // Calculer la similarité entre utilisateurs avec la corrélation de Pearson
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            // Définir un voisinage (les 2 utilisateurs les plus similaires)
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

            // Créer le recommandeur basé sur les utilisateurs
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Générer 3 recommandations pour l'utilisateur avec l'ID 1
            List<RecommendedItem> recommendations = recommender.recommend(1, 3);
            for (RecommendedItem recommendation : recommendations) {
                System.out.println("Item recommandé : " + recommendation.getItemID() +
                        ", Score : " + recommendation.getValue());
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
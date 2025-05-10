package com.example.service;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class RecommendationService {


    public List<RecommendedItem> getRecommendations(int userId, int howMany) throws Exception {
      
            // Charger les données depuis le fichier CSV
            DataModel model = new FileDataModel(new File("data.csv"));

            // Calculer la similarité entre utilisateurs avec la corrélation de Pearson
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            // Définir un voisinage (les 2 utilisateurs les plus similaires)
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, model);

            // Créer le recommandeur basé sur les utilisateurs
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            return recommender.recommend(userId, howMany);
    }
}

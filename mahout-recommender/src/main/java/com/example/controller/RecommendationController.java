package com.example.controller;

import com.example.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Recommendation API", description = "Endpoints pour les recommandations d'articles")
public class RecommendationController {

    private final RecommendationService recommenderService;

    public RecommendationController(RecommendationService recommenderService) {
        this.recommenderService = recommenderService;
    }

    @GetMapping("/{userId}")
    @Operation(
        summary = "Obtenir des recommandations",
        description = "Retourne les 4 meilleures recommandations pour un utilisateur donné"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Recommandations trouvées"
    )
    @ApiResponse(
        responseCode = "400", 
        description = "ID utilisateur invalide"
    )
    @ApiResponse(
        responseCode = "404", 
        description = "Utilisateur non trouvé"
    )
    @ApiResponse(
        responseCode = "500", 
        description = "Erreur interne du serveur"
    )
    public ResponseEntity<Map<String, Object>> getRecommendations(
        @Parameter(
            description = "ID de l'utilisateur", 
            example = "1", 
            required = true
        )
        @PathVariable int userId) {

        Map<String, Object> response = new HashMap<>();
        
        try {
            if(userId <= 0) {
                response.put("error", "ID utilisateur invalide");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<RecommendedItem> recommendations = recommenderService.getRecommendations(userId, 4);
            
            if(recommendations.isEmpty()) {
                response.put("message", "Aucune recommandation trouvée");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            List<Map<String, Object>> recList = new ArrayList<>();
            for (RecommendedItem item : recommendations) {
                Map<String, Object> rec = new HashMap<>();
                rec.put("itemId", item.getItemID());
                rec.put("score", String.format("%.2f", item.getValue()));
                recList.add(rec);
            }
            
            response.put("userId", userId);
            response.put("recommendations", recList);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", "Erreur lors du traitement: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
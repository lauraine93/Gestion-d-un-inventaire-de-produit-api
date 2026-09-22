package com.Gestion.d.un.Inventaire.de.Produits.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Réponse d'erreur détaillée")
public class ErrorResponse {

    @Schema(description = "Code HTTP de l'erreur", example = "404")
    private int status;

    @Schema(description = "Message principal de l'erreur", example = "Produit non trouvé")
    private String message;

    @Schema(description = "Détails supplémentaires sur l'erreur")
    private Map<String, String> details;

    @Schema(description = "Horodatage de l'erreur", example = "2025-09-04T14:30:00")
    private LocalDateTime timestamp;
}

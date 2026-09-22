package com.Gestion.d.un.Inventaire.de.Produits.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Requête de création ou mise à jour d'un produit")
public class ProductRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Schema(description = "Nom du produit", example = "Clavier mécanique")
    private String name;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @Schema(description = "Prix du produit en euros", example = "49.99")
    private BigDecimal price;

    @NotNull(message = "La quantité en stock est obligatoire")
    @Min(value = 0, message = "La quantité en stock ne peut pas être négative")
    @Schema(description = "Quantité disponible en stock", example = "10")
    private Integer quantityInStock;
}

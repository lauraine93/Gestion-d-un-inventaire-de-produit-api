package com.Gestion.d.un.Inventaire.de.Produits.dto;

import com.Gestion.d.un.Inventaire.de.Produits.model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Réponse contenant les informations d'un produit")
public class ProductResponse {

    @Schema(description = "Identifiant unique du produit", example = "1")
    private Long id;

    @Schema(description = "Nom du produit", example = "Clavier mécanique")
    private String name;

    @Schema(description = "Prix du produit en euros", example = "49.99")
    private BigDecimal price;

    @Schema(description = "Quantité disponible en stock", example = "10")
    private Integer quantityInStock;

    @Schema(description = "Alerte activée si le stock est inférieur à 5 unités", example = "false")
    private Boolean lowStockAlert;

    public static ProductResponse fromEntity(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantityInStock(product.getQuantityInStock())
                .lowStockAlert(product.getLowStockAlert())
                .build();
    }
}

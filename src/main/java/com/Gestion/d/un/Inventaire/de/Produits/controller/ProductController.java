package com.Gestion.d.un.Inventaire.de.Produits.controller;

import com.Gestion.d.un.Inventaire.de.Produits.dto.ProductRequest;
import com.Gestion.d.un.Inventaire.de.Produits.dto.ProductResponse;
import com.Gestion.d.un.Inventaire.de.Produits.exception.ErrorResponse;
import com.Gestion.d.un.Inventaire.de.Produits.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "API de gestion d'un inventaire de produits")
public class ProductController {

    private final ProductService productService;

    // ==================== GET ALL PRODUCTS ====================

    @GetMapping
    @Operation(
            summary = "Afficher la liste de tous les produits",
            description = "Retourne la liste complète de tous les produits présents dans l'inventaire."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Succès — La liste des produits a été récupérée avec succès. Le body contient un tableau de produits.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "❌ Erreur interne du serveur — Une erreur inattendue s'est produite côté serveur. Vérifiez les logs du serveur.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // ==================== GET PRODUCT BY ID ====================

    @GetMapping("/{id}")
    @Operation(
            summary = "Afficher un produit par son identifiant",
            description = "Retourne les détails complets d'un produit spécifique à partir de son identifiant unique."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Succès — Le produit a été trouvé et retourné avec succès."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌ Ressource non trouvée — Aucun produit n'existe avec l'identifiant fourni. Vérifiez l'ID et réessayez."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "❌ Erreur interne du serveur — Une erreur inattendue s'est produite côté serveur."
            )
    })
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Identifiant unique du produit", required = true, example = "1")
            @PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // ==================== CREATE PRODUCT ====================

    @PostMapping
    @Operation(
            summary = "Créer un nouveau produit",
            description = "Ajoute un nouveau produit à l'inventaire avec son nom, son prix et sa quantité en stock. " +
                    "L'alerte de stock bas est automatiquement activée si la quantité est inférieure à 5 unités."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "✅ Créé — Le produit a été créé avec succès. Le body contient le produit créé avec son ID généré."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌ Requête invalide — Les données envoyées sont incorrectes ou incomplètes. " +
                            "Vérifiez les champs: le nom est obligatoire, le prix doit être > 0, la quantité doit être >= 0. " +
                            "Le body contient les détails des erreurs de validation.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "❌ Erreur interne du serveur — Une erreur inattendue s'est produite côté serveur."
            )
    })
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // ==================== UPDATE PRODUCT ====================

    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour un produit existant",
            description = "Modifie les informations d'un produit existant (nom, prix, quantité en stock). " +
                    "L'alerte de stock bas est recalculée automatiquement."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Succès — Le produit a été mis à jour avec succès. Le body contient le produit modifié."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌ Requête invalide — Les données envoyées sont incorrectes. " +
                            "Vérifiez que le nom n'est pas vide, que le prix est > 0, et que la quantité est >= 0.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌ Ressource non trouvée — Aucun produit n'existe avec l'identifiant fourni. Vérifiez l'ID."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "❌ Erreur interne du serveur — Une erreur inattendue s'est produite côté serveur."
            )
    })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Identifiant unique du produit à modifier", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    // ==================== DELETE PRODUCT ====================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un produit",
            description = "Supprime définitivement un produit de l'inventaire à partir de son identifiant unique."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "✅ Succès — Le produit a été supprimé avec succès. Aucun body n'est retourné."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "❌ Ressource non trouvée — Aucun produit n'existe avec l'identifiant fourni. Vérifiez l'ID."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "❌ Erreur interne du serveur — Une erreur inattendue s'est produite côté serveur."
            )
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Identifiant unique du produit à supprimer", required = true, example = "1")
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== LOW STOCK ALERT ====================

    @GetMapping("/low-stock")
    @Operation(
            summary = "Alerte sur les produits à stock bas",
            description = "Retourne la liste de tous les produits dont la quantité en stock est inférieure à 5 unités. " +
                    "Ces produits sont identifiés par le champ 'lowStockAlert' à true."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Succès — La liste des produits en stock bas a été récupérée. " +
                            "Peut être vide (tableau[]) si aucun produit n'est en rupture de stock."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "❌ Erreur interne du serveur — Une erreur inattendue s'est produite côté serveur."
            )
    })
    public ResponseEntity<List<ProductResponse>> getLowStockProducts() {
        List<ProductResponse> lowStockProducts = productService.getLowStockProducts();
        return ResponseEntity.ok(lowStockProducts);
    }

    // ==================== GET PRODUCTS BY MIN QUANTITY ====================

    @GetMapping("/below-quantity/{threshold}")
    @Operation(
            summary = "Filtrer les produits par quantité maximale",
            description = "Retourne tous les produits dont la quantité en stock est strictement inférieure au seuil spécifié."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "✅ Succès — La liste des produits correspondants a été retournée."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "❌ Requête invalide — Le seuil doit être un nombre positif.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "❌ Erreur interne du serveur — Une erreur inattendue s'est produite côté serveur."
            )
    })
    public ResponseEntity<List<ProductResponse>> getProductsByMinQuantity(
            @Parameter(description = "Seuil maximal de quantité (exclusive)", required = true, example = "10")
            @PathVariable Integer threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException(
                    "Le seuil doit être un nombre positif. Valeur reçue: " + threshold);
        }
        List<ProductResponse> products = productService.getProductsByMinQuantity(threshold);
        return ResponseEntity.ok(products);
    }
}

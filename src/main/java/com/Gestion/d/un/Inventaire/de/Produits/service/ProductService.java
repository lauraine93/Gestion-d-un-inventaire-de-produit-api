package com.Gestion.d.un.Inventaire.de.Produits.service;

import com.Gestion.d.un.Inventaire.de.Produits.dto.ProductRequest;
import com.Gestion.d.un.Inventaire.de.Produits.dto.ProductResponse;
import com.Gestion.d.un.Inventaire.de.Produits.exception.ResourceNotFoundException;
import com.Gestion.d.un.Inventaire.de.Produits.model.Product;
import com.Gestion.d.un.Inventaire.de.Produits.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private static final int LOW_STOCK_THRESHOLD = 5;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'identifiant: " + id));
        return ProductResponse.fromEntity(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .quantityInStock(request.getQuantityInStock())
                .lowStockAlert(request.getQuantityInStock() < LOW_STOCK_THRESHOLD)
                .build();

        Product savedProduct = productRepository.save(product);
        return ProductResponse.fromEntity(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produit non trouvé avec l'identifiant: " + id));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantityInStock(request.getQuantityInStock());
        product.setLowStockAlert(request.getQuantityInStock() < LOW_STOCK_THRESHOLD);

        Product updatedProduct = productRepository.save(product);
        return ProductResponse.fromEntity(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Produit non trouvé avec l'identifiant: " + id);
        }
        productRepository.deleteById(id);
    }

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findByLowStockAlertTrue()
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    public List<ProductResponse> getProductsByMinQuantity(Integer threshold) {
        return productRepository.findByQuantityInStockLessThan(threshold)
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }
}

#  API de Gestion d'un Inventaire de Produits

##  Objectif

API REST permettant de gérer un inventaire de produits avec suivi des stocks et alertes de stock bas.

<img width="1891" height="971" alt="image" src="https://github.com/user-attachments/assets/bcea735b-f4ea-463c-b2f9-0ac76e8ba815" />


##  Stack Technologique

| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Data JPA | (via Spring Boot) |
| PostgreSQL | 16 |
| springdoc-openapi (Swagger) | 3.0.1 |
| Lombok | (via Spring Boot) |
| Maven | (via wrapper inclus) |
| Docker | (pour PostgreSQL) |

---

##  Installation et Démarrage

### Prérequis

- **Java 21** ou supérieur installé
- **Docker** installé et en cours d'exécution
- **Maven** (le wrapper `mvnw` est inclus dans le projet)

### Étape 1 : Démarrer PostgreSQL

Le projet utilise un conteneur Docker pour PostgreSQL sur le **port 5437** (afin de ne pas confliter avec les autres bases de données).

```bash
docker compose up -d
```

Vérifiez que le conteneur est bien lancé :

```bash
docker ps | grep inventaire-postgres
```

Le conteneur crée automatiquement la base `inventaire_produits`.

### Étape 2 : Démarrer l'application Spring Boot

```bash
# Windows
.\mvnw spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

L'application démarre sur le **port 8087**.

### Étape 3 : Accéder à la Documentation Swagger

Ouvrez votre navigateur et allez sur :

```
http://localhost:8087/swagger-ui.html
```

Vous y trouverez l'interface interactive Swagger permettant de tester chaque endpoint directement depuis le navigateur.

---

## 📋 Endpoints de l'API

### Base URL : `http://localhost:8087/api/products`

| Méthode | Endpoint | Description | Code de succès |
|---|---|---|---|
| `GET` | `/api/products` | Lister tous les produits | `200 OK` |
| `GET` | `/api/products/{id}` | Obtenir un produit par son ID | `200 OK` |
| `POST` | `/api/products` | Créer un nouveau produit | `201 Created` |
| `PUT` | `/api/products/{id}` | Mettre à jour un produit | `200 OK` |
| `DELETE` | `/api/products/{id}` | Supprimer un produit | `204 No Content` |
| `GET` | `/api/products/low-stock` | Lister les produits en stock bas (< 5) | `200 OK` |
| `GET` | `/api/products/below-quantity/{threshold}` | Lister les produits sous un seuil | `200 OK` |

---

## 📝 Détails des Endpoints

### 1. Créer un produit

**`POST /api/products`**

**Body (JSON) :**
```json
{
  "name": "Clavier mécanique",
  "price": 49.99,
  "quantityInStock": 10
}
```

**Réponse `201 Created` :**
```json
{
  "id": 1,
  "name": "Clavier mécanique",
  "price": 49.99,
  "quantityInStock": 10,
  "lowStockAlert": false
}
```

**Réponse `400 Bad Request` (champs invalides) :**
```json
{
  "status": 400,
  "message": "Erreurs de validation des données",
  "details": {
    "name": "Le nom du produit est obligatoire",
    "price": "Le prix doit être supérieur à 0"
  },
  "timestamp": "2025-09-04T14:30:00"
}
```

### 2. Lister tous les produits

**`GET /api/products`**

**Réponse `200 OK` :**
```json
[
  {
    "id": 1,
    "name": "Clavier mécanique",
    "price": 49.99,
    "quantityInStock": 10,
    "lowStockAlert": false
  },
  {
    "id": 2,
    "name": "Souris sans fil",
    "price": 29.99,
    "quantityInStock": 3,
    "lowStockAlert": true
  }
]
```

### 3. Obtenir un produit par ID

**`GET /api/products/1`**

**Réponse `200 OK` :**
```json
{
  "id": 1,
  "name": "Clavier mécanique",
  "price": 49.99,
  "quantityInStock": 10,
  "lowStockAlert": false
}
```

**Réponse `404 Not Found` :**
```json
{
  "status": 404,
  "message": "Produit non trouvé avec l'identifiant: 99",
  "timestamp": "2025-09-04T14:30:00"
}
```

### 4. Mettre à jour un produit

**`PUT /api/products/1`**

**Body (JSON) :**
```json
{
  "name": "Clavier mécanique RGB",
  "price": 59.99,
  "quantityInStock": 2
}
```

**Réponse `200 OK` :** Le produit est mis à jour et l'alerte de stock bas est recalculée automatiquement.

### 5. Supprimer un produit

**`DELETE /api/products/1`**

**Réponse `204 No Content`** : Suppression réussie, aucun body retourné.

**Réponse `404 Not Found`** : Le produit avec cet ID n'existe pas.

### 6. Alertes de stock bas

**`GET /api/products/low-stock`**

Retourne tous les produits dont le stock est **inférieur à 5 unités**.

**Réponse `200 OK` :**
```json
[
  {
    "id": 2,
    "name": "Souris sans fil",
    "price": 29.99,
    "quantityInStock": 3,
    "lowStockAlert": true
  }
]
```

### 7. Filtrer par seuil de quantité

**`GET /api/products/below-quantity/10`**

Retourne tous les produits dont le stock est **strictement inférieur** au seuil spécifié.

---

##  Codes de Réponse HTTP

| Code | Signification | Quand |
|---|---|---|
| `200 OK` | La requête a été traitée avec succès | Lecture ou mise à jour d'un produit |
| `201 Created` | Un nouveau produit a été créé avec succès | Création d'un produit |
| `204 No Content` | Suppression réussie, pas de contenu à retourner | Suppression d'un produit |
| `400 Bad Request` | Requête invalide — données manquantes ou incorrectes | Champs obligatoires manquants, prix négatif, quantité négative |
| `404 Not Found` | Ressource non trouvée — l'ID fourni n'existe pas | Recherche, mise à jour ou suppression d'un produit inexistant |
| `500 Internal Server Error` | Erreur interne inattendue côté serveur | Bug inattendu, problème de connexion à la base de données |

---

##  Ports Utilisés

| Service | Port externe | Port interne | Description |
|---|---|---|---|
| **Application Spring Boot** | **8087** | 8087 | API REST |
| **PostgreSQL (inventaire)** | **5437** | 5432 | Base de données de l'inventaire |
| PostgreSQL (bibliothèque) | 5432 | 5432 | ⚠️ Déjà utilisé |
| PostgreSQL (freestock) | 5433 | 5432 | ⚠️ Déjà utilisé |
| PostgreSQL (todo) | 5434 | 5432 | ⚠️ Déjà utilisé |
| PostgreSQL (gestion-utilisateurs) | 5435 | 5432 | ⚠️ Déjà utilisé |
| PostgreSQL (blog) | 5436 | 5432 | ⚠️ Déjà utilisé |

> Le port **5437** a été choisi pour PostgreSQL afin de ne pas confliter avec les bases existantes.

---

## 🧪 Comment Tester l'API

### Méthode 1 : Swagger UI (Recommandé)

1. Démarrez l'application (voir Étape 2)
2. Ouvrez `http://localhost:8087/swagger-ui.html`
3. Cliquez sur un endpoint pour le déplier
4. Cliquez sur **"Try it out"**
5. Remplissez les champs et cliquez sur **"Execute"**

### Méthode 2 : cURL (Terminal)

**Créer un produit :**
```bash
curl -X POST http://localhost:8087/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Clavier mécanique", "price": 49.99, "quantityInStock": 10}'
```

**Lister tous les produits :**
```bash
curl http://localhost:8087/api/products
```

**Obtenir un produit :**
```bash
curl http://localhost:8087/api/products/1
```

**Mettre à jour un produit :**
```bash
curl -X PUT http://localhost:8087/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Clavier mécanique RGB", "price": 59.99, "quantityInStock": 2}'
```

**Supprimer un produit :**
```bash
curl -X DELETE http://localhost:8087/api/products/1
```

**Voir les produits en stock bas :**
```bash
curl http://localhost:8087/api/products/low-stock
```

**Filtrer par seuil de quantité :**
```bash
curl http://localhost:8087/api/products/below-quantity/10
```

### Méthode 3 : Postman

1. Créez une nouvelle requête
2. Entrez l'URL : `http://localhost:8087/api/products`
3. Sélectionnez la méthode HTTP (GET, POST, PUT, DELETE)
4. Pour POST/PUT, allez dans l'onglet **Body > raw > JSON** et collez le JSON
5. Envoyez la requête

---

##  Structure du Projet

```
src/main/java/com/Gestion/d/un/Inventaire/de/Produits/
├── Application.java                  # Point d'entrée Spring Boot
├── model/
│   └── Product.java                  # Entité JPA (id, name, price, quantityInStock, lowStockAlert)
├── dto/
│   ├── ProductRequest.java           # DTO de requête (création/mise à jour)
│   └── ProductResponse.java          # DTO de réponse (retour API)
├── repository/
│   └── ProductRepository.java        # Interface Spring Data JPA
├── service/
│   └── ProductService.java           # Logique métier (CRUD + alertes stock bas)
├── controller/
│   └── ProductController.java        # Endpoints REST + documentation Swagger
└── exception/
    ├── ResourceNotFoundException.java  # Exception personnalisée 404
    ├── ErrorResponse.java            # Modèle de réponse d'erreur
    └── GlobalExceptionHandler.java   # Gestionnaire global des exceptions
```

---

## 📊 Modèle de Données

### Table `products`

| Colonne | Type | Contrainte | Description |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO INCREMENT | Identifiant unique |
| `name` | VARCHAR | NOT NULL | Nom du produit |
| `price` | DECIMAL(10,2) | NOT NULL | Prix du produit |
| `quantity_in_stock` | INTEGER | NOT NULL | Quantité en stock |
| `low_stock_alert` | BOOLEAN | NOT NULL | Alerte activée si stock < 5 |

> La table est créée automatiquement par Hibernate au démarrage (`ddl-auto=update`).

---

##  Arrêter les Services

```bash
# Arrêter l'application Spring Boot : Ctrl+C dans le terminal

# Arrêter PostgreSQL :
docker compose down

# Arrêter PostgreSQL ET supprimer les données :
docker compose down -v
```

---

## 🔧 Configuration

Les paramètres de configuration se trouvent dans `src/main/resources/application.properties` :

| Propriété | Valeur | Description |
|---|---|---|
| `server.port` | `8087` | Port de l'application |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5437/inventaire_produits` | URL de connexion à la base |
| `spring.datasource.username` | `postgres` | Utilisateur PostgreSQL |
| `spring.datasource.password` | `postgres` | Mot de passe PostgreSQL |
| `spring.jpa.hibernate.ddl-auto` | `update` | Mise à jour automatique du schéma |

---

##  Notes Importantes

1. **Alerte de stock bas** : Le champ `lowStockAlert` est calculé automatiquement. Il passe à `true` lorsque la quantité en stock est **inférieure à 5 unités**.

2. **Port PostgreSQL** : Le port `5437` a été choisi spécialement pour ce projet afin de ne pas interferer avec les autres projets utilisant PostgreSQL sur les ports 5432 à 5436.

3. **Documentation Swagger** : Toute la documentation des codes de réponse (200, 201, 400, 404, 500) avec descriptions détaillées est intégrée dans le code via les annotations `@ApiResponses`. Elle est consultable sur l'interface Swagger UI et renvoyée dans les métadonnées de l'API.

4. **Sérialisation Lombok** : Les classes utilisent Lombok (`@Getter`, `@Setter`, `@Builder`, etc.) pour réduire le code boilerplate. Aucun getter/setter manuel n'est nécessaire.

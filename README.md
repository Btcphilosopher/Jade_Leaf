# Jade Leaf 茶馆 - Premium Luxury Tea House App

Jade Leaf is a modern luxury tea house experience inspired by traditional Chinese tea culture, contemporary architecture, and boutique hospitality. This workspace contains the complete production-grade source code for both the **Kotlin Jetpack Compose Android Client** and the **Spring Boot Enterprise Backend**.

---

## 🎨 Visual Identity & Style Guide
The visual design replicates the serenity and tranquility of entering a physical luxury tea lounge:
* **Palette**: Jade Green (`#1E433A`), Ivory White (`#FAFAF7`), Matte Black Stone (`#0C110E`), and Gold accents (`#B18E29`).
* **Typography Pairing**: Elegant classical serif display headings (`FontFamily.Serif`) paired with clean sans-serif body fonts for superb readability.
* **Density & Negative Space**: Generous margins, rounded cards (16dp), tonal elevation, and edge-to-edge layouts (`enableEdgeToEdge()`).
* **Visual Polish**: Curated custom hero banners, tea ceremony layouts, and a gold-brushed virtual digital membership card.

---

## 📱 Deliverable 1: Android Kotlin Application
The mobile client is built following clean **MVVM (Model-View-ViewModel)** architecture:

### Tech Stack
* **Language**: Kotlin 2.2.10
* **Framework**: Jetpack Compose with Material Design 3 (M3)
* **Database**: Room Persistence (with full local reactive updates)
* **Async**: Kotlin Coroutines & Flow
* **Images**: Coil Compose image loading
* **Testing**: Robolectric local JVM testing and Roborazzi screenshot verification

### Core Architectural Components
1. **`MainActivity.kt`**: Connects edge-to-edge layout settings, initializes the single-instance Room Database, and binds the Repository and ViewModel via `TeaViewModelFactory`.
2. **`TeaDatabase.kt`**: Declares Room entities (`UserEntity`, `ProductEntity`, `StoreEntity`, `OrderEntity`, `ReservationEntity`, `ArticleEntity`) and handles transactional DAO flows.
3. **`TeaRepository.kt`**: Handles reactive database operations, automatically pre-populating luxury bilingual items upon first launch.
4. **`TeaViewModel.kt`**: Manages all active UI states—including shopping cart customisation selectors, reservation slots, local balance deductions, and dynamic status progress timers.
5. **`TeaAppScreens.kt`**:
   * **Home Screen**: Features a stunning bamboo-garden hero view, membership rewards status, quick actions, and seasonal carousels.
   * **Digital Menu**: Offers tabbed item exploration, search, customizable options (strength, sugar, ice, milk), and detailed tea specifications.
   * **Store Locator**: Displays locations, facility lists, opening hours, directions, and favorites toggling.
   * **Tea Academy**: Provides interactive countdown steeping timers with animated gauges alongside cultural articles.
   * **Reservations**: Offers seat preference selectors and a calendar widget.
   * **Account Screen**: Displays a virtual membership card, account balance top-ups, and live-updating order history.
   * **Staff Admin Portal**: Toggles staff roles to manage products (CRUD) and dispatch orders (Received -> Preparing -> Ready -> Served) with instant reactive Room updates.

---

## 💻 Deliverable 2: Enterprise Spring Boot Backend
The backend sources reside inside the `/backend` folder:

* **`schema.sql`**: Complete PostgreSQL schema defining relational tables for users, stores, products, inventory, reservations, orders, payments, loyalty points, and push logs.
* **`BackendApplication.kt`**: Real Spring Boot application in Kotlin. Features JPA Entity mappings, secure Spring Services, and active `@RestController` APIs.
* **`ApiDocumentation.md`**: REST API endpoints, request/response formats, and JWT secure headers.
* **`Dockerfile`**: Multistage container setup using Eclipse Temurin JDK 17.
* **`k8s.yaml`**: ConfigMaps, Deployment structures (3 replicas), services, and a Horizontal Pod Autoscaler.

---

## 🛠️ Compilation and Execution Instructions

### A. Android Application Compilation (AI Studio Platform)
To build and check the application compile status:
1. Trigger the built-in compiler tool:
   ```bash
   compile_applet
   ```
2. Verify local unit and Robolectric tests:
   ```bash
   gradle :app:testDebugUnitTest
   ```

### B. Spring Boot Backend Execution
To spin up the Spring Boot server locally or in a container:
1. **Maven local launch**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
2. **Docker local container build**:
   ```bash
   cd backend
   docker build -t jade-leaf/tea-backend .
   docker run -p 8080:8080 jade-leaf/tea-backend
   ```

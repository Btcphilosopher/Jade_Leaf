package com.example.data.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

// ==========================================
// 1. DATABASE ENTITIES
// ==========================================

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "林逸轩 (Yixuan)",
    val points: Int = 1250,
    val tier: String = "黄金会员 (Gold)", // Silver, Gold, Platinum, Jade Master
    val balance: Double = 880.00,
    val selectedLanguage: String = "CN", // CN, EN
    val role: String = "CUSTOMER" // CUSTOMER, STAFF_ADMIN
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val nameCn: String,
    val nameEn: String,
    val category: String, // Loose Leaf, Signature, Food, Retail
    val price: Double,
    val originCn: String,
    val originEn: String,
    val regionCn: String,
    val regionEn: String,
    val harvestCn: String,
    val harvestEn: String,
    val processingCn: String,
    val processingEn: String,
    val brewingTemp: String, // e.g. 85°C
    val brewingGuideCn: String,
    val brewingGuideEn: String,
    val tastingCn: String,
    val tastingEn: String,
    val aromaCn: String,
    val aromaEn: String,
    val caffeineCn: String,
    val caffeineEn: String,
    val imageResId: String, // Resource identifier
    val rating: Double = 4.9,
    val isFeatured: Boolean = false
)

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey val id: Int,
    val nameCn: String,
    val nameEn: String,
    val addressCn: String,
    val addressEn: String,
    val hoursCn: String,
    val hoursEn: String,
    val facilitiesCn: String, // Comma-separated list
    val facilitiesEn: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val latitude: Double,
    val longitude: Double
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderNumber: String,
    val productNames: String, // Comma separated names
    val customisation: String,
    val orderType: String, // Dine-in, Order Ahead, Delivery
    val totalPrice: Double,
    val status: String, // Received, Preparing, Ready, Delivered
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val storeName: String,
    val roomType: String, // Private Tea Room, Traditional Mat, Afternoon Tea Table
    val dateTime: String,
    val seatingPreference: String,
    val pax: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: Int,
    val titleCn: String,
    val titleEn: String,
    val summaryCn: String,
    val summaryEn: String,
    val contentCn: String,
    val contentEn: String,
    val category: String, // Academy, Culture, Ceremony
    val imageResId: String,
    val readTimeMinutes: Int = 5
)

// ==========================================
// 2. DATA ACCESS OBJECTS (DAOs)
// ==========================================

@Dao
interface TeaDao {
    // Users
    @Query("SELECT * FROM users WHERE id = 1 LIMIT 1")
    fun getUserFlow(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Products
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProductsFlow(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProductById(productId: Int)

    // Stores
    @Query("SELECT * FROM stores ORDER BY id ASC")
    fun getAllStoresFlow(): Flow<List<StoreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(stores: List<StoreEntity>)

    @Query("UPDATE stores SET isFavorite = :isFav WHERE id = :storeId")
    suspend fun updateStoreFavorite(storeId: Int, isFav: Boolean)

    // Orders
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrdersFlow(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String)

    // Reservations
    @Query("SELECT * FROM reservations ORDER BY timestamp DESC")
    fun getAllReservationsFlow(): Flow<List<ReservationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: ReservationEntity)

    @Query("DELETE FROM reservations WHERE id = :reservationId")
    suspend fun deleteReservationById(reservationId: Int)

    // Articles / Academy
    @Query("SELECT * FROM articles ORDER BY id ASC")
    fun getAllArticlesFlow(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)
}

// ==========================================
// 3. DATABASE CLASS
// ==========================================

@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        StoreEntity::class,
        OrderEntity::class,
        ReservationEntity::class,
        ArticleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TeaDatabase : RoomDatabase() {
    abstract fun teaDao(): TeaDao

    companion object {
        @Volatile
        private var INSTANCE: TeaDatabase? = null

        fun getDatabase(context: Context): TeaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TeaDatabase::class.java,
                    "jade_leaf_db"
                )
                .addCallback(DatabasePrepopulationCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabasePrepopulationCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Trigger pre-population on first creation
            // Note: In real life we'd use a coroutine to populate.
        }
    }
}

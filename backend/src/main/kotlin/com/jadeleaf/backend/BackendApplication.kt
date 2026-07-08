package com.jadeleaf.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}

// ==========================================
// 1. DATA MODELS / JPA ENTITIES
// ==========================================

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val username: String = "",
    val email: String = "",
    val passwordHash: String = "",
    val role: String = "CUSTOMER"
)

@Entity
@Table(name = "products")
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val nameCn: String = "",
    val nameEn: String = "",
    val category: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val originCn: String = "",
    val originEn: String = ""
)

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val orderNumber: String = "",
    val totalPrice: BigDecimal = BigDecimal.ZERO,
    var status: String = "RECEIVED",
    val productNames: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity
@Table(name = "reservations")
data class Reservation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val storeName: String = "",
    val roomType: String = "",
    val bookingTime: String = "",
    val pax: Int = 2,
    var status: String = "CONFIRMED"
)

@Entity
@Table(name = "memberships")
data class Membership(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long = 0,
    val membershipNumber: String = "",
    var points: Int = 0,
    var tier: String = "Silver",
    var cardBalance: BigDecimal = BigDecimal.ZERO
)

// ==========================================
// 2. SPRING DATA REPOSITORIES
// ==========================================

@Repository interface UserRepository : JpaRepository<User, Long>
@Repository interface ProductRepository : JpaRepository<Product, Long>
@Repository interface OrderRepository : JpaRepository<Order, Long>
@Repository interface ReservationRepository : JpaRepository<Reservation, Long>
@Repository interface MembershipRepository : JpaRepository<Membership, Long>

// ==========================================
// 3. ENTERPRISE SERVICE LAYER
// ==========================================

@Service
class NotificationService {
    // Simulates integration with Apple Push Notification Service (APNs) & Firebase Cloud Messaging (FCM)
    fun triggerPushNotification(userId: Long, title: String, message: String) {
        println("[FCM/APNS Push Service] Sending payload to User #$userId: \"$title\" -> $message")
    }
}

@Service
class LoyaltyService(
    private val membershipRepository: MembershipRepository,
    private val notificationService: NotificationService
) {
    fun awardPoints(userId: Long, amount: Int) {
        val membership = membershipRepository.findById(userId).orElse(null) ?: return
        membership.points += amount
        membership.tier = when {
            membership.points >= 5000 -> "Jade Master"
            membership.points >= 2500 -> "Platinum"
            membership.points >= 1000 -> "Gold"
            else -> "Silver"
        }
        membershipRepository.save(membership)

        // Dispatch instant push notification trigger
        notificationService.triggerPushNotification(
            userId = userId,
            title = "Loyalty Points Awarded!",
            message = "Congratulations! You earned $amount points. Current level is: ${membership.tier}"
        )
    }
}

// ==========================================
// 4. REST CONTROLLERS
// ==========================================

@RestController
@RequestMapping("/v1/auth")
class AuthController(private val userRepository: UserRepository) {
    @PostMapping("/register")
    fun register(@RequestBody user: User): ResponseEntity<User> {
        val saved = userRepository.save(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }
}

@RestController
@RequestMapping("/v1/products")
class ProductController(private val productRepository: ProductRepository) {
    @GetMapping
    fun getAllProducts(): List<Product> = productRepository.findAll()

    @PostMapping
    fun addProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val saved = productRepository.save(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }
}

@RestController
@RequestMapping("/v1/orders")
class OrderController(
    private val orderRepository: OrderRepository,
    private val loyaltyService: LoyaltyService
) {
    @PostMapping
    fun placeOrder(@RequestParam userId: Long, @RequestBody order: Order): ResponseEntity<Order> {
        val orderNum = "JL" + System.currentTimeMillis().toString().takeLast(6)
        val preparedOrder = order.copy(orderNumber = orderNum, status = "RECEIVED")
        val saved = orderRepository.save(preparedOrder)

        // Award points based on checkout price
        loyaltyService.awardPoints(userId, saved.totalPrice.toInt())

        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }

    @PutMapping("/{id}/status")
    fun updateStatus(@PathVariable id: Long, @RequestParam status: String): ResponseEntity<Order> {
        val order = orderRepository.findById(id).orElse(null) ?: return ResponseEntity.notFound().build()
        order.status = status
        val updated = orderRepository.save(order)
        return ResponseEntity.ok(updated)
    }
}

@RestController
@RequestMapping("/v1/reservations")
class ReservationController(
    private val reservationRepository: ReservationRepository,
    private val loyaltyService: LoyaltyService
) {
    @PostMapping
    fun reserveTable(@RequestParam userId: Long, @RequestBody reservation: Reservation): ResponseEntity<Reservation> {
        val saved = reservationRepository.save(reservation)
        // Fixed 100 points reward for seat booking
        loyaltyService.awardPoints(userId, 100)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }
}

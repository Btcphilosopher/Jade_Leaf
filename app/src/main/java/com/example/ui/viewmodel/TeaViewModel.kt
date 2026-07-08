package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.*
import com.example.data.repository.TeaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CartItem(
    val product: ProductEntity,
    val quantity: Int = 1,
    val teaStrength: String = "标准 (Medium)", // 标准 (Medium), 浓郁 (Strong), 醇厚 (Extra Strong)
    val sugarLevel: String = "半糖 (50% Sugar)", // 无糖 (Unsweetened), 少糖 (30%), 半糖 (50%), 常规糖 (100%)
    val iceLevel: String = "少冰 (Less Ice)", // 常温 (Room Temp), 去冰 (No Ice), 少冰 (Less Ice), 常规冰 (Standard)
    val milkOption: String = "无奶 (No Milk)" // 无奶 (No Milk), 有机全脂奶 (Whole Milk), 燕麦奶 (Oat Milk), 椰奶 (Coconut Milk)
)

class TeaViewModel(private val repository: TeaRepository) : ViewModel() {

    // 1. Core flows from database
    val user: StateFlow<UserEntity?> = repository.userFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val products: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stores: StateFlow<List<StoreEntity>> = repository.allStores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reservations: StateFlow<List<ReservationEntity>> = repository.allReservations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val articles: StateFlow<List<ArticleEntity>> = repository.allArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. Active UI States
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Premium Loose Leaf Teas")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _activeTrackedOrderId = MutableStateFlow<Int?>(null)
    val activeTrackedOrderId: StateFlow<Int?> = _activeTrackedOrderId.asStateFlow()

    // 3. Setup and pre-population
    init {
        viewModelScope.launch {
            repository.prepopulateDatabaseIfEmpty()
        }
    }

    // Language / Role Controllers
    fun toggleLanguage() {
        viewModelScope.launch {
            val currentUser = user.value ?: UserEntity()
            val nextLang = if (currentUser.selectedLanguage == "CN") "EN" else "CN"
            repository.updateUserLanguage(nextLang)
        }
    }

    fun toggleRole() {
        viewModelScope.launch {
            val currentUser = user.value ?: UserEntity()
            val nextRole = if (currentUser.role == "CUSTOMER") "STAFF_ADMIN" else "CUSTOMER"
            repository.updateUserRole(nextRole)
        }
    }

    // Cart Operations
    fun addToCart(product: ProductEntity, quantity: Int, strength: String, sugar: String, ice: String, milk: String) {
        val currentList = _cart.value.toMutableList()
        // Check if exact customization item exists
        val existingIndex = currentList.indexOfFirst {
            it.product.id == product.id &&
            it.teaStrength == strength &&
            it.sugarLevel == sugar &&
            it.iceLevel == ice &&
            it.milkOption == milk
        }

        if (existingIndex != -1) {
            val existing = currentList[existingIndex]
            currentList[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            currentList.add(CartItem(product, quantity, strength, sugar, ice, milk))
        }
        _cart.value = currentList
    }

    fun removeFromCart(item: CartItem) {
        _cart.value = _cart.value.filter { it != item }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    fun updateCartQuantity(item: CartItem, change: Int) {
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOf(item)
        if (index != -1) {
            val updatedQty = item.quantity + change
            if (updatedQty <= 0) {
                currentList.removeAt(index)
            } else {
                currentList[index] = item.copy(quantity = updatedQty)
            }
            _cart.value = currentList
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Checkout Order Flow
    fun checkout(orderType: String) {
        if (_cart.value.isEmpty()) return
        val itemsList = _cart.value
        val totalPrice = itemsList.sumOf { it.product.price * it.quantity }
        val productNames = itemsList.joinToString(", ") {
            val currentUser = user.value
            if (currentUser?.selectedLanguage == "CN") it.product.nameCn else it.product.nameEn
        }
        val firstItem = itemsList.first()
        val customizationDetails = "Strength: ${firstItem.teaStrength}, Sugar: ${firstItem.sugarLevel}, Ice: ${firstItem.iceLevel}, Milk: ${firstItem.milkOption}"

        viewModelScope.launch {
            val orderNum = "JL" + System.currentTimeMillis().toString().takeLast(6)
            val newOrder = OrderEntity(
                orderNumber = orderNum,
                productNames = productNames,
                customisation = customizationDetails,
                orderType = orderType,
                totalPrice = totalPrice,
                status = "Received"
            )
            val orderId = repository.createOrder(newOrder).toInt()

            // Award Loyalty Points: 1 point per CNY/Double
            val pointsEarned = totalPrice.toInt()
            repository.addLoyaltyPoints(pointsEarned)

            // Deduct balance
            repository.updateBalance(-totalPrice)

            // Set tracked order
            _activeTrackedOrderId.value = orderId

            // Clear Cart
            clearCart()

            // Simulate Live Tracking (Received -> Preparing -> Ready)
            simulateOrderProgress(orderId)
        }
    }

    private fun simulateOrderProgress(orderId: Int) {
        viewModelScope.launch {
            // 1. Status: Received (Just placed)
            delay(10000) // 10 seconds to Preparing
            repository.updateOrderStatus(orderId, "Preparing")

            delay(15000) // 15 seconds to Ready
            repository.updateOrderStatus(orderId, "Ready")

            delay(20000) // 20 seconds to Delivered / Complete
            repository.updateOrderStatus(orderId, "Delivered")
        }
    }

    // Reservation Flow
    fun bookReservation(storeName: String, roomType: String, dateTime: String, seating: String, pax: Int) {
        viewModelScope.launch {
            val booking = ReservationEntity(
                storeName = storeName,
                roomType = roomType,
                dateTime = dateTime,
                seatingPreference = seating,
                pax = pax
            )
            repository.bookReservation(booking)
            repository.addLoyaltyPoints(100) // 100 points for booking a reservation!
        }
    }

    fun cancelReservation(id: Int) {
        viewModelScope.launch {
            repository.cancelReservation(id)
        }
    }

    // Favorite Store Toggle
    fun toggleFavoriteStore(storeId: Int, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleStoreFavorite(storeId, !currentFav)
        }
    }

    // Admin Portal Operations
    fun adminAddProduct(nameCn: String, nameEn: String, category: String, price: Double, originCn: String, originEn: String) {
        viewModelScope.launch {
            val newId = (products.value.maxOfOrNull { it.id } ?: 100) + 1
            val product = ProductEntity(
                id = newId,
                nameCn = nameCn,
                nameEn = nameEn,
                category = category,
                price = price,
                originCn = originCn,
                originEn = originEn,
                regionCn = "行政优选 (Staff Handpicked)",
                regionEn = "Staff Selected Estate",
                harvestCn = "四季精采",
                harvestEn = "Year-Round Harvest",
                processingCn = "精工制作",
                processingEn = "Carefully Finished",
                brewingTemp = "90°C",
                brewingGuideCn = "标准茶壶温润冲泡即可。",
                brewingGuideEn = "Standard porcelain pot brewing.",
                tastingCn = "甘醇可口，芬芳隽雅。",
                tastingEn = "Mellow and wonderfully fragrant.",
                aromaCn = "清雅四溢",
                aromaEn = "Charming floral notes",
                caffeineCn = "低",
                caffeineEn = "Low",
                imageResId = "img_app_icon"
            )
            repository.saveProduct(product)
        }
    }

    fun adminDeleteProduct(id: Int) {
        viewModelScope.launch {
            repository.deleteProduct(id)
        }
    }

    fun adminUpdateOrderStatus(orderId: Int, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
        }
    }

    fun updateBalance(amount: Double) {
        viewModelScope.launch {
            repository.updateBalance(amount)
            if (amount > 0) {
                repository.addLoyaltyPoints(amount.toInt())
            }
        }
    }
}

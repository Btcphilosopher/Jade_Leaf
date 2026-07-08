package com.example.ui.screens

import android.os.CountDownTimer
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.database.*
import com.example.ui.viewmodel.CartItem
import com.example.ui.viewmodel.TeaViewModel
import java.text.SimpleDateFormat
import java.util.*

// Helper function to dynamically map generated JPG assets
fun getTeaDrawable(imageName: String): Int {
    return when (imageName) {
        "img_app_icon" -> R.drawable.img_app_icon_1783509045891
        "img_hero_banner" -> R.drawable.img_hero_banner_1783509064911
        "img_tea_academy" -> R.drawable.img_tea_academy_1783509076198
        else -> R.drawable.img_app_icon_1783509045891
    }
}

// Global localization dictionary for dynamic translations
object Lang {
    fun translate(key: String, isCn: Boolean): String {
        return if (isCn) {
            when (key) {
                "brand" -> "Jade Leaf 茶馆"
                "slogan" -> "静心一隅，慢品清茗"
                "nav_home" -> "精选"
                "nav_menu" -> "茶单"
                "nav_locator" -> "茶馆"
                "nav_academy" -> "学堂"
                "nav_account" -> "尊享"
                "loyalty_points" -> "积分"
                "loyalty_tier" -> "会员级别"
                "nearby_stores" -> "附近茶馆"
                "seasonal_collection" -> "时令珍品"
                "tea_month" -> "本月茶魁"
                "featured_articles" -> "茶馆雅谈"
                "order_now" -> "立即预订"
                "order_ahead" -> "提前点单"
                "facilities" -> "设施配备"
                "hours" -> "营业时间"
                "reserve_table" -> "预订私室"
                "reserve_btn" -> "立即订座"
                "fav_stores" -> "已收藏茶馆"
                "menu_search_placeholder" -> "搜寻你钟爱的茶品/糕点..."
                "customise" -> "定制茶品"
                "tea_strength" -> "茶汤浓度"
                "sugar_level" -> "甜度定制"
                "ice_level" -> "冰量调配"
                "milk_option" -> "牛乳/燕麦奶"
                "add_to_cart" -> "加入点单"
                "cart_title" -> "点单篮"
                "total" -> "小计"
                "checkout_btn" -> "去结算"
                "checkout_success" -> "订单已提交！正在安排现沏..."
                "tracking_title" -> "茶品现沏进度"
                "order_received" -> "茶馆已接单"
                "order_preparing" -> "茶艺师正现沏中"
                "order_ready" -> "茶品已备好，请至吧台自提"
                "order_delivered" -> "已享用 / 送达"
                "balance" -> "茶卡余额"
                "topup" -> "充值卡券"
                "history_order" -> "点单历史"
                "history_res" -> "雅座历史"
                "customer_portal" -> "客户模式"
                "admin_portal" -> "管理大厅"
                "admin_add_product" -> "上架新商品"
                "admin_analytics" -> "连锁店实时分析"
                "origin" -> "产地"
                "region" -> "核心茶区"
                "harvest" -> "采摘时令"
                "processing" -> "传统工艺"
                "brewing_guide" -> "沏茶纲领"
                "tasting_notes" -> "风味特征"
                "aroma_profile" -> "香气脉络"
                "caffeine_level" -> "咖啡因度"
                "pax_label" -> "宴请人数"
                "date_label" -> "日期时段"
                "seating_label" -> "席位偏好"
                "cancel_booking" -> "撤销预订"
                "book_success" -> "雅院已为您预留尊贵席位"
                else -> key
            }
        } else {
            when (key) {
                "brand" -> "Jade Leaf"
                "slogan" -> "A sanctuary of luxury tea hospitality"
                "nav_home" -> "Featured"
                "nav_menu" -> "Menu"
                "nav_locator" -> "Stores"
                "nav_academy" -> "Academy"
                "nav_account" -> "Club"
                "loyalty_points" -> "Points"
                "loyalty_tier" -> "Tier"
                "nearby_stores" -> "Our Tea Houses"
                "seasonal_collection" -> "Seasonal Curations"
                "tea_month" -> "Tea of the Month"
                "featured_articles" -> "Editorial Articles"
                "order_now" -> "Reserve Now"
                "order_ahead" -> "Order Ahead"
                "facilities" -> "Premium Amenities"
                "hours" -> "Opening Hours"
                "reserve_table" -> "Reserve Private Room"
                "reserve_btn" -> "Book Selected Table"
                "fav_stores" -> "Saved Locations"
                "menu_search_placeholder" -> "Search tea leaves, lattes, or pastries..."
                "customise" -> "Customize Drink"
                "tea_strength" -> "Tea Strength"
                "sugar_level" -> "Sugar Level"
                "ice_level" -> "Ice Level"
                "milk_option" -> "Milk Base"
                "add_to_cart" -> "Add to Cart"
                "cart_title" -> "Cart"
                "total" -> "Subtotal"
                "checkout_btn" -> "Place Order"
                "checkout_success" -> "Order placed! Our Tea Master is preparing..."
                "tracking_title" -> "Brewing Progress"
                "order_received" -> "Order Received"
                "order_preparing" -> "Tea Master Hand-Brewing"
                "order_ready" -> "Ready for Collection"
                "order_delivered" -> "Served / Delivered"
                "balance" -> "Card Balance"
                "topup" -> "Top Up Balance"
                "history_order" -> "Order History"
                "history_res" -> "Reservations"
                "customer_portal" -> "Customer Portal"
                "admin_portal" -> "Admin Portal"
                "admin_add_product" -> "List New Product"
                "admin_analytics" -> "Franchise Analytics"
                "origin" -> "Origin"
                "region" -> "Core Terroir"
                "harvest" -> "Harvest Season"
                "processing" -> "Crafting Method"
                "brewing_guide" -> "Brewing Guide"
                "tasting_notes" -> "Tasting Profile"
                "aroma_profile" -> "Aroma Signature"
                "caffeine_level" -> "Caffeine Level"
                "pax_label" -> "Number of Guests"
                "date_label" -> "Date & Time Slot"
                "seating_label" -> "Seating Preference"
                "cancel_booking" -> "Cancel Reservation"
                "book_success" -> "Sanctuary table reserved successfully"
                else -> key
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TeaViewModel) {
    val userState by viewModel.user.collectAsStateWithLifecycle()
    val isCn = (userState?.selectedLanguage ?: "CN") == "CN"
    val isCustomer = (userState?.role ?: "CUSTOMER") == "CUSTOMER"

    var selectedTab by remember { mutableStateOf("home") }
    val cartItems by viewModel.cart.collectAsStateWithLifecycle()
    var showCartDialog by remember { mutableStateOf(false) }

    val activeTrackedOrderId by viewModel.activeTrackedOrderId.collectAsStateWithLifecycle()
    val ordersList by viewModel.orders.collectAsStateWithLifecycle()
    val activeTrackedOrder = ordersList.find { it.id == activeTrackedOrderId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = Lang.translate("brand", isCn),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = Lang.translate("slogan", isCn),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                actions = {
                    // Quick language switcher with premium pill
                    IconButton(
                        onClick = { viewModel.toggleLanguage() },
                        modifier = Modifier.testTag("lang_toggle_button")
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = if (isCn) "EN" else "中",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Cart Icon badge
                    if (cartItems.isNotEmpty()) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = MaterialTheme.colorScheme.tertiary) {
                                    Text(
                                        cartItems.sumOf { it.quantity }.toString(),
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        ) {
                            IconButton(
                                onClick = { showCartDialog = true },
                                modifier = Modifier.testTag("cart_header_icon")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "Shopping Cart",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 8.dp
            ) {
                val homeLabel = Lang.translate("nav_home", isCn)
                val menuLabel = Lang.translate("nav_menu", isCn)
                val locatorLabel = Lang.translate("nav_locator", isCn)
                val academyLabel = Lang.translate("nav_academy", isCn)
                val accountLabel = Lang.translate("nav_account", isCn)

                NavigationBarItem(
                    selected = selectedTab == "home",
                    onClick = { selectedTab = "home" },
                    icon = { Icon(Icons.Outlined.AutoAwesome, contentDescription = homeLabel) },
                    label = { Text(homeLabel) },
                    modifier = Modifier.testTag("nav_home_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == "menu",
                    onClick = { selectedTab = "menu" },
                    icon = { Icon(Icons.Outlined.RestaurantMenu, contentDescription = menuLabel) },
                    label = { Text(menuLabel) },
                    modifier = Modifier.testTag("nav_menu_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == "locator",
                    onClick = { selectedTab = "locator" },
                    icon = { Icon(Icons.Outlined.Map, contentDescription = locatorLabel) },
                    label = { Text(locatorLabel) },
                    modifier = Modifier.testTag("nav_locator_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == "academy",
                    onClick = { selectedTab = "academy" },
                    icon = { Icon(Icons.Outlined.School, contentDescription = academyLabel) },
                    label = { Text(academyLabel) },
                    modifier = Modifier.testTag("nav_academy_tab")
                )
                NavigationBarItem(
                    selected = selectedTab == "account",
                    onClick = { selectedTab = "account" },
                    icon = { Icon(Icons.Outlined.CardMembership, contentDescription = accountLabel) },
                    label = { Text(accountLabel) },
                    modifier = Modifier.testTag("nav_account_tab")
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Real-time floating Order Tracking Bar if an order is active
                activeTrackedOrder?.let { order ->
                    if (order.status != "Delivered") {
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTab = "account" }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .testTag("active_order_banner")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalCafe,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${Lang.translate("tracking_title", isCn)} (#${order.orderNumber})",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = order.productNames,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = when (order.status) {
                                            "Received" -> Lang.translate("order_received", isCn)
                                            "Preparing" -> Lang.translate("order_preparing", isCn)
                                            "Ready" -> Lang.translate("order_ready", isCn)
                                            else -> order.status
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Cross-cutting view swap between Customer views and Admin dashboard
                if (!isCustomer) {
                    AdminPortalScreen(viewModel = viewModel, isCn = isCn)
                } else {
                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                        },
                        label = "TabTransition"
                    ) { tab ->
                        when (tab) {
                            "home" -> HomeScreen(viewModel = viewModel, isCn = isCn, onNavigateToMenu = { selectedTab = "menu" }, onNavigateToLocator = { selectedTab = "locator" })
                            "menu" -> MenuScreen(viewModel = viewModel, isCn = isCn)
                            "locator" -> StoreLocatorScreen(viewModel = viewModel, isCn = isCn, onNavigateToBooking = { selectedTab = "account" })
                            "academy" -> TeaAcademyScreen(viewModel = viewModel, isCn = isCn)
                            "account" -> AccountScreen(viewModel = viewModel, isCn = isCn)
                        }
                    }
                }
            }

            // Cart Dialog bottom sheet or centered Dialog
            if (showCartDialog) {
                CartDetailsDialog(
                    cartItems = cartItems,
                    viewModel = viewModel,
                    isCn = isCn,
                    onDismiss = { showCartDialog = false }
                )
            }
        }
    }
}

// ==========================================
// 4. SCREEN: HOME SCREEN (curated luxury hotel aesthetic)
// ==========================================
@Composable
fun HomeScreen(
    viewModel: TeaViewModel,
    isCn: Boolean,
    onNavigateToMenu: () -> Unit,
    onNavigateToLocator: () -> Unit
) {
    val userState by viewModel.user.collectAsStateWithLifecycle()
    val productsList by viewModel.products.collectAsStateWithLifecycle()
    val storesList by viewModel.stores.collectAsStateWithLifecycle()
    val articlesList by viewModel.articles.collectAsStateWithLifecycle()

    val featuredTeas = productsList.filter { it.isFeatured }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_column"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Hero Image / Brand Board
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Image(
                    painter = painterResource(id = getTeaDrawable("img_hero_banner")),
                    contentDescription = "Jade Leaf Luxury Tea House",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Overlaid elegant dark vignette
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                                startY = 100f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = if (isCn) "奢华茶道体验" else "LUXURY HOSPITALITY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = if (isCn) "Jade Leaf 茶馆" else "Jade Leaf Tea Lounge",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = if (isCn) "传承东方茶道美学，为您敬献极致茶文化服务" else "Traditional heritage meeting modern architectural dining",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
                    )
                }
            }
        }

        // 2. Premium Quick Services Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Order Ahead card
                Surface(
                    onClick = onNavigateToMenu,
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp)),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalCafe,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp)
                            )
                        }
                        Text(
                            text = Lang.translate("order_ahead", isCn),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // Table reservation card
                Surface(
                    onClick = onNavigateToLocator,
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp)),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MeetingRoom,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp)
                            )
                        }
                        Text(
                            text = Lang.translate("reserve_table", isCn),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // 3. Loyalty Banner Widget
        item {
            userState?.let { member ->
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = member.name,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                            )
                            Text(
                                text = "${Lang.translate("loyalty_tier", isCn)}: ${member.tier}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.tertiary),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Simple dynamic points progress bar
                            val progress = (member.points % 1000) / 1000f
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = MaterialTheme.colorScheme.tertiary,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${member.points} / ${((member.points / 1000) + 1) * 1000} ${Lang.translate("loyalty_points", isCn)}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f))
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Membership Card Scan",
                            tint = Color.White,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }
            }
        }

        // 4. Horizontal Seasonal tea curations
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = Lang.translate("seasonal_collection", isCn),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(featuredTeas) { tea ->
                        Surface(
                            modifier = Modifier
                                .width(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigateToMenu() },
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = getTeaDrawable(tea.imageResId)),
                                        contentDescription = if (isCn) tea.nameCn else tea.nameEn,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    Surface(
                                        color = MaterialTheme.colorScheme.tertiary,
                                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                                        modifier = Modifier.align(Alignment.TopStart)
                                    ) {
                                        Text(
                                            text = "¥${tea.price}",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = if (isCn) tea.nameCn else tea.nameEn,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = if (isCn) tea.originCn else tea.originEn,
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Featured Articles list (Tea academy snippets)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = Lang.translate("featured_articles", isCn),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                articlesList.take(2).forEach { article ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = getTeaDrawable(article.imageResId)),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isCn) article.titleCn else article.titleEn,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isCn) article.summaryCn else article.summaryEn,
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. SCREEN: STORE LOCATOR SCREEN
// ==========================================
@Composable
fun StoreLocatorScreen(
    viewModel: TeaViewModel,
    isCn: Boolean,
    onNavigateToBooking: () -> Unit
) {
    val stores by viewModel.stores.collectAsStateWithLifecycle()
    var searchStoreQuery by remember { mutableStateOf("") }

    val filteredStores = stores.filter {
        it.nameCn.contains(searchStoreQuery, ignoreCase = true) ||
        it.nameEn.contains(searchStoreQuery, ignoreCase = true) ||
        it.addressCn.contains(searchStoreQuery, ignoreCase = true) ||
        it.addressEn.contains(searchStoreQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("locator_screen_root")
    ) {
        // Simple search header
        OutlinedTextField(
            value = searchStoreQuery,
            onValueChange = { searchStoreQuery = it },
            placeholder = { Text(if (isCn) "输入城市或分店名称..." else "Search store name or city...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("store_search_input"),
            shape = RoundedCornerShape(24.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredStores) { store ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Image(
                                painter = painterResource(id = getTeaDrawable(store.imageUrl)),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = CircleShape
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.tertiary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isCn) store.hoursCn else store.hoursEn,
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                                        )
                                    }
                                }

                                // Heart Saved Location button
                                IconButton(
                                    onClick = { viewModel.toggleFavoriteStore(store.id, store.isFavorite) },
                                    modifier = Modifier.testTag("fav_button_${store.id}")
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color.Black.copy(alpha = 0.6f),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (store.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Favorite Store",
                                            tint = if (store.isFavorite) Color.Red else Color.White,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (isCn) store.nameCn else store.nameEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isCn) store.addressCn else store.addressEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = Lang.translate("facilities", isCn),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            // Flow row of store amenities
                            val amenities = (if (isCn) store.facilitiesCn else store.facilitiesEn).split(",")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                amenities.forEach { amenity ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    ) {
                                        Text(
                                            text = amenity.trim(),
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { /* Simulated Map intent launch */ },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Directions, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isCn) "获取路线" else "Directions")
                                }

                                Button(
                                    onClick = onNavigateToBooking,
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .testTag("book_button_${store.id}"),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(Icons.Default.Event, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(Lang.translate("reserve_table", isCn))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. SCREEN: DIGITAL MENU SCREEN
// ==========================================
@Composable
fun MenuScreen(viewModel: TeaViewModel, isCn: Boolean) {
    val productsList by viewModel.products.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val categories = listOf(
        "Premium Loose Leaf Teas",
        "Signature Drinks",
        "Food",
        "Retail"
    )

    // Filter logic
    val filteredProducts = productsList.filter {
        it.category == selectedCategory && (
            it.nameCn.contains(searchQuery, ignoreCase = true) ||
            it.nameEn.contains(searchQuery, ignoreCase = true) ||
            it.originCn.contains(searchQuery, ignoreCase = true) ||
            it.originEn.contains(searchQuery, ignoreCase = true)
        )
    }

    var selectedProductForDetail by remember { mutableStateOf<ProductEntity?>(null) }
    var productForCustomise by remember { mutableStateOf<ProductEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("menu_screen_root")
    ) {
        // 1. Curated Search header
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = { Text(Lang.translate("menu_search_placeholder", isCn)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("menu_search_input"),
            shape = RoundedCornerShape(24.dp)
        )

        // 2. Sliding premium tab bar for categories
        ScrollableTabRow(
            selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
            containerColor = Color.Transparent,
            edgePadding = 16.dp,
            divider = {}
        ) {
            categories.forEach { category ->
                val localizedName = when (category) {
                    "Premium Loose Leaf Teas" -> if (isCn) "特级散茶 (Loose Leaf)" else "Loose Leaf"
                    "Signature Drinks" -> if (isCn) "招牌现制 (Signatures)" else "Signatures"
                    "Food" -> if (isCn) "精致点心 (Food)" else "Food"
                    "Retail" -> if (isCn) "尊享零售 (Retail)" else "Retail"
                    else -> category
                }
                Tab(
                    selected = selectedCategory == category,
                    onClick = { viewModel.selectCategory(category) },
                    text = {
                        Text(
                            text = localizedName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Grid of products
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isCn) "雅院暂无相关产品..." else "No tea curations match your search.",
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredProducts) { item ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                            .clickable { selectedProductForDetail = item }
                            .testTag("product_card_${item.id}"),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = getTeaDrawable(item.imageResId)),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Surface(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                                    modifier = Modifier.align(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = "¥${item.price}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isCn) item.nameCn else item.nameEn,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isCn) item.originCn else item.originEn,
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Dynamic action: Add to Cart customized vs Retail instant-buy
                                Button(
                                    onClick = {
                                        if (item.category == "Food" || item.category == "Retail") {
                                            // Instant add for food & retail without customisation sheets
                                            viewModel.addToCart(
                                                item, 1,
                                                "标准", "常规", "去冰", "无奶"
                                            )
                                        } else {
                                            // Open customization panel for tea brews
                                            productForCustomise = item
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .testTag("add_cart_btn_${item.id}"),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (item.category == "Food" || item.category == "Retail") {
                                            if (isCn) "直接加购" else "Add"
                                        } else {
                                            Lang.translate("customise", isCn)
                                        },
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // A. PRODUCT COMPREHENSIVE SPECIFICATION DIALOG
    selectedProductForDetail?.let { tea ->
        TeaDetailDialog(
            tea = tea,
            isCn = isCn,
            onDismiss = { selectedProductForDetail = null }
        )
    }

    // B. DRINK CUSTOMISATION DIALOG SHEET
    productForCustomise?.let { tea ->
        CustomiseDrinkDialog(
            tea = tea,
            isCn = isCn,
            viewModel = viewModel,
            onDismiss = { productForCustomise = null }
        )
    }
}

// ==========================================
// 7. SCREEN: TEA ACADEMY (EDUCATIONAL CLASSROOM)
// ==========================================
@Composable
fun TeaAcademyScreen(viewModel: TeaViewModel, isCn: Boolean) {
    val articles by viewModel.articles.collectAsStateWithLifecycle()
    var activeTimerSeconds by remember { mutableStateOf(0) }
    var timerRunning by remember { mutableStateOf(false) }
    var isGreenTeaSelection by remember { mutableStateOf(true) } // Green vs Pu-erh
    var timerObject by remember { mutableStateOf<CountDownTimer?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("academy_screen_root"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Featured header image with rising steam aesthetic
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = getTeaDrawable("img_tea_academy")),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isCn) "Jade Leaf 茶道大讲堂" else "Jade Leaf Academy",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    )
                    Text(
                        text = if (isCn) "一器一茶，一温一理" else "Master the art of heat, duration, and brewing vessels",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
                    )
                }
            }
        }

        // 2. Interactive brewing guide tool (SENSORY PLAYGROUND)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isCn) "🍵 沏茶计时与水温指南" else "🍵 Interactive Brewing Dial",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isCn) "选择茶类获取冲泡水温纲领，启动沙漏进行计时制茶：" else "Select a tea style to configure the brewing timer:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                isGreenTeaSelection = true
                                activeTimerSeconds = 180 // 3 minutes
                                timerRunning = false
                                timerObject?.cancel()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isGreenTeaSelection) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (isCn) "龙井 (明前) - 80°C" else "Longjing - 80°C")
                        }

                        OutlinedButton(
                            onClick = {
                                isGreenTeaSelection = false
                                activeTimerSeconds = 240 // 4 minutes
                                timerRunning = false
                                timerObject?.cancel()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (!isGreenTeaSelection) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (isCn) "陈年普洱 - 95°C" else "Pu-erh - 95°C")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Timer Dial Graphic
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .align(Alignment.CenterHorizontally)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val minutes = activeTimerSeconds / 60
                            val seconds = activeTimerSeconds % 60
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = if (isGreenTeaSelection) "80°C" else "95°C",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Timer controls
                    Button(
                        onClick = {
                            if (timerRunning) {
                                timerObject?.cancel()
                                timerRunning = false
                            } else {
                                if (activeTimerSeconds == 0) {
                                    activeTimerSeconds = if (isGreenTeaSelection) 180 else 240
                                }
                                timerRunning = true
                                timerObject = object : CountDownTimer((activeTimerSeconds * 1000).toLong(), 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        activeTimerSeconds = (millisUntilFinished / 1000).toInt()
                                    }

                                    override fun onFinish() {
                                        timerRunning = false
                                        activeTimerSeconds = 0
                                    }
                                }.start()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("academy_timer_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (timerRunning) Color.Red else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = if (timerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (timerRunning) (if (isCn) "暂停计时" else "Pause Timer") else (if (isCn) "开始沏茶" else "Start Brewing"))
                    }
                }
            }
        }

        // 3. Main Articles Feed
        item {
            Text(
                text = if (isCn) "📖 茶道经典与美学典籍" else "📖 Tea Chronicles & Methods",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        items(articles) { article ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        Image(
                            painter = painterResource(id = getTeaDrawable(article.imageResId)),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(topStart = 8.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "${article.readTimeMinutes} mins read",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isCn) article.titleCn else article.titleEn,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isCn) article.contentCn else article.contentEn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. SCREEN: ACCOUNT & LOYALTY CARD
// ==========================================
@Composable
fun AccountScreen(viewModel: TeaViewModel, isCn: Boolean) {
    val userState by viewModel.user.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val reservations by viewModel.reservations.collectAsStateWithLifecycle()

    var showTopupDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("account_screen_root"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        userState?.let { member ->
            // 1. VIP DIGITAL MEMBERSHIP CARD (Luxury Gold/Black styling)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFFD4AF37).copy(alpha = 0.6f)),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1C1C1C),
                                        Color(0xFF2D5A47),
                                        Color(0xFF1C1C1C)
                                    )
                                )
                            )
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterVintage,
                                    contentDescription = null,
                                    tint = Color(0xFFD4AF37),
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    text = member.tier.split(" ").first(),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color(0xFFD4AF37),
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "CLUB MEMBERSHIP",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.5f),
                                    letterSpacing = 3.sp
                                )
                            )
                            Text(
                                text = member.name,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = Lang.translate("balance", isCn),
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.5f))
                                    )
                                    Text(
                                        text = "¥${String.format("%.2f", member.balance)}",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = Lang.translate("loyalty_points", isCn),
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.5f))
                                    )
                                    Text(
                                        text = "${member.points} PTS",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            color = Color(0xFFD4AF37),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Wallet Actions & Role Swap
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { showTopupDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("top_up_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Icon(Icons.Default.AddCard, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(Lang.translate("topup", isCn), color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.toggleRole() },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("portal_switch_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (member.role == "CUSTOMER") {
                                if (isCn) "切换后台" else "Admin Board"
                            } else {
                                Lang.translate("customer_portal", isCn)
                            }
                        )
                    }
                }
            }

            // 3. Order History Section
            item {
                Text(
                    text = Lang.translate("history_order", isCn),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (orders.isEmpty()) {
                item {
                    Text(
                        text = if (isCn) "尚无点单历史。快去大堂点杯好茶吧！" else "No order chronicles found yet.",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(orders) { order ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Order #${order.orderNumber}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = order.productNames,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(order.timestamp)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "¥${String.format("%.2f", order.totalPrice)}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Surface(
                                    shape = CircleShape,
                                    color = when (order.status) {
                                        "Received" -> MaterialTheme.colorScheme.primaryContainer
                                        "Preparing" -> MaterialTheme.colorScheme.tertiaryContainer
                                        "Ready" -> Color(0xFFD4AF37).copy(alpha = 0.2f)
                                        else -> MaterialTheme.colorScheme.primaryContainer
                                    }
                                ) {
                                    Text(
                                        text = when (order.status) {
                                            "Received" -> Lang.translate("order_received", isCn)
                                            "Preparing" -> Lang.translate("order_preparing", isCn)
                                            "Ready" -> Lang.translate("order_ready", isCn)
                                            "Delivered" -> Lang.translate("order_delivered", isCn)
                                            else -> order.status
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Private Table Reservations History Section
            item {
                Text(
                    text = Lang.translate("history_res", isCn),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            if (reservations.isEmpty()) {
                item {
                    // Let's integrate table reservation setup right inside this empty history screen!
                    InteractiveQuickBookingWidget(viewModel = viewModel, isCn = isCn)
                }
            } else {
                items(reservations) { booking ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = booking.storeName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                )
                                IconButton(
                                    onClick = { viewModel.cancelReservation(booking.id) },
                                    modifier = Modifier.testTag("cancel_booking_${booking.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Cancel Reservation",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${Lang.translate("date_label", isCn)}: ${booking.dateTime}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${Lang.translate("seating_label", isCn)}: ${booking.roomType} (${booking.seatingPreference})",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${Lang.translate("pax_label", isCn)}: ${booking.pax} Pax",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Append reservation creation too
                item {
                    InteractiveQuickBookingWidget(viewModel = viewModel, isCn = isCn)
                }
            }
        }
    }

    // Wallet Recharge popup Dialog
    if (showTopupDialog) {
        RechargeWalletDialog(
            isCn = isCn,
            onDismiss = { showTopupDialog = false },
            onConfirm = { amount ->
                viewModel.updateBalance(amount)
                showTopupDialog = false
            }
        )
    }
}

// ==========================================
// 9. SCREEN: ENTERPRISE ADMIN PORTAL
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPortalScreen(viewModel: TeaViewModel, isCn: Boolean) {
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val productsList by viewModel.products.collectAsStateWithLifecycle()

    var showAddProductDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_portal_root"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Portal Switch Back to Customer Mode
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Lang.translate("admin_portal", isCn),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isCn) "尊享店长及茶馆总管行政权力" else "Franchise Executive & Inventory Management",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Button(
                        onClick = { viewModel.toggleRole() },
                        modifier = Modifier.testTag("exit_admin_btn")
                    ) {
                        Text(Lang.translate("customer_portal", isCn))
                    }
                }
            }
        }

        // Analytical charts (Simple beautiful custom drawings / Canvas / Metrics cards)
        item {
            Text(
                text = Lang.translate("admin_analytics", isCn),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isCn) "连锁店总营业额" else "Total Revenue", style = MaterialTheme.typography.labelSmall)
                        val totalRev = orders.sumOf { it.totalPrice }
                        Text(
                            text = "¥${String.format("%.2f", totalRev)}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isCn) "连锁总点单数" else "Active Orders", style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = "${orders.size} Orders",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Order Management State machine actions
        item {
            Text(
                text = if (isCn) "☕ 待烹茶点列表与现沏指派" else "☕ Active Queue & Brewing Dispatch",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        if (orders.isEmpty()) {
            item {
                Text(
                    text = if (isCn) "连锁店内暂无待烹沏的点单。" else "Order queue is currently clear.",
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            items(orders) { order ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Order #${order.orderNumber}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = order.productNames,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Text(
                                text = "¥${order.totalPrice}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Queue Status:",
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            // Action state triggers
                            if (order.status == "Received") {
                                Button(
                                    onClick = { viewModel.adminUpdateOrderStatus(order.id, "Preparing") },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.testTag("admin_prep_${order.id}")
                                ) {
                                    Text(if (isCn) "开始手沏 (Brew)" else "Brew")
                                }
                            }
                            if (order.status == "Preparing") {
                                Button(
                                    onClick = { viewModel.adminUpdateOrderStatus(order.id, "Ready") },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                    modifier = Modifier.testTag("admin_ready_${order.id}")
                                ) {
                                    Text(if (isCn) "沏好待提 (Ready)" else "Ready", color = Color.Black)
                                }
                            }
                            if (order.status == "Ready") {
                                Button(
                                    onClick = { viewModel.adminUpdateOrderStatus(order.id, "Delivered") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                    modifier = Modifier.testTag("admin_deliver_${order.id}")
                                ) {
                                    Text(if (isCn) "核销享用 (Serve)" else "Serve", color = Color.White)
                                }
                            }

                            if (order.status == "Delivered") {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Served", tint = Color(0xFF2E7D32))
                                Text(if (isCn) "已送达/享用" else "Served", color = Color(0xFF2E7D32))
                            }
                        }
                    }
                }
            }
        }

        // Inventory Management block (CRUD products!)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isCn) "🏺 门店产品与零售大件管理" else "🏺 Product & Retail Catalog",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                )

                IconButton(
                    onClick = { showAddProductDialog = true },
                    modifier = Modifier.testTag("admin_add_product_btn")
                ) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                        Icon(Icons.Default.Add, contentDescription = "Add Product", tint = Color.White, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }

        items(productsList) { item ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = getTeaDrawable(item.imageResId)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isCn) item.nameCn else item.nameEn,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${item.category} • ¥${item.price}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    IconButton(
                        onClick = { viewModel.adminDeleteProduct(item.id) },
                        modifier = Modifier.testTag("delete_product_${item.id}")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }

    // Upper product listing form dialog
    if (showAddProductDialog) {
        AddProductDialog(
            isCn = isCn,
            onDismiss = { showAddProductDialog = false },
            onConfirm = { nameCn, nameEn, cat, price, origCn, origEn ->
                viewModel.adminAddProduct(nameCn, nameEn, cat, price, origCn, origEn)
                showAddProductDialog = false
            }
        )
    }
}

// ==========================================
// 10. REUSABLE COMPONENT: DETAILED DIALOGS
// ==========================================
@Composable
fun TeaDetailDialog(tea: ProductEntity, isCn: Boolean, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .testTag("tea_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = getTeaDrawable(tea.imageResId)),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(bottomStart = 12.dp),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "¥${tea.price}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isCn) tea.nameCn else tea.nameEn,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )

                Text(
                    text = tea.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // Detailed Specs
                val specList = listOf(
                    Lang.translate("origin", isCn) to (if (isCn) tea.originCn else tea.originEn),
                    Lang.translate("region", isCn) to (if (isCn) tea.regionCn else tea.regionEn),
                    Lang.translate("harvest", isCn) to (if (isCn) tea.harvestCn else tea.harvestEn),
                    Lang.translate("processing", isCn) to (if (isCn) tea.processingCn else tea.processingEn),
                    "Recommended Temperature" to tea.brewingTemp,
                    Lang.translate("tasting_notes", isCn) to (if (isCn) tea.tastingCn else tea.tastingEn),
                    Lang.translate("aroma_profile", isCn) to (if (isCn) tea.aromaCn else tea.aromaEn),
                    Lang.translate("caffeine_level", isCn) to (if (isCn) tea.caffeineCn else tea.caffeineEn)
                )

                specList.forEach { (title, desc) ->
                    if (desc.isNotEmpty() && desc != "不适用" && desc != "N/A") {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = Lang.translate("brewing_guide", isCn),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = if (isCn) tea.brewingGuideCn else tea.brewingGuideEn,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(if (isCn) "合上画册" else "Close Specification")
                }
            }
        }
    }
}

// DRINK CUSTOMIZATION SHEET DIALOG
@Composable
fun CustomiseDrinkDialog(
    tea: ProductEntity,
    isCn: Boolean,
    viewModel: TeaViewModel,
    onDismiss: () -> Unit
) {
    var strength by remember { mutableStateOf("标准 (Medium)") }
    var sugar by remember { mutableStateOf("半糖 (50% Sugar)") }
    var ice by remember { mutableStateOf("少冰 (Less Ice)") }
    var milk by remember { mutableStateOf("无奶 (No Milk)") }
    var qty by remember { mutableStateOf(1) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("customise_dialog")
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = Lang.translate("customise", isCn),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )
                Text(
                    text = if (isCn) tea.nameCn else tea.nameEn,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Customized strength selector
                Text(Lang.translate("tea_strength", isCn), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                val strengths = listOf("温和 (Light)", "标准 (Medium)", "浓郁 (Strong)")
                CustomSelectionRow(selected = strength, options = strengths, onSelected = { strength = it })

                Spacer(modifier = Modifier.height(12.dp))

                // Sugar
                Text(Lang.translate("sugar_level", isCn), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                val sugars = listOf("无糖 (0%)", "半糖 (50%)", "常规糖 (100%)")
                CustomSelectionRow(selected = sugar, options = sugars, onSelected = { sugar = it })

                Spacer(modifier = Modifier.height(12.dp))

                // Ice
                Text(Lang.translate("ice_level", isCn), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                val ices = listOf("去冰 (No Ice)", "少冰 (Less)", "常规冰 (Iced)")
                CustomSelectionRow(selected = ice, options = ices, onSelected = { ice = it })

                Spacer(modifier = Modifier.height(12.dp))

                // Alternative milk bases
                Text(Lang.translate("milk_option", isCn), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                val milks = listOf("无奶 (No Milk)", "全脂纯牛奶", "有机燕麦奶", "鲜椰奶")
                CustomSelectionRow(selected = milk, options = milks, onSelected = { milk = it })

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // Quantity selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (isCn) "沏制份数 (Quantity)" else "Quantity", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (qty > 1) qty-- }) {
                            Icon(Icons.Default.Remove, contentDescription = null)
                        }
                        Text(
                            text = qty.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        IconButton(onClick = { qty++ }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.addToCart(tea, qty, strength, sugar, ice, milk)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_to_cart_confirm"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${Lang.translate("add_to_cart", isCn)} - ¥${tea.price * qty}")
                }
            }
        }
    }
}

// Selector Row supporting responsive Material design
@Composable
fun CustomSelectionRow(selected: String, options: List<String>, onSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = selected == option || option.startsWith(selected.take(2))
            FilterChip(
                selected = isSelected,
                onClick = { onSelected(option) },
                label = { Text(option) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

// RESERVATION WIDGET FOR COMPACT DIRECTIVE ACCESSIBILITY
@Composable
fun InteractiveQuickBookingWidget(viewModel: TeaViewModel, isCn: Boolean) {
    var storeName by remember { mutableStateOf("西湖翠竹雅院 (Lakeside Sanctuary)") }
    var roomType by remember { mutableStateOf("榻榻米VIP私密茶室") }
    var dateSlot by remember { mutableStateOf("Tomorrow Afternoon 14:00") }
    var seatingPreference by remember { mutableStateOf("临湖观景窗边 (Window Seat)") }
    var pax by remember { mutableStateOf(4) }

    var bookSuccessAlert by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = Lang.translate("reserve_table", isCn),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Branch selector
            Text(if (isCn) "选择分店 (Branch)" else "Select Branch", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            val branches = listOf("西湖翠竹雅院 (Lakeside Sanctuary)", "北京紫禁别苑", "上海水榭茶阁")
            CustomSelectionRow(selected = storeName, options = branches, onSelected = { storeName = it })

            Spacer(modifier = Modifier.height(8.dp))

            // Room / Afternoon Tea Curations
            Text(if (isCn) "席位种类 (Experience)" else "Seat Experience", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            val rooms = listOf("榻榻米VIP私密茶室", "传统中式竹簟席", "皇家春季下午茶专座")
            CustomSelectionRow(selected = roomType, options = rooms, onSelected = { roomType = it })

            Spacer(modifier = Modifier.height(8.dp))

            // Seating preference
            Text(Lang.translate("seating_label", isCn), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            val seats = listOf("临湖观景窗边 (Window Seat)", "幽静翠竹深处", "大堂清幽古筝侧")
            CustomSelectionRow(selected = seatingPreference, options = seats, onSelected = { seatingPreference = it })

            Spacer(modifier = Modifier.height(8.dp))

            // Date / Time slots
            Text(Lang.translate("date_label", isCn), style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            val dates = listOf("Tomorrow Afternoon 14:00", "Weekend Morning 10:00", "Weekend Night 19:30")
            CustomSelectionRow(selected = dateSlot, options = dates, onSelected = { dateSlot = it })

            Spacer(modifier = Modifier.height(12.dp))

            // Pax count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(Lang.translate("pax_label", isCn), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (pax > 1) pax-- }) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                    }
                    Text(
                        text = "$pax Pax",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    IconButton(onClick = { if (pax < 12) pax++ }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.bookReservation(storeName, roomType, dateSlot, seatingPreference, pax)
                    bookSuccessAlert = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reserve_confirm_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Event, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(Lang.translate("reserve_btn", isCn))
            }
        }
    }

    if (bookSuccessAlert) {
        AlertDialog(
            onDismissRequest = { bookSuccessAlert = false },
            title = { Text(if (isCn) "席座预订成功！" else "Sanctuary Reserved!") },
            text = { Text(Lang.translate("book_success", isCn)) },
            confirmButton = {
                TextButton(onClick = { bookSuccessAlert = false }) {
                    Text(if (isCn) "尊悉" else "Acknowledge")
                }
            }
        )
    }
}

// CART DETAILS BOTTOM DIALOG SHEET
@Composable
fun CartDetailsDialog(
    cartItems: List<CartItem>,
    viewModel: TeaViewModel,
    isCn: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
                .testTag("cart_dialog_container")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Lang.translate("cart_title", isCn),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = getTeaDrawable(item.product.imageResId)),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isCn) item.product.nameCn else item.product.nameEn,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Custom: ${item.teaStrength} | ${item.sugarLevel}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "¥${item.product.price * item.quantity}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { viewModel.updateCartQuantity(item, -1) }) {
                                            Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                                        }
                                        Text(item.quantity.toString())
                                        IconButton(onClick = { viewModel.updateCartQuantity(item, 1) }) {
                                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                val subtotal = cartItems.sumOf { it.product.price * it.quantity }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(Lang.translate("total", isCn), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Text(
                        text = "¥${String.format("%.2f", subtotal)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (isCn) "继续选购" else "Add More")
                    }

                    Button(
                        onClick = {
                            viewModel.checkout("Order Ahead")
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("checkout_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(Lang.translate("checkout_btn", isCn))
                    }
                }
            }
        }
    }
}

// ADMIN ADD NEW MENU DIALOG FORM
@Composable
fun AddProductDialog(
    isCn: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (nameCn: String, nameEn: String, category: String, price: Double, originCn: String, originEn: String) -> Unit
) {
    var nameCn by remember { mutableStateOf("") }
    var nameEn by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Premium Loose Leaf Teas") }
    var priceStr by remember { mutableStateOf("") }
    var originCn by remember { mutableStateOf("") }
    var originEn by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("admin_add_product_dialog")
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = Lang.translate("admin_add_product", isCn),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nameCn,
                    onValueChange = { nameCn = it },
                    label = { Text("中文名称 (CN Name)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("英文名称 (EN Name)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Category Select:")
                val catList = listOf("Premium Loose Leaf Teas", "Signature Drinks", "Food", "Retail")
                CustomSelectionRow(selected = category, options = catList, onSelected = { category = it })

                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("价格 (Price in CNY)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = originCn,
                    onValueChange = { originCn = it },
                    label = { Text("中文产地 (CN Origin)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = originEn,
                    onValueChange = { originEn = it },
                    label = { Text("英文产地 (EN Origin)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val p = priceStr.toDoubleOrNull() ?: 0.0
                            onConfirm(nameCn, nameEn, category, p, originCn, originEn)
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("admin_add_confirm"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Save & List")
                    }
                }
            }
        }
    }
}

// WALLET RECHARGE DIALOG
@Composable
fun RechargeWalletDialog(
    isCn: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var rechargeAmount by remember { mutableStateOf("200") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = Lang.translate("topup", isCn),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(12.dp))

                val opts = listOf("100", "200", "500", "1000")
                CustomSelectionRow(selected = rechargeAmount, options = opts, onSelected = { rechargeAmount = it })

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val amt = rechargeAmount.toDoubleOrNull() ?: 0.0
                            onConfirm(amt)
                        },
                        modifier = Modifier.weight(1.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(if (isCn) "确认充值" else "Confirm Top Up")
                    }
                }
            }
        }
    }
}

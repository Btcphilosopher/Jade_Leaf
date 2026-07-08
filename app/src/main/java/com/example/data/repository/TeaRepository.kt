package com.example.data.repository

import com.example.data.database.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class TeaRepository(private val teaDao: TeaDao) {

    // User Session Flows
    val userFlow: Flow<UserEntity?> = teaDao.getUserFlow()

    suspend fun saveUser(user: UserEntity) {
        teaDao.insertUser(user)
    }

    suspend fun updateUserLanguage(lang: String) {
        val current = teaDao.getUserFlow().first() ?: UserEntity()
        teaDao.insertUser(current.copy(selectedLanguage = lang))
    }

    suspend fun updateUserRole(role: String) {
        val current = teaDao.getUserFlow().first() ?: UserEntity()
        teaDao.insertUser(current.copy(role = role))
    }

    suspend fun addLoyaltyPoints(p: Int) {
        val current = teaDao.getUserFlow().first() ?: UserEntity()
        val newPoints = current.points + p
        val newTier = when {
            newPoints >= 5000 -> "玉叶大师 (Jade)"
            newPoints >= 2500 -> "铂金会员 (Platinum)"
            newPoints >= 1000 -> "黄金会员 (Gold)"
            else -> "白银会员 (Silver)"
        }
        teaDao.insertUser(current.copy(points = newPoints, tier = newTier))
    }

    suspend fun updateBalance(amount: Double) {
        val current = teaDao.getUserFlow().first() ?: UserEntity()
        teaDao.insertUser(current.copy(balance = current.balance + amount))
    }

    // Product Menu Flows
    val allProducts: Flow<List<ProductEntity>> = teaDao.getAllProductsFlow()

    suspend fun saveProduct(product: ProductEntity) {
        teaDao.insertProduct(product)
    }

    suspend fun deleteProduct(productId: Int) {
        teaDao.deleteProductById(productId)
    }

    // Store Locator Flows
    val allStores: Flow<List<StoreEntity>> = teaDao.getAllStoresFlow()

    suspend fun toggleStoreFavorite(storeId: Int, isFav: Boolean) {
        teaDao.updateStoreFavorite(storeId, isFav)
    }

    // Order Tracking Flows
    val allOrders: Flow<List<OrderEntity>> = teaDao.getAllOrdersFlow()

    suspend fun createOrder(order: OrderEntity): Long {
        return teaDao.insertOrder(order)
    }

    suspend fun updateOrderStatus(orderId: Int, status: String) {
        teaDao.updateOrderStatus(orderId, status)
    }

    // Reservations Flows
    val allReservations: Flow<List<ReservationEntity>> = teaDao.getAllReservationsFlow()

    suspend fun bookReservation(reservation: ReservationEntity) {
        teaDao.insertReservation(reservation)
    }

    suspend fun cancelReservation(reservationId: Int) {
        teaDao.deleteReservationById(reservationId)
    }

    // Tea Academy / Articles
    val allArticles: Flow<List<ArticleEntity>> = teaDao.getAllArticlesFlow()

    // Database Prepopulation logic
    suspend fun prepopulateDatabaseIfEmpty() {
        val productsExist = teaDao.getAllProductsFlow().first()
        if (productsExist.isNotEmpty()) return

        // 1. Insert Default User Session
        teaDao.insertUser(UserEntity())

        // 2. Insert Premium Tea & Food Menu Products
        val defaultProducts = listOf(
            ProductEntity(
                id = 101,
                nameCn = "西湖龙井 (特级)",
                nameEn = "West Lake Longjing (Imperial)",
                category = "Premium Loose Leaf Teas",
                price = 128.00,
                originCn = "中国浙江杭州西湖区",
                originEn = "West Lake, Hangzhou, Zhejiang, China",
                regionCn = "狮峰山核心产区",
                regionEn = "Shi Feng Mountain Core Region",
                harvestCn = "清明前手采明前茶",
                harvestEn = "Pre-Qingming hand-harvested",
                processingCn = "传统手工铁锅炒制，十大手法杀青",
                processingEn = "Traditional hand-fired flat roasting in iron woks",
                brewingTemp = "80°C - 85°C",
                brewingGuideCn = "宜用高形无盖玻璃杯，采用中投法冲泡，静置3分钟。",
                brewingGuideEn = "Best brewed in an open glass cup using the middle-drop method, steep for 3 mins.",
                tastingCn = "鲜爽甘醇，伴有浓郁炒豆瓣香与天然兰花甜香。",
                tastingEn = "Vibrant, fresh and nutty with orchid-like sweetness and chestnut finish.",
                aromaCn = "清高持久，板栗香与兰花香交织。",
                aromaEn = "Persistent roasted chestnut paired with delicate mountain orchid.",
                caffeineCn = "中等",
                caffeineEn = "Moderate",
                imageResId = "img_tea_academy", // fallback to tea academy illustration
                isFeatured = true
            ),
            ProductEntity(
                id = 102,
                nameCn = "福鼎白毫银针",
                nameEn = "Fuding White Silver Needle",
                category = "Premium Loose Leaf Teas",
                price = 148.00,
                originCn = "中国福建福鼎太姥山",
                originEn = "Taimu Mountain, Fuding, Fujian, China",
                regionCn = "海拔800米高山茶园",
                regionEn = "High altitude 800m cloud gardens",
                harvestCn = "春季纯单芽手采",
                harvestEn = "Spring premium single buds only",
                processingCn = "仅日光自然萎凋，轻微干燥，无揉捻",
                processingEn = "Sun-withered, gently slow-dried, strictly un-rolled",
                brewingTemp = "85°C - 90°C",
                brewingGuideCn = "使用白瓷盖碗，温润泡15秒后开盖冲泡，每泡延长10秒。",
                brewingGuideEn = "Use a white porcelain Gaiwan, rinse quickly, then steep starting at 15s with open lid.",
                tastingCn = "蜜盏甘甜，有天然毫香与清甜黄瓜鲜爽感。",
                tastingEn = "Delicately sweet, honeylike, loaded with white pekoes and crisp cucumber freshness.",
                aromaCn = "清雅幽甜，竹叶清香。",
                aromaEn = "Subtle sweet hay with fresh bamboo leaf notes.",
                caffeineCn = "低",
                caffeineEn = "Low",
                imageResId = "img_tea_academy",
                isFeatured = false
            ),
            ProductEntity(
                id = 103,
                nameCn = "安溪铁观音 (清香型)",
                nameEn = "Anxi Tieguanyin (Anjou)",
                category = "Premium Loose Leaf Teas",
                price = 118.00,
                originCn = "中国福建安溪西坪镇",
                originEn = "Xiping Town, Anxi, Fujian, China",
                regionCn = "云雾缭绕高山铁观音核心地带",
                regionEn = "Misty mountains of Anxi",
                harvestCn = "秋分后手采开面三叶",
                harvestEn = "Autumn harvest hand-picked multi-leaf buds",
                processingCn = "摇青做青，反复包揉，轻度烘焙",
                processingEn = "Traditional tossing, semi-oxidation, cloth-rolling, and light firing",
                brewingTemp = "95°C - 100°C",
                brewingGuideCn = "沸水紫砂壶冲泡，首泡5秒出汤，极耐冲泡。",
                brewingGuideEn = "Brew with boiling water in a clay teapot, first steep 5s, highly re-steepable.",
                tastingCn = "音韵显露，清甜饱满，满口生津兰花蜜意。",
                tastingEn = "Classic 'Guanyin Rh韵' with thick floral broth, saliva-inducing orchid sweetness.",
                aromaCn = "高雅兰花香，浓郁持久。",
                aromaEn = "Captivating and highly aromatic fresh orchids.",
                caffeineCn = "高",
                caffeineEn = "High",
                imageResId = "img_tea_academy",
                isFeatured = true
            ),
            ProductEntity(
                id = 104,
                nameCn = "勐海陈年熟普洱 (2012)",
                nameEn = "Aged Menghai Ripe Pu-erh (2012)",
                category = "Premium Loose Leaf Teas",
                price = 168.00,
                originCn = "中国云南西双版纳勐海",
                originEn = "Menghai, Xishuangbanna, Yunnan, China",
                regionCn = "古茶山大叶种百年乔木茶园",
                regionEn = "Old-growth arbor trees in classic tea hills",
                harvestCn = "春季一芽二叶晒青毛茶",
                harvestEn = "Spring hand-plucked sun-dried large leaf",
                processingCn = "传统渥堆发酵，自然干仓存放14年",
                processingEn = "Traditional microbial wet-piling fermentation, dry-aged for 14 years",
                brewingTemp = "95°C - 100°C",
                brewingGuideCn = "高温沸水烫壶洗茶两遍，第3泡起10秒出汤，茶汤红浓明亮。",
                brewingGuideEn = "Use boiling water, rinse twice, steep starting from 10s. Liquor is bright ruby red.",
                tastingCn = "汤香醇厚，如丝绸般顺滑，伴随淡淡陈木与甘草甜。",
                tastingEn = "Rich, silky and full-bodied wood liquor with camphor, dark chocolate, and sweet molasses.",
                aromaCn = "深沉陈香，樟木与枣香。",
                aromaEn = "Earthy warehouse, cedarwood and sweet red dates.",
                caffeineCn = "高",
                caffeineEn = "High",
                imageResId = "img_tea_academy",
                isFeatured = false
            ),
            ProductEntity(
                id = 105,
                nameCn = "碧螺春玉露拿铁",
                nameEn = "Biluochun Jade Dew Latte",
                category = "Signature Drinks",
                price = 48.00,
                originCn = "江苏苏州洞庭山碧螺春茶底",
                originEn = "Dongting Biluochun green tea base",
                regionCn = "茶果间作示范园区",
                regionEn = "Inter-cropped orchards of Suzhou",
                harvestCn = "春季嫩芽手采",
                harvestEn = "Spring baby buds",
                processingCn = "冷萃绿茶拼配有机燕麦奶",
                processingEn = "Cold-brewed green tea concentrated, blended with organic oat milk",
                brewingTemp = "0°C - 5°C",
                brewingGuideCn = "冰镇冷饮。完美定制可选择少冰无糖，展现甘甜本色。",
                brewingGuideEn = "Served iced. Best enjoyed with less ice and unsweetened to appreciate the tea.",
                tastingCn = "清新果香与绿茶清爽完美融入丝滑燕麦奶中。",
                tastingEn = "Fruity and floral green tea notes dancing harmoniously with premium velvety oat milk.",
                aromaCn = "绿茶清香与坚果奶香交融。",
                aromaEn = "Toasty oat milk overlaying high-mountain sweet grassy green tea.",
                caffeineCn = "中等",
                caffeineEn = "Moderate",
                imageResId = "img_app_icon",
                isFeatured = true
            ),
            ProductEntity(
                id = 106,
                nameCn = "黄金桂雨露果茶",
                nameEn = "Golden Osmanthus Fruit Medley",
                category = "Signature Drinks",
                price = 45.00,
                originCn = "福建黄金桂乌龙茶底",
                originEn = "Fujian Huangjingui Oolong base",
                regionCn = "天然优质桂花庄园",
                regionEn = "Organic osmanthus grove",
                harvestCn = "金秋十月新鲜桂花",
                harvestEn = "Golden autumn fresh osmanthus blooms",
                processingCn = "高山乌龙茶熏制，配以鲜榨青红果肉",
                processingEn = "In-house scented oolong tea shaken with fresh grapefruits and mango nectar",
                brewingTemp = "0°C - 10°C",
                brewingGuideCn = "少冰冲调，配合一勺Q弹晶球，口感丰盈。",
                brewingGuideEn = "Shaken iced with standard toppings and a scoop of agar boba for textual delight.",
                tastingCn = "桂花馥郁，果肉爆汁，乌龙底韵清香宜人。",
                tastingEn = "Intense, beautiful osmanthus aroma leading into juicy citrus and clean refreshing oolong.",
                aromaCn = "浓烈桂花香，热带百香果与香橙。",
                aromaEn = "Rich floral osmanthus, tropical passionfruit, and sweet oranges.",
                caffeineCn = "低",
                caffeineEn = "Low",
                imageResId = "img_app_icon",
                isFeatured = false
            ),
            ProductEntity(
                id = 107,
                nameCn = "精致翠玉点心拼盘",
                nameEn = "Jade Imperial Dim Sum Platter",
                category = "Food",
                price = 98.00,
                originCn = "翠竹大堂主厨手作",
                originEn = "Handcrafted by Jade Lounge executive chef",
                regionCn = "新鲜农场应季食材料理",
                regionEn = "Locally sourced seasonal farm ingredients",
                harvestCn = "每日新鲜蒸制",
                harvestEn = "Steamed fresh daily to order",
                processingCn = "现做即蒸，配以特制桂花秘酱",
                processingEn = "Made by hand and steamed, served with organic local honey honey sauce",
                brewingTemp = "75°C",
                brewingGuideCn = "热食。推荐搭配西湖龙井茶，化解油腻，衬托鲜甜。",
                brewingGuideEn = "Serve piping hot. Pairs exceptionally well with Longjing green tea to elevate sweetness.",
                tastingCn = "虾饺Q弹鲜美， osmanthus cake 软糯清甜，烧麦肉香四溢。",
                tastingEn = "Savory crystal shrimp dumplings, soft gelatinous osmanthus floral cake, and rich pork shumai.",
                aromaCn = "面香，香菇与鲜虾的咸香，夹杂竹木香气。",
                aromaEn = "Warm wheat dough, savory shiitake, and bamboo steamer steam.",
                caffeineCn = "无",
                caffeineEn = "None",
                imageResId = "img_hero_banner",
                isFeatured = true
            ),
            ProductEntity(
                id = 108,
                nameCn = "宇治抹茶熔岩蛋糕",
                nameEn = "Uji Matcha Lava Cake",
                category = "Food",
                price = 38.00,
                originCn = "手工法式烘焙坊",
                originEn = "Artisan in-house french bakery",
                regionCn = "进口有机宇治抹茶粉",
                regionEn = "Imported organic ceremonial grade matcha",
                harvestCn = "每日手工限量烘焙",
                harvestEn = "Baked fresh daily in limited quantities",
                processingCn = "精细控温烘焙，确保切开流心顺滑",
                processingEn = "Baked at high heat to achieve chocolate sponge exterior with a flowing rich green center",
                brewingTemp = "60°C",
                brewingGuideCn = "温食。流心融化状态最佳。配以一碗香浓大红袍可中和甜度。",
                brewingGuideEn = "Serve warm for maximum flowing lava effect. Excellent with strong roasted Rock Oolong.",
                tastingCn = "抹茶醇厚微苦，与微甜的蛋糕坯和软糯红豆泥交织，甜而不腻。",
                tastingEn = "Decadent and mildly bitter matcha cream balanced by sponge sweetness and a side of red beans.",
                aromaCn = "浓烈抹茶香与高品质黄油烘烤香。",
                aromaEn = "Rich ceremonial matcha and oven-baked butter biscuits.",
                caffeineCn = "无",
                caffeineEn = "None",
                imageResId = "img_hero_banner",
                isFeatured = false
            ),
            ProductEntity(
                id = 109,
                nameCn = "御贡龙井散茶礼盒 (250g)",
                nameEn = "Tribute Longjing Loose Leaf Box (250g)",
                category = "Retail",
                price = 588.00,
                originCn = "杭州狮峰山龙井村",
                originEn = "Longjing Village, Shi Feng Mountain, China",
                regionCn = "国家地理标志原产核心保护区",
                regionEn = "Protected origin geographic region",
                harvestCn = "春分前头采限量金针芽",
                harvestEn = "Early spring initial flush gold buds",
                processingCn = "纯手工制茶大师特制，密封马口铁罐及高级金箔竹盒包装",
                processingEn = "Individually hand-roasted by certified Tea Masters, packed in gold foil tins and bamboo casket",
                brewingTemp = "80°C",
                brewingGuideCn = "内附精美产品证书，极富收藏价值。送礼或自备极品之选。",
                brewingGuideEn = "Includes product authenticity certificate. Perfect for luxury gifting or fine tea enthusiasts.",
                tastingCn = "汤色嫩绿明亮，口感柔滑醇和，兰花余韵半日不绝。",
                tastingEn = "Exquisite buttery smooth mouthfeel with unmatched orchid honey lingering in throat for hours.",
                aromaCn = "极幽雅的清甜兰花香，无丝毫青涩。",
                aromaEn = "Stunningly refined alpine orchid and warm roasted chestnut sweetness.",
                caffeineCn = "中等",
                caffeineEn = "Moderate",
                imageResId = "img_app_icon",
                isFeatured = true
            ),
            ProductEntity(
                id = 110,
                nameCn = "汉白玉雕纹紫砂茶具套组",
                nameEn = "Alabaster Engraved Clay Tea Set",
                category = "Retail",
                price = 888.00,
                originCn = "中国陶都江苏宜兴",
                originEn = "Yixing Ceramic Capital, Jiangsu, China",
                regionCn = "大师手工定制窑口",
                regionEn = "Artisanal family kilns of Yixing",
                harvestCn = "2025手作收藏款",
                harvestEn = "2025 hand-built collection",
                processingCn = "精选原矿朱泥制胚，手工微雕汉玉纹路，含一壶四杯",
                processingEn = "Built with premium mineral red Zhuni clay, hand carved with traditional dynasty textures. Set of 1 teapot, 4 cups",
                brewingTemp = "不适用",
                brewingGuideCn = "紫砂壶吸附茶香，适合泡乌龙、黑茶或普洱。一壶不侍二茶。",
                brewingGuideEn = "Porous clay seasons over time. Ideal for Oolongs and Pu-erhs. Dedicated to one tea family only.",
                tastingCn = "不适用",
                tastingEn = "N/A",
                aromaCn = "陶泥天然古朴质感",
                aromaEn = "Warm natural primitive earthenware aesthetic",
                caffeineCn = "无",
                caffeineEn = "None",
                imageResId = "img_app_icon",
                isFeatured = false
            )
        )
        teaDao.insertProducts(defaultProducts)

        // 3. Insert Store Locations
        val defaultStores = listOf(
            StoreEntity(
                id = 1,
                nameCn = "西湖翠竹雅院 (Lakeside Sanctuary)",
                nameEn = "West Lake Lakeside Sanctuary",
                addressCn = "杭州市西湖区孤山路12号 (紧邻平湖秋月)",
                addressEn = "No. 12 Gushan Road, West Lake District, Hangzhou (Near Autumn Moon over the Calm Lake)",
                hoursCn = "09:00 - 22:00",
                hoursEn = "09:00 AM - 10:00 PM",
                facilitiesCn = "临湖露台茶座, 榻榻米VIP茶室, 每日茶道表演, 100%全景玻璃窗, 免费极速无线上网",
                facilitiesEn = "Lakeside Deck, Tatami VIP Rooms, Daily Tea Ceremony, 100% Panoramic Windows, High-speed Wi-Fi",
                imageUrl = "img_hero_banner",
                isFavorite = true,
                latitude = 30.2529,
                longitude = 120.1415
            ),
            StoreEntity(
                id = 2,
                nameCn = "北京紫禁琉璃别苑 (Forbidden Court)",
                nameEn = "Beijing Forbidden Court",
                addressCn = "北京市东城区南池子大街45号 (距东华门200米)",
                addressEn = "No. 45 Nanchizi Street, Dongcheng District, Beijing (200m from Donghuamen Gate)",
                hoursCn = "10:00 - 23:00",
                hoursEn = "10:00 AM - 11:00 PM",
                facilitiesCn = "四合院中庭景观, 古筝现场演奏, 汉服茶会体验, 精美宫廷茶点, 私密商务隔间",
                facilitiesEn = "Traditional Courtyard, Live Guzheng Performances, Hanfu Tea Parties, Imperial Pastries, Business Suites",
                imageUrl = "img_tea_academy",
                isFavorite = false,
                latitude = 39.9123,
                longitude = 116.4024
            ),
            StoreEntity(
                id = 3,
                nameCn = "上海翠叶水榭 (Shanghai Bamboo Lounge)",
                nameEn = "Shanghai Bamboo Lounge",
                addressCn = "上海市静安区南京西路1018号 (静安嘉里中心北区)",
                addressEn = "No. 1018 Nanjing West Road, Jing'an District, Shanghai (Jing'an Kerry Center North Wing)",
                hoursCn = "08:30 - 22:30",
                hoursEn = "08:30 AM - 10:30 PM",
                facilitiesCn = "都市室内温室竹林, 瀑布水幕屏风, 极速外带吧台, 会议投屏投影仪, 零售陈列大厅",
                facilitiesEn = "Indoor Bamboo Greenhouse, Waterfall Water Curtain, Fast Takeaway Counter, Conference Projectors, Retail Gallery",
                imageUrl = "img_hero_banner",
                isFavorite = false,
                latitude = 31.2265,
                longitude = 121.4485
            )
        )
        teaDao.insertStores(defaultStores)

        // 4. Insert Default Academy Articles
        val defaultArticles = listOf(
            ArticleEntity(
                id = 501,
                titleCn = "静心茶道：从温杯到一饮尽江山",
                titleEn = "The Way of Tea: From Warming the Cup to Tranquility",
                summaryCn = "探索中国茶道仪式（功夫茶）的基本步骤与精神内核，寻找都市喧嚣中的一抹宁静。",
                summaryEn = "Unlock the steps and meditative spirit of Gongfu Tea ceremony, finding zen in modern urban living.",
                contentCn = "茶道，是一场关于温度、时间和呼吸的修行。第一步，温杯：用沸水温润白瓷盖碗与小品茗杯，激发出泥土与窑烧的温存。第二步，投茶：观茶干之形，闻干茶之香，将干茶缓缓滑入温暖的茶具。第三步，温润泡：首泡注水，5秒内即倾泻而出，不作饮用，只为唤醒沉睡的叶片（即‘洗茶’）。第四步，正泡：根据茶类掌控水温，高冲低斟，静止数秒，将金黄的汤水匀入公道杯，再分斟至品茗杯。品饮时，分三口啜饮：一闻其香，二品其汤，三回其甘。在这一注一倒之间，人的思绪也随之沉淀，回归太古清幽。",
                contentEn = "The tea ceremony is a practice of temperature, timing, and breath. First, warming the cups (温杯): cascade hot water into the Gaiwan and tasting cups to warm the clay. Second, placing the leaves (投茶): inspect the dried leaves and inhale their clean dry fragrance as they slide into the warm chamber. Third, awakening (温润泡): pour boiling water over the leaves and pour out instantly within 5 seconds; this is not for drinking, but to revive the sleeping leaves. Fourth, brewing (正泡): carefully monitor the heat, pouring water from high above and decanting it close to the pitcher, smoothing out bubbles. Finally, savor the golden liquor in three progressive sips: first to inhale the aroma, second to taste the full mouthfeel, and third to appreciate the lingering sweet echo in the throat. In this pouring rhythm, modern anxiety dissolves into ancient tranquility.",
                category = "Ceremony",
                imageResId = "img_tea_academy",
                readTimeMinutes = 5
            ),
            ArticleEntity(
                id = 502,
                titleCn = "金汤奥秘：绿茶、白茶与普洱的完美冲泡温度",
                titleEn = "Liquid Gold: Perfect Temperatures for Green, White & Pu-erh",
                summaryCn = "水温是茶的灵魂导师。用温度锁住龙井的鲜、白茶的蜜以及熟普洱的糯。",
                summaryEn = "Water temperature is the tea's spiritual guide. Discover how to balance vibrant green, sweet white, and rich aged teas.",
                contentCn = "许多人认为，茶必须要用滚烫的开水冲泡，这其实是一个常见的误区。不同发酵程度与叶片娇嫩程度的茶，对水温的要求差之千里。\n\n1. 绿茶 (如西湖龙井)：水温宜在 80°C - 85°C。绿茶属于不发酵茶，富含鲜嫩的氨基酸与不耐热的维生素。沸水会直接将叶片烫熟，产生焦苦的‘熟汤气’，使其丧失鲜爽感。冲泡时可让开水在公道杯中静置2分钟后再注水。\n\n2. 白茶 (如白毫银针)：水温宜在 85°C - 90°C。白茶萎凋时间长，茸毛丰盈。过高水温会烫坏叶表茸毛，导致茶汤浑浊；过低水温又无法浸出毫香。温润冲泡是绽放其甜瓜香的关键。\n\n3. 青茶/乌龙茶 (如铁观音)：水温宜在 95°C - 100°C。乌龙茶属于半发酵，叶片肥厚坚韧，需要极高温度的沸水冲击，方能激发出内含的高沸点芳香物质。宜使用保温性极佳的朱泥紫砂壶冲泡。\n\n4. 黑茶/普洱熟茶：必须使用 100°C 的沸水。并且需要至少洗茶一遍。高水温能瞬间逼出熟茶的陈香与糖分，使茶汤甘甜软糯，如糯米般黏密。",
                contentEn = "Many assume tea should always be brewed with boiling water, which is a major pitfall. Teas of different oxidation and leaf tenderness require vastly different water temperatures:\n\n1. Green Tea (e.g. Longjing): 80°C - 85°C. Unoxidized green tea is rich in amino acids and heat-sensitive vitamins. Boiling water literally scalds the delicate leaves, causing bitter 'cooked vegetable' tastes. Cool the water in a pitcher for 2 minutes before pouring.\n\n2. White Tea (e.g. Silver Needle): 85°C - 90°C. White tea leaves are covered in delicate fine downy hairs. Too hot, and you scorch these hairs, muddying the brew; too cool, and you fail to extract the hay sweetness.\n\n3. Oolong Tea (e.g. Tieguanyin): 95°C - 100°C. Oolong leaves are thick and tightly rolled. They require aggressive bubbling heat to uncoil the leaf structure and extract the high-temperature floral orchid oils.\n\n4. Aged Pu-erh and Black Teas: 100°C Boiling Water. Require a solid boiling flush. High temperatures unlock the wood, camphor, and brown sugar molecules, creating a broth that is incredibly rich, sweet, and velvety.",
                category = "Academy",
                imageResId = "img_tea_academy",
                readTimeMinutes = 7
            ),
            ArticleEntity(
                id = 503,
                titleCn = "高山云雾：探寻福建安溪铁观音的神秘发源地",
                titleEn = "Mist and Mountains: Finding the Origins of Tieguanyin",
                summaryCn = "走进云遮雾绕的安溪西坪古村，听一听神树铁观音如何在红土地与山林泉水间孕育音韵。",
                summaryEn = "Journey into the misty valleys of Xiping Town, Fujian, exploring how the legendary tree grows in red clay and spring water.",
                contentCn = "福建安溪，是中国乌龙茶的摇篮。这里常年被云雾和松涛包围，红色的微酸性土壤中富含铁质与矿物。铁观音名字的由来有一个美丽的传说：乾隆年间，安溪一位虔诚的茶农每日都会为观音大士供茶。一晚，他梦见观音指引他在山中石缝间寻得一株散发奇香的茶树。他醒后依梦寻找，果真在悬崖边得一新茶树，叶片重如铁，茶汤香如观音，故名‘铁观音’。如今，安溪的核心茶园推行‘茶林共生’的有机种植法，茶树在竹林、野花和清泉之间生长，其吸收了山野的草木精华，这便是‘观音韵’的奥秘所在。每一杯铁观音，都凝聚了福建高山厚重的雾气与纯洁的山泉。",
                contentEn = "Anxi, Fujian, is the cradle of Chinese Oolong. This geographic region is surrounded year-round by rolling white clouds and wind. The local red soil is uniquely rich in iron and vital minerals. The name 'Tieguanyin' (Iron Goddess of Mercy) comes from a classic folklore: in the Qing Dynasty, a pious tea farmer made fresh tea offerings to Guanyin daily. One night, the goddess appeared in his dreams, pointing to a secret crevice in the rocks where a miraculous glowing plant grew. The next morning, he found the tea sapling at the foot of a cliff. The leaves were heavy as iron, and the brewed cup was sweet and calming as the goddess herself. Today, core fields promote 'Forest-Tea Symbiosis', planting tea alongside wild bamboo groves, wildflowers, and natural mineral springs, absorbing mountain energy. That is the biological secret of its famous orchid finish.",
                category = "Culture",
                imageResId = "img_tea_academy",
                readTimeMinutes = 6
            )
        )
        teaDao.insertArticles(defaultArticles)
    }
}

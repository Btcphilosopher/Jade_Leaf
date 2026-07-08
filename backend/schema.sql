-- ====================================================================
-- Jade Leaf 茶馆 - Enterprise PostgreSQL Database Schema
-- ====================================================================

-- 1. Users & Credentials
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) DEFAULT 'CUSTOMER', -- CUSTOMER, STAFF_ADMIN, SYSTEM_SUPER
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Store Locations
CREATE TABLE stores (
    id SERIAL PRIMARY KEY,
    name_cn VARCHAR(100) NOT NULL,
    name_en VARCHAR(100) NOT NULL,
    address_cn VARCHAR(255) NOT NULL,
    address_en VARCHAR(255) NOT NULL,
    latitude DECIMAL(9, 6) NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL,
    phone_number VARCHAR(30),
    hours_cn VARCHAR(100),
    hours_en VARCHAR(100),
    facilities_cn TEXT, -- Comma-separated or JSON list
    facilities_en TEXT,
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tea Collections & Categories
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    slug VARCHAR(50) UNIQUE NOT NULL,
    name_cn VARCHAR(50) NOT NULL,
    name_en VARCHAR(50) NOT NULL
);

-- 4. Products (Tea Leaves, Signature Drinks, Food, Retail Accessories)
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    category_id INT REFERENCES categories(id),
    name_cn VARCHAR(150) NOT NULL,
    name_en VARCHAR(150) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    is_featured BOOLEAN DEFAULT FALSE,
    image_url VARCHAR(255),
    
    -- Specific Tea Specifications (Metadata)
    origin_cn VARCHAR(150),
    origin_en VARCHAR(150),
    region_cn VARCHAR(150),
    region_en VARCHAR(150),
    harvest_cn VARCHAR(100),
    harvest_en VARCHAR(100),
    processing_cn TEXT,
    processing_en TEXT,
    brewing_temp VARCHAR(30),
    brewing_guide_cn TEXT,
    brewing_guide_en TEXT,
    tasting_cn TEXT,
    tasting_en TEXT,
    aroma_cn TEXT,
    aroma_en TEXT,
    caffeine_cn VARCHAR(50),
    caffeine_en VARCHAR(50),
    
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Inventory Management
CREATE TABLE inventory (
    id SERIAL PRIMARY KEY,
    store_id INT REFERENCES stores(id) ON DELETE CASCADE,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    stock_quantity INT DEFAULT 0,
    min_alert_quantity INT DEFAULT 10,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. Memberships & Loyalty Accounts
CREATE TABLE memberships (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    membership_number VARCHAR(30) UNIQUE NOT NULL,
    points INT DEFAULT 0,
    tier VARCHAR(20) DEFAULT 'Silver', -- Silver, Gold, Platinum, Jade Master
    card_balance DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 7. Loyalty Points Transactions Log
CREATE TABLE loyalty_transactions (
    id SERIAL PRIMARY KEY,
    membership_id INT REFERENCES memberships(id) ON DELETE CASCADE,
    points_changed INT NOT NULL,
    description_cn VARCHAR(255),
    description_en VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 8. Table & Private Tea Room Reservations
CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    store_id INT REFERENCES stores(id) ON DELETE CASCADE,
    room_type VARCHAR(50) NOT NULL, -- Private Tea Room, Traditional Mat, Afternoon Tea Table
    seating_preference VARCHAR(100),
    booking_time TIMESTAMP WITH TIME ZONE NOT NULL,
    pax INT NOT NULL,
    status VARCHAR(30) DEFAULT 'PENDING', -- PENDING, CONFIRMED, CANCELLED, FULFILLED
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 9. Orders
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    store_id INT REFERENCES stores(id),
    order_number VARCHAR(50) UNIQUE NOT NULL,
    order_type VARCHAR(30) NOT NULL, -- DINE_IN, TAKEAWAY, DELIVERY
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(30) DEFAULT 'RECEIVED', -- RECEIVED, PREPARING, READY, DELIVERED, CANCELLED
    customisation_notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 10. Order Items
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id) ON DELETE CASCADE,
    product_id INT REFERENCES products(id),
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    tea_strength VARCHAR(50),
    sugar_level VARCHAR(50),
    ice_level VARCHAR(50),
    milk_option VARCHAR(100)
);

-- 11. Payments History
CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id),
    payment_method VARCHAR(50) NOT NULL, -- CLUB_CARD, WECHAT_PAY, ALIPAY, APPLE_PAY
    transaction_id VARCHAR(100) UNIQUE,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(30) DEFAULT 'SUCCESS', -- SUCCESS, FAILED, REFUNDED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 12. Push Notifications Log
CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    title_cn VARCHAR(150) NOT NULL,
    title_en VARCHAR(150) NOT NULL,
    body_cn TEXT NOT NULL,
    body_en TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 13. Customer Reviews & Ratings
CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    product_id INT REFERENCES products(id),
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

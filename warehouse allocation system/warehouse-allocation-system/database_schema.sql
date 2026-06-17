-- Drop database if exists (for fresh setup)
DROP DATABASE IF EXISTS warehouse_db;

-- Create database
CREATE DATABASE warehouse_db;
USE warehouse_db;

-- =====================================================
-- 1. WAREHOUSES TABLE
-- =====================================================
CREATE TABLE warehouses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    version BIGINT DEFAULT 0,
    INDEX idx_status (status),
    INDEX idx_deleted_at (deleted_at)
);

-- =====================================================
-- 2. PRODUCTS TABLE
-- =====================================================
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE,
    total_stock INT NOT NULL DEFAULT 0,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    INDEX idx_sku (sku),
    INDEX idx_name (name)
);

-- =====================================================
-- 3. WAREHOUSE INVENTORY TABLE
-- =====================================================
CREATE TABLE warehouse_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    available_quantity INT NOT NULL DEFAULT 0,
    allocated_quantity INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY unique_warehouse_product (warehouse_id, product_id),
    INDEX idx_warehouse (warehouse_id),
    INDEX idx_product (product_id),
    INDEX idx_available_quantity (available_quantity)
);

-- =====================================================
-- 4. ALLOCATIONS TABLE
-- =====================================================
CREATE TABLE allocations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    allocation_number VARCHAR(50) NOT NULL UNIQUE,
    product_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    allocated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ALLOCATED',
    reference_number VARCHAR(100),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    INDEX idx_product_id (product_id),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_allocated_at (allocated_at),
    INDEX idx_status (status),
    INDEX idx_allocation_number (allocation_number)
);

-- =====================================================
-- 5. STOCK TRANSFERS TABLE
-- =====================================================
CREATE TABLE stock_transfers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transfer_number VARCHAR(50) NOT NULL UNIQUE,
    source_warehouse_id BIGINT NOT NULL,
    target_warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    transfer_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    transfer_type VARCHAR(20) DEFAULT 'MANUAL',
    FOREIGN KEY (source_warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (target_warehouse_id) REFERENCES warehouses(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_source_warehouse (source_warehouse_id),
    INDEX idx_target_warehouse (target_warehouse_id),
    INDEX idx_product (product_id),
    INDEX idx_transfer_date (transfer_date),
    INDEX idx_status (status)
);

-- =====================================================
-- 6. AUDIT LOG TABLE (Optional - for tracking changes)
-- =====================================================
CREATE TABLE audit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    created_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_created_at (created_at)
);

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Warehouses
INSERT INTO warehouses (name, location, capacity, status) VALUES
('North Warehouse', 'New York, NY', 10000, 'ACTIVE'),
('South Warehouse', 'Atlanta, GA', 8000, 'ACTIVE'),
('East Warehouse', 'Boston, MA', 12000, 'ACTIVE'),
('West Warehouse', 'Los Angeles, CA', 15000, 'ACTIVE'),
('Central Warehouse', 'Chicago, IL', 9000, 'ACTIVE');

-- Insert Products
INSERT INTO products (name, sku, total_stock, price) VALUES
('Laptop Pro X1', 'LAP-X1-001', 500, 1299.99),
('Gaming Mouse RGB', 'MOU-RGB-002', 1000, 49.99),
('Mechanical Keyboard', 'KEY-MEC-003', 750, 89.99),
('27" 4K Monitor', 'MON-4K-004', 300, 449.99),
('USB-C Hub 5-in-1', 'USB-HUB-005', 2000, 39.99),
('Wireless Headphones', 'AUD-WH-006', 800, 79.99),
('External SSD 1TB', 'STO-SSD-007', 400, 119.99),
('Webcam 4K', 'CAM-WEB-008', 350, 99.99);

-- Insert Warehouse Inventory
INSERT INTO warehouse_inventory (warehouse_id, product_id, available_quantity, allocated_quantity) VALUES
-- North Warehouse (ID: 1)
(1, 1, 150, 10),
(1, 2, 300, 20),
(1, 3, 200, 15),
(1, 4, 80, 5),
(1, 5, 500, 30),
(1, 6, 200, 10),
(1, 7, 100, 5),
(1, 8, 80, 5),

-- South Warehouse (ID: 2)
(2, 1, 100, 5),
(2, 2, 250, 15),
(2, 3, 150, 10),
(2, 4, 60, 3),
(2, 5, 400, 20),
(2, 6, 150, 8),
(2, 7, 80, 4),
(2, 8, 60, 3),

-- East Warehouse (ID: 3)
(3, 1, 200, 15),
(3, 2, 350, 25),
(3, 3, 250, 20),
(3, 4, 100, 8),
(3, 5, 600, 40),
(3, 6, 250, 15),
(3, 7, 120, 8),
(3, 8, 100, 8),

-- West Warehouse (ID: 4)
(4, 1, 50, 2),
(4, 2, 100, 5),
(4, 3, 150, 10),
(4, 4, 60, 4),
(4, 5, 300, 15),
(4, 6, 100, 5),
(4, 7, 60, 3),
(4, 8, 60, 3),

-- Central Warehouse (ID: 5)
(5, 1, 80, 4),
(5, 2, 150, 10),
(5, 3, 100, 8),
(5, 4, 50, 3),
(5, 5, 200, 12),
(5, 6, 100, 6),
(5, 7, 50, 3),
(5, 8, 50, 2);

-- Insert Sample Allocations
INSERT INTO allocations (allocation_number, product_id, warehouse_id, quantity, status, reference_number) VALUES
('ALLOC-1700000001-1234', 1, 1, 5, 'ALLOCATED', 'ORD-001'),
('ALLOC-1700000002-5678', 2, 2, 10, 'ALLOCATED', 'ORD-002'),
('ALLOC-1700000003-9012', 3, 3, 8, 'COMPLETED', 'ORD-003'),
('ALLOC-1700000004-3456', 4, 4, 3, 'CANCELLED', 'ORD-004'),
('ALLOC-1700000005-7890', 5, 5, 20, 'ALLOCATED', 'ORD-005');

-- Insert Sample Stock Transfers
INSERT INTO stock_transfers (transfer_number, source_warehouse_id, target_warehouse_id, product_id, quantity, status, transfer_type) VALUES
('TRF-1700000001-1111', 1, 5, 1, 10, 'COMPLETED', 'MANUAL'),
('TRF-1700000002-2222', 2, 4, 2, 20, 'COMPLETED', 'AUTO_REBALANCE'),
('TRF-1700000003-3333', 3, 1, 3, 15, 'PENDING', 'MANUAL'),
('TRF-1700000004-4444', 4, 2, 4, 5, 'COMPLETED', 'MANUAL'),
('TRF-1700000005-5555', 5, 3, 5, 25, 'FAILED', 'AUTO_REBALANCE');

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Check all tables
SHOW TABLES;

-- Verify warehouse data
SELECT * FROM warehouses;

-- Verify product data
SELECT * FROM products;

-- Verify inventory data
SELECT 
    w.name AS warehouse_name,
    p.name AS product_name,
    wi.available_quantity,
    wi.allocated_quantity
FROM warehouse_inventory wi
JOIN warehouses w ON wi.warehouse_id = w.id
JOIN products p ON wi.product_id = p.id
LIMIT 10;

-- Get inventory summary by warehouse
SELECT 
    w.name AS warehouse_name,
    SUM(wi.available_quantity) AS total_available,
    SUM(wi.allocated_quantity) AS total_allocated,
    w.capacity,
    (SUM(wi.available_quantity) + SUM(wi.allocated_quantity)) AS total_used,
    ((SUM(wi.available_quantity) + SUM(wi.allocated_quantity)) * 100.0 / w.capacity) AS utilization_percentage
FROM warehouse_inventory wi
JOIN warehouses w ON wi.warehouse_id = w.id
GROUP BY w.id, w.name, w.capacity;

-- Get allocation summary
SELECT 
    status,
    COUNT(*) AS count,
    SUM(quantity) AS total_quantity
FROM allocations
GROUP BY status;

-- Get transfer summary
SELECT 
    status,
    COUNT(*) AS count,
    SUM(quantity) AS total_quantity
FROM stock_transfers
GROUP BY status;

-- =====================================================
-- STORED PROCEDURES (Optional)
-- =====================================================

-- Procedure to get warehouse utilization
DELIMITER //
CREATE PROCEDURE GetWarehouseUtilization()
BEGIN
    SELECT 
        w.id,
        w.name,
        w.location,
        w.capacity,
        COALESCE(SUM(wi.available_quantity + wi.allocated_quantity), 0) AS total_used,
        ROUND(COALESCE(SUM(wi.available_quantity + wi.allocated_quantity), 0) * 100.0 / w.capacity, 2) AS utilization_percentage
    FROM warehouses w
    LEFT JOIN warehouse_inventory wi ON w.id = wi.warehouse_id
    WHERE w.deleted_at IS NULL
    GROUP BY w.id, w.name, w.location, w.capacity
    ORDER BY utilization_percentage DESC;
END //
DELIMITER ;

-- Procedure to get product stock across warehouses
DELIMITER //
CREATE PROCEDURE GetProductStockDistribution(IN p_product_id BIGINT)
BEGIN
    SELECT 
        w.name AS warehouse_name,
        w.location,
        wi.available_quantity,
        wi.allocated_quantity,
        (wi.available_quantity + wi.allocated_quantity) AS total_quantity
    FROM warehouse_inventory wi
    JOIN warehouses w ON wi.warehouse_id = w.id
    WHERE wi.product_id = p_product_id
    ORDER BY total_quantity DESC;
END //
DELIMITER ;

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

-- Additional indexes for better query performance
CREATE INDEX idx_warehouse_inventory_available ON warehouse_inventory(available_quantity);
CREATE INDEX idx_allocations_allocated_at ON allocations(allocated_at);
CREATE INDEX idx_stock_transfers_transfer_date ON stock_transfers(transfer_date);
CREATE INDEX idx_products_price ON products(price);

-- =====================================================
-- VIEWS (Optional)
-- =====================================================

-- View for warehouse inventory summary
CREATE VIEW v_warehouse_inventory_summary AS
SELECT 
    w.id AS warehouse_id,
    w.name AS warehouse_name,
    w.location,
    w.capacity,
    COUNT(DISTINCT wi.product_id) AS unique_products,
    SUM(wi.available_quantity) AS total_available,
    SUM(wi.allocated_quantity) AS total_allocated,
    SUM(wi.available_quantity + wi.allocated_quantity) AS total_inventory,
    ROUND(SUM(wi.available_quantity + wi.allocated_quantity) * 100.0 / w.capacity, 2) AS capacity_utilization
FROM warehouses w
LEFT JOIN warehouse_inventory wi ON w.id = wi.warehouse_id
WHERE w.deleted_at IS NULL
GROUP BY w.id, w.name, w.location, w.capacity;

-- View for product availability across warehouses
CREATE VIEW v_product_availability AS
SELECT 
    p.id AS product_id,
    p.name AS product_name,
    p.sku,
    p.price,
    COUNT(DISTINCT wi.warehouse_id) AS warehouses_count,
    SUM(wi.available_quantity) AS total_available,
    SUM(wi.allocated_quantity) AS total_allocated
FROM products p
LEFT JOIN warehouse_inventory wi ON p.id = wi.product_id
GROUP BY p.id, p.name, p.sku, p.price;

-- =====================================================
-- TRIGGERS (Optional)
-- =====================================================

-- Trigger to update product total_stock when inventory changes
DELIMITER //
CREATE TRIGGER UpdateProductTotalStock
AFTER INSERT ON warehouse_inventory
FOR EACH ROW
BEGIN
    UPDATE products 
    SET total_stock = (
        SELECT COALESCE(SUM(available_quantity + allocated_quantity), 0)
        FROM warehouse_inventory
        WHERE product_id = NEW.product_id
    )
    WHERE id = NEW.product_id;
END //
DELIMITER ;

-- =====================================================
-- COMMIT AND VERIFY
-- =====================================================

COMMIT;

-- Display final verification
SELECT 'Database schema created successfully!' AS status;
SELECT COUNT(*) AS total_warehouses FROM warehouses;
SELECT COUNT(*) AS total_products FROM products;
SELECT COUNT(*) AS total_inventory_records FROM warehouse_inventory;
SELECT COUNT(*) AS total_allocations FROM allocations;
SELECT COUNT(*) AS total_transfers FROM stock_transfers;
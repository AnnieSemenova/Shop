CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    price DOUBLE PRECISION NOT NULL
);
-- просто индекс для starts with поиска, чувствительного к регистру, для более сложных сценариев, можно сделать индекс поверх LOWER()
-- или использовать GIN индекс для contains
CREATE INDEX idx_product_users_name ON product(name);

CREATE TABLE cart (
    id BIGSERIAL PRIMARY KEY,
    username TEXT NOT NULL
);

CREATE TABLE cart_item (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL REFERENCES cart(id),
    product_id BIGINT NOT NULL REFERENCES product(id),
    quantity INT NOT NULL
);

CREATE INDEX idx_cart_item_cart_id ON cart_item(cart_id);
CREATE INDEX idx_cart_item_product_id ON cart_item(product_id);

-- order зарезервировано, во избежание путаницы, именуем как shop_order
CREATE TABLE shop_order (
    id BIGSERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- TEXT вместо предсозданного type для большей гибкости
    status TEXT NOT NULL DEFAULT 'CREATED'
);

CREATE TABLE order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES shop_order(id),
    product_id BIGINT NOT NULL REFERENCES product(id),
    quantity INT NOT NULL,
    price DOUBLE PRECISION NOT NULL
);

CREATE INDEX idx_order_item_order_id ON order_item(order_id);
CREATE INDEX idx_order_item_product_id ON order_item(product_id);
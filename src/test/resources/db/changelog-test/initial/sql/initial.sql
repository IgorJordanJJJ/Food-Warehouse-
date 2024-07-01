CREATE TABLE Category (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) UNIQUE NOT NULL,
                          description VARCHAR(500),
                          created_date TIMESTAMP,
                          last_updated_date TIMESTAMP,
                          active BOOLEAN
);

CREATE TABLE Product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) UNIQUE NOT NULL,
                         description VARCHAR(500),
                         price DECIMAL(10, 2),
                         weight DECIMAL(10, 2),
                         category_id BIGINT,
                         manufacture_date DATE,
                         expiration_date DATE,
                         manufacturer VARCHAR(100),
                         stock_quantity INT,
                         sku VARCHAR(50),
                         brand VARCHAR(100),
                         country_of_origin VARCHAR(100),
                         discount DECIMAL(3, 2),
                         available BOOLEAN,
                         created_date TIMESTAMP,
                         last_updated_date TIMESTAMP,
                         CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES Category(id)
);

COMMENT ON TABLE Category IS 'Таблица для хранения категорий';

COMMENT ON COLUMN Category.id IS 'Идентификатор категории';
COMMENT ON COLUMN Category.name IS 'Название категории';
COMMENT ON COLUMN Category.description IS 'Описание категории';
COMMENT ON COLUMN Category.created_date IS 'Дата создания категории';
COMMENT ON COLUMN Category.last_updated_date IS 'Дата последнего обновления категории';
COMMENT ON COLUMN Category.active IS 'Статус активности категории';

COMMENT ON TABLE Product IS 'Таблица для хранения продуктов';

COMMENT ON COLUMN Product.id IS 'Идентификатор продукта';
COMMENT ON COLUMN Product.name IS 'Название продукта';
COMMENT ON COLUMN Product.description IS 'Описание продукта';
COMMENT ON COLUMN Product.price IS 'Цена продукта';
COMMENT ON COLUMN Product.weight IS 'Масса продукта';
COMMENT ON COLUMN Product.category_id IS 'Идентификатор категории продукта';
COMMENT ON COLUMN Product.manufacture_date IS 'Дата изготовления продукта';
COMMENT ON COLUMN Product.expiration_date IS 'Дата окончания срока годности продукта';
COMMENT ON COLUMN Product.manufacturer IS 'Производитель продукта';
COMMENT ON COLUMN Product.stock_quantity IS 'Количество продукта на складе';
COMMENT ON COLUMN Product.sku IS 'Артикул продукта';
COMMENT ON COLUMN Product.brand IS 'Бренд продукта';
COMMENT ON COLUMN Product.country_of_origin IS 'Страна происхождения продукта';
COMMENT ON COLUMN Product.discount IS 'Скидка на продукт';
COMMENT ON COLUMN Product.available IS 'Доступность продукта';
COMMENT ON COLUMN Product.created_date IS 'Дата создания продукта';
COMMENT ON COLUMN Product.last_updated_date IS 'Дата последнего обновления продукта';


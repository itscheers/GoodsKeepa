CREATE TABLE IF NOT EXISTS categories (
                                          id   INTEGER PRIMARY KEY AUTOINCREMENT,
                                          name TEXT    NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS products (
                                        id          INTEGER PRIMARY KEY AUTOINCREMENT,
                                        name        TEXT    NOT NULL,
                                        brand       TEXT,
                                        price       REAL,
                                        quantity    INTEGER,
                                        category_id INTEGER,
                                        FOREIGN KEY(category_id) REFERENCES categories(id)
    );

CREATE TABLE IF NOT EXISTS suppliers (
                                         id          INTEGER PRIMARY KEY AUTOINCREMENT,
                                         first_name  TEXT    NOT NULL,
                                         last_name   TEXT    NOT NULL,
                                         company     TEXT
);

CREATE TABLE IF NOT EXISTS product_suppliers (
                                                 product_id  INTEGER,
                                                 supplier_id INTEGER,
                                                 PRIMARY KEY(product_id, supplier_id),
    FOREIGN KEY(product_id)   REFERENCES products(id),
    FOREIGN KEY(supplier_id)  REFERENCES suppliers(id)
    );

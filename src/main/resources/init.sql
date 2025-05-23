DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS categories;

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
                                        FOREIGN KEY (category_id) REFERENCES categories(id)
    );

CREATE TABLE IF NOT EXISTS suppliers (
                                         id         INTEGER PRIMARY KEY AUTOINCREMENT,
                                         first_name TEXT    NOT NULL,
                                         last_name  TEXT    NOT NULL,
                                         company    TEXT
);



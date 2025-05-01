package com.mycompany.inventory.service;

import com.mycompany.inventory.dao.ProductDAO;
import com.mycompany.inventory.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDAO dao = new ProductDAO();

    public Product createProduct(String name, String brand, double price, int quantity, int categoryId) throws SQLException {
        List<Product> candidates = dao.filterByName(name);
        for (Product p : candidates) {
            if (p.getBrand().equalsIgnoreCase(brand) && p.getCategoryId() == categoryId) {
                return p;
            }
        }
        Product np = new Product(name, brand, price, quantity, categoryId);
        dao.add(np);
        return np;
    }

    public List<Product> listAll() throws SQLException {
        return dao.getAll();
    }

    public void updateProduct(int id, String name, String brand, double price, int quantity, int categoryId) throws SQLException {
        for (Product p : dao.filterByName(name)) {
            if (p.getBrand().equalsIgnoreCase(brand)
                    && p.getCategoryId() == categoryId
                    && p.getId() != id) {
                throw new SQLException("Товар вже існує: " + name + " (" + brand + ")");
            }
        }
        Product p = dao.getById(id);
        p.setName(name);
        p.setBrand(brand);
        p.setPrice(price);
        p.setQuantity(quantity);
        p.setCategoryId(categoryId);
        dao.update(p);
    }

    public void deleteProduct(int id) throws SQLException {
        dao.delete(id);
    }
}

package com.mycompany.inventory.service;

import com.mycompany.inventory.dao.CategoryDAO;
import com.mycompany.inventory.model.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private final CategoryDAO dao = new CategoryDAO();

    public Category createCategory(String name) throws SQLException {
        List<Category> list = dao.filterByName(name);
        for (Category c : list) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        Category newCat = new Category(name);
        dao.add(newCat);
        return newCat;
    }

    public List<Category> listAll() throws SQLException {
        return dao.getAll();
    }

    public void updateCategory(int id, String newName) throws SQLException {
        for (Category c : dao.filterByName(newName)) {
            if (c.getName().equalsIgnoreCase(newName) && c.getId() != id) {
                throw new SQLException("Категорія с іменем «" + newName + "» вже існує");
            }
        }
        Category c = dao.getById(id);
        c.setName(newName);
        dao.update(c);
    }

    public void deleteCategory(int id) throws SQLException {
        dao.delete(id);
    }
}


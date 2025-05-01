package com.mycompany.inventory.service;

import com.mycompany.inventory.dao.SupplierDAO;
import com.mycompany.inventory.model.Supplier;

import java.sql.SQLException;
import java.util.List;

public class SupplierService {
    private final SupplierDAO dao = new SupplierDAO();

    public Supplier createSupplier(String firstName, String lastName) throws SQLException {
        List<Supplier> list = dao.filterByName(firstName);
        for (Supplier s : list) {
            if (s.getFirstName().equalsIgnoreCase(firstName)
                    && s.getLastName().equalsIgnoreCase(lastName)) {
                return s;
            }
        }
        Supplier ns = new Supplier(firstName, lastName);
        dao.add(ns);
        return ns;
    }

    public List<Supplier> listAll() throws SQLException {
        return dao.getAll();
    }

    public void updateSupplier(int id, String firstName, String lastName) throws SQLException {
        for (Supplier s : dao.filterByName(firstName)) {
            if (s.getFirstName().equalsIgnoreCase(firstName)
                    && s.getLastName().equalsIgnoreCase(lastName)
                    && s.getId() != id) {
                throw new SQLException("Поставщик уже существует: " + firstName + " " + lastName);
            }
        }
        Supplier s = dao.getById(id);
        s.setFirstName(firstName);
        s.setLastName(lastName);
        dao.update(s);
    }

    public void deleteSupplier(int id) throws SQLException {
        dao.delete(id);
    }
}

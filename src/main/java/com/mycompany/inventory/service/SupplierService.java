package com.mycompany.inventory.service;

import com.mycompany.inventory.dao.SupplierDAO;
import com.mycompany.inventory.model.Supplier;

import java.sql.SQLException;
import java.util.List;

public class SupplierService {
    private final SupplierDAO dao = new SupplierDAO();
    public Supplier createSupplier(String firstName, String lastName, String company) throws SQLException {
        List<Supplier> list = dao.filterByName(firstName);
        for (Supplier s : list) {
            if (s.getFirstName().equalsIgnoreCase(firstName)
                    && s.getLastName().equalsIgnoreCase(lastName)
                    && s.getCompany().equalsIgnoreCase(company)) {
                return s;
            }
        }
        Supplier ns = new Supplier(firstName, lastName, company);
        dao.add(ns);
        return ns;
    }

    public List<Supplier> listAll() throws SQLException {
        return dao.getAll();
    }

    public void updateSupplier(int id, String firstName, String lastName, String company) throws SQLException {
        for (Supplier s : dao.filterByName(firstName)) {
            if (s.getFirstName().equalsIgnoreCase(firstName)
                    && s.getLastName().equalsIgnoreCase(lastName)
                    && s.getCompany().equalsIgnoreCase(company)
                    && s.getId() != id) {
                throw new SQLException("Постачальник вже існує: "
                        + firstName + " " + lastName + " (" + company + ")");
            }
        }
        Supplier s = dao.getById(id);
        if (s == null) {
            throw new SQLException("Постачальника з ID=" + id + " не знайдено");
        }
        s.setFirstName(firstName);
        s.setLastName(lastName);
        s.setCompany(company);
        dao.update(s);
    }

    public void deleteSupplier(int id) throws SQLException {
        dao.delete(id);
    }
}

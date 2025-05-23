package com.mycompany.inventory.controller;

import com.mycompany.inventory.model.Supplier;
import com.mycompany.inventory.service.SupplierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SupplierController {
    @FXML private TableView<Supplier> table;
    @FXML private TableColumn<Supplier, Integer> colId;
    @FXML private TableColumn<Supplier, String>  colFirst;
    @FXML private TableColumn<Supplier, String>  colLast;
    @FXML private TableColumn<Supplier, String>  colCompany;
    @FXML private TextField tfSearch;

    private final SupplierService service = new SupplierService();
    private final ObservableList<Supplier> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirst.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLast.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        table.setItems(data);
        loadAll();
    }

    private void loadAll() {
        try {
            data.setAll(service.listAll());
        } catch (SQLException e) {
            showError(e);
        }
    }

    @FXML private void onSearch() {
        String kw = tfSearch.getText().trim().toLowerCase();
        try {
            if (kw.isEmpty()) {
                loadAll();
            } else {
                data.setAll(service.searchSuppliers(kw));
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    @FXML private void onAdd() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setHeaderText("Додати постачальника");
        dlg.setContentText("Введіть ім'я, прізвище и компанію через кому:");
        Optional<String> res = dlg.showAndWait();
        res.ifPresent(str -> {
            try {
                String[] parts = str.split(",", 3);
                String first   = parts[0].trim();
                String last    = parts.length > 1 ? parts[1].trim() : "";
                String company = parts.length > 2 ? parts[2].trim() : "";
                service.createSupplier(first, last, company);
                loadAll();
            } catch (Exception e) {
                showError(e);
            }
        });
    }

    @FXML private void onEdit() {
        Supplier sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        TextInputDialog dlg = new TextInputDialog(
                sel.getFirstName() + ", " + sel.getLastName() + ", " + sel.getCompany()
        );
        dlg.setHeaderText("Редагувати постачальника");
        dlg.setContentText("Ім'я, прізвище, компанія через кому:");
        Optional<String> res = dlg.showAndWait();

        res.ifPresent(str -> {
            try {
                String[] parts = str.split(",", 3);
                String first   = parts[0].trim();
                String last    = parts.length > 1 ? parts[1].trim() : "";
                String company = parts.length > 2 ? parts[2].trim() : "";
                service.updateSupplier(sel.getId(), first, last, company);
                loadAll();
            } catch (Exception e) {
                showError(e);
            }
        });
    }

    @FXML private void onDelete() {
        Supplier sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Видалити «" + sel.getFirstName() + " " + sel.getLastName() +
                        " (" + sel.getCompany() + ")»?",
                ButtonType.YES, ButtonType.NO
        );
        Optional<ButtonType> ans = confirm.showAndWait();
        if (ans.orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                service.deleteSupplier(sel.getId());
                loadAll();
            } catch (SQLException e) {
                showError(e);
            }
        }
    }

    private void showError(Exception e) {
        new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK)
                .showAndWait();
    }
}
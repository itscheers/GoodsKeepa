package com.mycompany.inventory.controller;

import com.mycompany.inventory.model.Product;
import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.service.ProductService;
import com.mycompany.inventory.service.CategoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductController {
    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String>  colName;
    @FXML private TableColumn<Product, String>  colBrand;
    @FXML private TableColumn<Product, Double>  colPrice;
    @FXML private TableColumn<Product, Integer> colQty;
    @FXML private TableColumn<Product, String>  colCat;
    @FXML private TextField tfSearch;

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final ObservableList<Product> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCat.setCellValueFactory(cell -> {
            int catId = cell.getValue().getCategoryId();
            try {
                Category c = categoryService.listAll()
                        .stream()
                        .filter(x -> x.getId() == catId)
                        .findFirst()
                        .orElse(null);
                String name = c != null ? c.getName() : "";
                return new SimpleStringProperty(name);
            } catch (SQLException e) {
                return new SimpleStringProperty("");
            }
        });

        table.setItems(data);
        loadAll();
    }

    private void loadAll() {
        try {
            data.setAll(productService.listAll());
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
                List<Product> filtered =
                        productService.listAll().stream()
                                .filter(p -> p.getName().toLowerCase().contains(kw))
                                .toList();
                data.setAll(filtered);
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    @FXML private void onAdd() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setHeaderText("Новый товар (имя,бренд,цена,кол-во,категория)");
        dlg.setContentText("Введите через запятую:");
        Optional<String> res = dlg.showAndWait();
        res.ifPresent(str -> {
            try {
                String[] arr = str.split(",");
                String name  = arr[0].trim();
                String brand = arr[1].trim();
                double price = Double.parseDouble(arr[2].trim());
                int qty      = Integer.parseInt(arr[3].trim());
                int catId    = categoryService.createCategory(arr[4].trim()).getId();
                productService.createProduct(name, brand, price, qty, catId);
                loadAll();
            } catch (Exception e) {
                showError(e);
            }
        });
    }

    @FXML private void onEdit() {
        Product sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        try {
            String initial = String.join(",",
                    sel.getName(),
                    sel.getBrand(),
                    String.valueOf(sel.getPrice()),
                    String.valueOf(sel.getQuantity()),
                    categoryService.listAll().stream()
                            .filter(c -> c.getId() == sel.getCategoryId())
                            .findFirst().map(Category::getName).orElse("")
            );
            TextInputDialog dlg = new TextInputDialog(initial);
            dlg.setHeaderText("Редактировать товар");
            dlg.setContentText("Имя,бренд,цена,кол-во,категория:");
            Optional<String> res = dlg.showAndWait();
            res.ifPresent(str -> {
                try {
                    String[] a = str.split(",");
                    productService.updateProduct(
                            sel.getId(),
                            a[0].trim(),
                            a[1].trim(),
                            Double.parseDouble(a[2].trim()),
                            Integer.parseInt(a[3].trim()),
                            categoryService.createCategory(a[4].trim()).getId()
                    );
                    loadAll();
                } catch (Exception ex) {
                    showError(ex);
                }
            });
        } catch (SQLException e) {
            showError(e);
        }
    }

    @FXML private void onDelete() {
        Product sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить товар «" + sel.getName() + "»?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                productService.deleteProduct(sel.getId());
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

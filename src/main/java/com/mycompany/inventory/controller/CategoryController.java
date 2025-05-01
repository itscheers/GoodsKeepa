package com.mycompany.inventory.controller;

import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.service.CategoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoryController {

    @FXML private TableView<Category> table;
    @FXML private TableColumn<Category, Integer> colId;
    @FXML private TableColumn<Category, String>  colName;
    @FXML private TextField tfSearch;

    private final CategoryService service = new CategoryService();
    private final ObservableList<Category> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

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
                List<Category> filtered = service.listAll().stream()
                        .filter(c -> c.getName().toLowerCase().contains(kw))
                        .toList();
                data.setAll(filtered);
            }
        } catch (SQLException e) {
            showError(e);
        }
    }

    @FXML private void onAdd() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setHeaderText("Новая категория");
        dlg.setContentText("Введите имя:");
        Optional<String> res = dlg.showAndWait();
        res.ifPresent(name -> {
            try {
                service.createCategory(name.trim());
                loadAll();
            } catch (Exception e) {
                showError(e);
            }
        });
    }

    @FXML private void onEdit() {
        Category sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        TextInputDialog dlg = new TextInputDialog(sel.getName());
        dlg.setHeaderText("Редактировать категорию");
        dlg.setContentText("Новое имя:");
        Optional<String> res = dlg.showAndWait();
        res.ifPresent(name -> {
            try {
                service.updateCategory(sel.getId(), name.trim());
                loadAll();
            } catch (Exception e) {
                showError(e);
            }
        });
    }

    @FXML private void onDelete() {
        Category sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Удалить категорию «" + sel.getName() + "»?",
                ButtonType.YES, ButtonType.NO
        );
        Optional<ButtonType> ans = confirm.showAndWait();
        if (ans.orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                service.deleteCategory(sel.getId());
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

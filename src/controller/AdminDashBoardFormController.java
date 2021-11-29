package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Item;
import model.MovableItem;
import view.tm.ItemTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminDashBoardFormController {
    public AnchorPane dashBoardContext;
    public AnchorPane subContext;
    public JFXTextField txtMostItemCode;
    public JFXTextField txtLeastItemCode;
    public JFXTextField txtMostDescription;
    public JFXTextField txtLeastDescription;
    public JFXTextField txtMostQty;
    public JFXTextField txtLeastQty;
    public JFXTextField txtMostUnitPrice;
    public JFXTextField txtLeastUnitPrice;
    public JFXTextField txtMostDiscount;
    public JFXTextField txtLeastDiscount;
    public TableView tblItemList;
    public TableColumn colItemCode;
    public TableColumn colDescription;

    public void initialize(){

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        try {
            loadMovableItemDetail();
            loadItemList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    ObservableList<ItemTM> observableList = FXCollections.observableArrayList();
    private void loadItemList() throws SQLException {
        List<Item> allItemForAdmin = new ItemController().getAllItemForAdmin();

        for (Item item:allItemForAdmin) {
            observableList.add(new ItemTM(item.getItemCOde(),item.getDescription()));
        }
        tblItemList.setItems(observableList);
    }

    private void loadMovableItemDetail() throws SQLException {
        List<MovableItem> movableItem = new OrderController().findMovableItem();
        if(movableItem.isEmpty()){

        }else{
           txtMostItemCode.setText(movableItem.get(0).getItemCode());
           txtLeastItemCode.setText(movableItem.get(movableItem.size()-1).getItemCode());

            Item itemMost = new ItemController().searchItem(txtMostItemCode.getText());
            txtMostDescription.setText(itemMost.getDescription());
            txtMostQty.setText(String.valueOf(itemMost.getQtyOnHand()));
            txtMostUnitPrice.setText(String.valueOf(itemMost.getUnitPrice()));
            txtMostDiscount.setText(String.valueOf(itemMost.getDiscount()));

            Item itemLeast = new ItemController().searchItem(txtLeastItemCode.getText());
            txtLeastDescription.setText(itemLeast.getDescription());
            txtLeastQty.setText(String.valueOf(itemLeast.getQtyOnHand()));
            txtLeastUnitPrice.setText(String.valueOf(itemLeast.getUnitPrice()));
            txtLeastDiscount.setText(String.valueOf(itemLeast.getDiscount()));


        }
    }

    public void loadUi(String file) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/" + file + ".fxml"));
        subContext.getChildren().clear();;
        subContext.getChildren().add(load);
    }

    public void openDashBoardOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/LogInForm.fxml"));
        Scene scene = new Scene(load);
        Stage window = (Stage) dashBoardContext.getScene().getWindow();
        window.setScene(scene);
    }

    public void openAdminAddItemOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("AdminAddItemForm");
    }

    public void openAdminManageItemsOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("AdminManageItemsForm");

    }

    public void openAdminRemoveItemsOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("AdminRemoveItemsForm");
    }

    public void openAdminDashBoardFormOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/AdminDashBoardForm.fxml"));
        Scene scene = new Scene(load);
        Stage window = (Stage) dashBoardContext.getScene().getWindow();
        window.setScene(scene);
    }

    public void openIncomeReportsOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("AdminIncomeReports");
    }
}

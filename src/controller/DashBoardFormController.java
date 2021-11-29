package controller;

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
import view.tm.ItemTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashBoardFormController {

    public AnchorPane dashBoardContext;
    public AnchorPane subContext;
    public TableView tbtCurrentItem;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colQtyOnHand;

    public void initialize(){

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        try {
            loadCurrentItemDetail();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void openLoginFormOnAction(ActionEvent actionEvent) throws IOException {
       loadUiForDashContext("LogInForm");
    }

    public void openDashBoardFormOnAction(ActionEvent actionEvent) throws IOException {
        loadUiForDashContext("DashBoardForm");
    }

    public void openAddOrderOnAction(ActionEvent actionEvent) throws IOException {
        loadUiForSubContext("MakeCustomerOrderForm");
    }

    public void openManageOrdersOnAction(ActionEvent actionEvent) throws IOException {
        loadUiForSubContext("ManageItemsForm");
    }

    private void loadUiForSubContext(String file) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/" + file + ".fxml"));
        subContext.getChildren().clear();
        subContext.getChildren().add(load);
    }

    private void loadUiForDashContext(String file) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/" + file + ".fxml"));
        Scene scene = new Scene(load);
        Stage window = (Stage) dashBoardContext.getScene().getWindow();
        window.setScene(scene);
    }

    ObservableList<ItemTM> itemTMObservableList = FXCollections.observableArrayList();
    private void loadCurrentItemDetail() throws SQLException {
        List<Item> allItemDetails = new ItemController().getAllItemDetails();

        for (Item item: allItemDetails) {
            itemTMObservableList.add(new ItemTM(item.getItemCOde(),
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQtyOnHand(),
                    item.getDiscount()));

        }
        tbtCurrentItem.setItems(itemTMObservableList);

    }



}

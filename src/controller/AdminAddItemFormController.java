package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import model.Item;

import java.sql.SQLException;

public class AdminAddItemFormController {
    public JFXTextField txtItemCode;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQonHand;
    public JFXTextField txtDescription;
    public JFXTextField txtDiscount;


    public void clearTxtOnAction(ActionEvent actionEvent) {
        txtItemCode.clear();
        txtDescription.clear();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtQonHand.clear();
        txtDiscount.clear();
    }

    public void addItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(new ItemController().checkItem(txtItemCode.getText())){
            new Alert(Alert.AlertType.WARNING,"Item code already exists").show();
            return;
        }
        Item item = new Item(
                txtItemCode.getText(),txtDescription.getText(),txtPackSize.getText(),
                Double.parseDouble(txtUnitPrice.getText()),Integer.parseInt(txtQonHand.getText()),Double.parseDouble(txtDiscount.getText())
        );
        if(new ItemController().saveItem(item)){
            new Alert(Alert.AlertType.INFORMATION,"Item added successfully").show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Error.Try Again latter").show();
        }
    }
}

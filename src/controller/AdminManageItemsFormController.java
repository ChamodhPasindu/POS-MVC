package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import model.Item;

import java.sql.SQLException;

public class AdminManageItemsFormController {

    public JFXTextField txtItemCode;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQonHand;
    public JFXTextField txtDescription;
    public JFXTextField txtDiscount;

    public void clearAllOnAction(ActionEvent actionEvent) {
        txtDescription.clear();
        txtItemCode.clear();
        txtPackSize.clear();;
        txtUnitPrice.clear();
        txtQonHand.clear();
        txtDiscount.clear();
    }

    public void updateItemOnAction(ActionEvent actionEvent) throws SQLException {
        Item item = new Item(txtItemCode.getText(),
                txtDescription.getText(),
                txtPackSize.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQonHand.getText()),
                Double.parseDouble(txtDiscount.getText()));

        if(new ItemController().updateItem(item)){
            new Alert(Alert.AlertType.INFORMATION,"Update Item Successfully").show();
        }else{
            new Alert(Alert.AlertType.WARNING,"Error.Try Again latter").show();;
        }
    }

    public void searchItemOnAction(ActionEvent actionEvent) throws SQLException {

        Item item = new ItemController().searchItem(txtItemCode.getText());
        if(item==null){
            new Alert(Alert.AlertType.WARNING,"Empty Result set").show();
        }else{
            txtDescription.setText(item.getDescription());
            txtPackSize.setText(item.getPackSize());
            txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
            txtQonHand.setText(String.valueOf(item.getQtyOnHand()));
            txtDiscount.setText(String.valueOf(item.getDiscount()));
        }
    }
}

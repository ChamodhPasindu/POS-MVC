package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.Item;

import java.sql.SQLException;
import java.util.Optional;

public class AdminRemoveItemsFormController {
    public JFXTextField txtItemCode;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQonHand;
    public JFXTextField txtDescription;
    public JFXTextField txtDiscount;

    public void clearAllOnAction() {
        txtDescription.clear();
        txtItemCode.clear();
        txtPackSize.clear();;
        txtUnitPrice.clear();
        txtQonHand.clear();
        txtDiscount.clear();
    }

    public void deleteAllOnAction(ActionEvent actionEvent) throws SQLException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION
                , "Do you want to Delete this item?", yes, no);
        alert.setTitle("Delete Confirmation");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(new ItemController().deleteItem(txtItemCode.getText())){
                clearAllOnAction();

            }else{
                new Alert(Alert.AlertType.WARNING,"Error,Try Again").show();
            }
        } else {

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

package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;
import view.tm.CartTM;
import view.tm.ItemTM;

import java.sql.SQLException;

public class PaymentInforController {

    public JFXButton btnCharge;
    public TableView tblPayment;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQty;

    Order order;
    ObservableList<CartTM> cartTMObservableList;

    public  void initialize(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

    }

    public void btnChargeOnAction( ) throws SQLException {

       if (new OrderController().saveOrder(order,cartTMObservableList)) {
            new Alert(Alert.AlertType.INFORMATION, "Order placed successfully").show();

            for (CartTM tm:cartTMObservableList) {
                new OrderController().updateQty(tm.getItemCode(),tm.getQty());
            }


        } else {
            new Alert(Alert.AlertType.WARNING, "Error.Try Again").show();
        }
       btnCharge.setDisable(true);

    }

    ObservableList<ItemTM> cartTMS = FXCollections.observableArrayList();
    public void setDetailsForPayment(Order order, ObservableList<CartTM> observableList, String total) throws SQLException {
        btnCharge.setText("Charge  Rs : "+total);

        for (CartTM ob:observableList) {
           cartTMS.add(new ItemTM(ob.getItemCode(),ob.getDescription(),ob.getQty()));
        }
        tblPayment.setItems(cartTMS);
        this.order=order;
        cartTMObservableList=observableList;

    }
}

package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import model.Customer;

import java.sql.SQLException;

public class AddNewCustomerFormController {

    public JFXTextField txtId;
    public JFXTextField txtTitle;
    public JFXTextField txtAddress;
    public JFXTextField txtCity;
    public JFXTextField txtProvince;
    public JFXTextField txtPostalCode;
    public JFXTextField txtName;

    public void confirmCustomerOnAction(ActionEvent actionEvent) throws SQLException {
        Customer customer = new Customer(txtId.getText(),txtTitle.getText(),txtName.getText(),
                txtAddress.getText(),txtCity.getText(),txtProvince.getText(),txtPostalCode.getText());


        if(new CustomerController().saveCustomer(customer)){
            new Alert(Alert.AlertType.INFORMATION,"Customer added Successfully ").show();
            clearAllOnAction();

        }else{
            new Alert(Alert.AlertType.WARNING,"Error.Try Again latter").show();
        }
    }

    public void clearAllOnAction() {
        txtAddress.clear();
        txtId.clear();
        txtCity.clear();
        txtName.clear();
        txtPostalCode.clear();
        txtProvince.clear();
        txtTitle.clear();
    }
}

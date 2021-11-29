package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import view.tm.CustomerWiseIncomeTM;

import java.sql.SQLException;
import java.util.List;

public class AdminIncomeReportsController {
    public TableView tblCustomerList;
    public TableColumn colCustId;
    public TableColumn colCustName;
    public TableColumn colAddress;
    public TableColumn colNoOfOrders;
    public TableColumn colPayment;
    public DatePicker txtForm;
    public DatePicker txtTo;
    public Label lblTotalIncome;
    public Label lblNoOfOrders;

    public void initialize() {

        colCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        colCustName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNoOfOrders.setCellValueFactory(new PropertyValueFactory<>("noOfOrder"));
        colPayment.setCellValueFactory(new PropertyValueFactory<>("total"));


        try {
            loadCustomerWiseIncomeDetails();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        txtForm.valueProperty().addListener((observable, oldValue, newValue1) -> {
            txtTo.valueProperty().addListener((observable1, oldValue1, newValue2) -> {
                int total=0;
                try {
                    List<String> orderId = new OrderController().getDate(String.valueOf(newValue1), String.valueOf(newValue2));
                    lblNoOfOrders.setText(String.valueOf(orderId.size()));

                    for (String oId:orderId) {
                        total+= new OrderController().getSumOfTotal(oId);
                    }

                    lblTotalIncome.setText("Rs : "+String.valueOf(total));

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });

        });
    }

    ObservableList<CustomerWiseIncomeTM> incomeTMS = FXCollections.observableArrayList();
    private void loadCustomerWiseIncomeDetails() throws SQLException {
        double total=0.0;
        List<String> customerId = new OrderController().getCustomerId();

        for (String custId:customerId) {
            List<String> orderId = new OrderController().getOrderId(custId);
            Customer customer = new CustomerController().searchCustomer(custId);

            for (String oId:orderId) {
                total+= new OrderController().getSumOfTotal(oId);
            }

            incomeTMS.add(new CustomerWiseIncomeTM(custId,customer.getName(),customer.getAddress(),orderId.size(),total));
            total=0.0;
        }
        tblCustomerList.setItems(incomeTMS);
    }
}

package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Customer;
import model.Item;
import model.Order;
import view.tm.CartTM;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MakeCustomerOrderFormController {
    public AnchorPane subContext;
    public JFXComboBox<String> cmbItemId;
    public JFXComboBox<String> cmbCustomerId;
    public Label lblUnitPrice;
    public Label lblQtyOnHand;
    public Label lblDiscount;
    public JFXTextField txtQty;
    public Label lblCustomerName;
    public Label lblCustomerCity;
    public Label lblCustomerAddress;
    public Label lblDate;
    public TableView<CartTM> tblOrderList;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public Label lblTotal;
    public Label lblOrderId;
    public JFXButton btnConfirmOrder;
    public Label lblDescription;


    int selectedRowForRemove=-1;

    public void initialize(){

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        lblTotal.setText("Rs 0.00");
        btnConfirmOrder.setDisable(true);

        loadDate();

        try {
            setOrderId();
            loadItemData();
            loadCustomerData();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        cmbItemId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if(newValue==null ){
            return;
        }
            try {
                setItemData(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue==null ){
                return;
            }
            try {
                setCustomerData(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } );

        tblOrderList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectedRowForRemove= (int) newValue;
        });

    }

    private void loadDate() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));
    }

    private void setOrderId() throws SQLException, ClassNotFoundException {
        lblOrderId.setText(new OrderController().getOrderIdForLbl());
    }

    private void setCustomerData(String newValue) throws SQLException {
        Customer customers = new CustomerController().searchCustomer(newValue);
        if(customers==null){
            new Alert(Alert.AlertType.WARNING,"Empty Result set").show();
        }else {
            lblCustomerName.setText(customers.getName());
            lblCustomerAddress.setText(customers.getAddress());
            lblCustomerCity.setText(customers.getCity());
        }
    }

    public void loadCustomerData() throws SQLException {
        List<String> customers = new CustomerController().getCustomer();
        cmbCustomerId.getItems().addAll(customers);

    }

    String description;
    private void setItemData(String newValue) throws SQLException {
        Item item = new ItemController().searchItem(newValue);
        if(item==null){
            new Alert(Alert.AlertType.WARNING,"Empty Result set").show();
        }else{
            description=item.getDescription();
            lblQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
            lblUnitPrice.setText(String.valueOf(item.getUnitPrice()));
            lblDiscount.setText(String.valueOf(item.getDiscount()));
            lblDescription.setText(item.getDescription());

        }
    }

    private void loadItemData() throws SQLException {
        List<String> items = new ItemController().getItem();
        cmbItemId.getItems().addAll(items);
    }

    public void openAddCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("../view/AddNewCustomerForm.fxml"));
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add new Customer");
        stage.show();
        stage.setOnCloseRequest(event ->{
            try {
                cmbCustomerId.getItems().clear();
                loadCustomerData();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } );
    }

    ObservableList<CartTM> cartTMObservableList=FXCollections.observableArrayList();
    public void addToListOnAction(ActionEvent actionEvent) {

        if(txtQty.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING,"Enter qty").show();
            return;
        }
        btnConfirmOrder.setDisable(false);

        String itemCode=cmbItemId.getValue();
        int qty=Integer.parseInt(txtQty.getText());
        double unitPrice=Double.parseDouble(lblUnitPrice.getText());
        double discount=Double.parseDouble(lblDiscount.getText());
        double total=unitPrice*qty-(unitPrice*qty*discount/100);

        if(Integer.parseInt(lblQtyOnHand.getText())<qty){
            new Alert(Alert.AlertType.WARNING,"Invalid QTY").show();
            return;
        }

        CartTM cartTM=new CartTM(
                itemCode,description,qty,unitPrice,discount,total
        );

        int rowNumber=isExists(cartTM);
        if(rowNumber==-1){
            cartTMObservableList.add(cartTM);

        }else{
            CartTM temp=cartTMObservableList.get(rowNumber);
            CartTM newCartTM=new CartTM(
                    itemCode,description,qty+temp.getQty(),unitPrice,discount,total+temp.getTotal()
            );

            if(newCartTM.getQty()>Integer.parseInt(lblQtyOnHand.getText())){
                System.out.println(newCartTM.getTotal());
                new Alert(Alert.AlertType.WARNING,"QTY out of range").show();
                return;
            }
            cartTMObservableList.remove(rowNumber);
            cartTMObservableList.add(newCartTM);
        }
        tblOrderList.setItems(cartTMObservableList);
        calculateTotal();
    }

    private int isExists(CartTM cartTM) {
        for (int i=0;i<cartTMObservableList.size();i++){
            if(cartTM.getItemCode().equals(cartTMObservableList.get(i).getItemCode())){
                return i;
            }
        }
        return -1;
    }

    public void removeSelectedItemOnAction(ActionEvent actionEvent) {
        if(selectedRowForRemove==-1){
            new Alert(Alert.AlertType.WARNING,"Please select a row").show();
        }else{
            cartTMObservableList.remove(selectedRowForRemove);
            if(cartTMObservableList.isEmpty()){
                btnConfirmOrder.setDisable(true);
            }
            calculateTotal();
            tblOrderList.refresh();
        }
    }

    private void calculateTotal(){
        double total=0.00;
        for (CartTM tm:cartTMObservableList) {
            total=total+tm.getTotal();
        }
        lblTotal.setText("Rs "+String.valueOf(total));
    }

    public void confirmOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {

        if(cmbCustomerId.getValue()==null){
            new Alert(Alert.AlertType.WARNING,"Please Select customer").show();

            return;
        }

        Order order = new Order(
                lblOrderId.getText(),
                lblDate.getText(),
                cmbCustomerId.getValue()
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/PaymentInfo.fxml"));
        Parent load = loader.load();
        PaymentInforController paymentInforController = loader.getController();
        paymentInforController.setDetailsForPayment(order,cartTMObservableList,lblTotal.getText());

        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            btnConfirmOrder.setDisable(true);
            cancelOrderOnAction(actionEvent);
            try {
                setOrderId();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });


       /* if (new OrderController().saveOrder(order,cartTMObservableList)) {
            new Alert(Alert.AlertType.INFORMATION, "Order placed successfully").show();
            btnConfirmOrder.setDisable(true);
            for (CartTM tm:cartTMObservableList) {
                new OrderController().updateQty(tm.getItemCode(),tm.getQty());
            }
            cancelOrderOnAction(actionEvent);
            setOrderId();
        } else {
            new Alert(Alert.AlertType.WARNING, "Error.Try Again").show();
        }*/
    }

    public void cancelOrderOnAction(ActionEvent actionEvent) {
        tblOrderList.getItems().removeAll(cartTMObservableList);
        tblOrderList.refresh();
        calculateTotal();
    }

    public void clearAllOnAction(ActionEvent actionEvent) {
        cmbItemId.setValue(null);
        cmbCustomerId.setValue(null);
        txtQty.clear();
        lblDescription.setText("");
        lblQtyOnHand.setText("");
        lblCustomerCity.setText("");
        lblUnitPrice.setText("");
        lblCustomerAddress.setText("");
        lblDiscount.setText("");
        lblCustomerName.setText("");
    }


}

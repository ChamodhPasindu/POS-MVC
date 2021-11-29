package controller;

import Db.DbConnection;
import javafx.collections.ObservableList;
import model.MovableItem;
import model.Order;
import model.OrderDetail;
import view.tm.CartTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public String getOrderIdForLbl() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT OrderId FROM Orders ORDER BY OrderId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "O-00"+tempId;
            }else if(tempId<=99){
                return "O-0"+tempId;
            }else{
                return "O-"+tempId;
            }

        }else{
            return "O-001";
        }
    }

    public boolean saveOrder(Order order, ObservableList<CartTM> cartTMArrayList) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Orders VALUES(?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,order.getOrderId());
        stm.setObject(2,order.getDate());
        stm.setObject(3,order.getCustomerId());

       boolean b1 =stm.executeUpdate()>0;

        for (CartTM tm:cartTMArrayList) {
            String query1="INSERT INTO OrderDetail VALUES(?,?,?,?,?)";
            PreparedStatement stm1 = con.prepareStatement(query1);
            stm1.setObject(1,order.getOrderId());
            stm1.setObject(2,tm.getItemCode());
            stm1.setObject(3,tm.getQty());
            stm1.setObject(4,tm.getDiscount());
            stm1.setObject(5,tm.getTotal());
            stm1.executeUpdate();
        }
        return b1;
    }

    public void updateOrder(String id,ObservableList<CartTM> cartTMArrayList) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "DELETE FROM OrderDetail WHERE OrderId =?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);
        stm.executeUpdate();
        for (CartTM tm:cartTMArrayList) {
            String query1="INSERT INTO OrderDetail VALUES(?,?,?,?,?)";
            PreparedStatement stm1 = con.prepareStatement(query1);
            stm1.setObject(1,id);
            stm1.setObject(2,tm.getItemCode());
            stm1.setObject(3,tm.getQty());
            stm1.setObject(4,tm.getDiscount());
            stm1.setObject(5,tm.getTotal());
            stm1.executeUpdate();
        }
    }

    public void updateQtyFromManageOrder(String itemCode,int qty) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "UPDATE Item SET QtyOnHand=(QtyOnHand+" + qty
                + ") WHERE ItemCode='" + itemCode + "'";
        PreparedStatement stm = con.prepareStatement(query);
        stm.executeUpdate();
    }

    public void updateQty(String itemCode,int qty) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "UPDATE Item SET QtyOnHand=(QtyOnHand-" + qty
                + ") WHERE ItemCode='" + itemCode + "'";
        PreparedStatement stm = con.prepareStatement(query);
        stm.executeUpdate();
    }

    public List<String> getCustomerId() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT DISTINCT CustId FROM Orders ";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet resultSet = stm.executeQuery();
        List<String> customerId = new ArrayList<>();
        while (resultSet.next()){
            customerId.add(resultSet.getString(1));
        }
        return customerId;
    }

    public List<String> getOrderId(String id) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT OrderId FROM Orders WHERE CustId=?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);
        List<String> orderId=new ArrayList<>();
        ResultSet resultSet = stm.executeQuery();
        while (resultSet.next()){
            orderId.add(resultSet.getString(1));
        }
        return orderId;

    }

    public List<OrderDetail> getOrderDetail(String id) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT ItemCode,OrderQty,Discount,Cost FROM OrderDetail WHERE OrderId=?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);
        List<OrderDetail>orderDetailList = new ArrayList<>();
        ResultSet resultSet = stm.executeQuery();
        while (resultSet.next()){
            orderDetailList.add(new OrderDetail(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getDouble(3),
                    resultSet.getDouble(4)
            ));
        }
        return orderDetailList;
    }

    public boolean deleteOrder(String id) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "DELETE FROM Orders WHERE OrderId=?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);
        return stm.executeUpdate()>0;
    }

    public boolean removeItemFromOrder(String id) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "DELETE FROM OrderDetail WHERE ItemCode=?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);
        return stm.executeUpdate()>0;
    }

    public List<MovableItem> findMovableItem() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT ItemCode,count(*) FROM OrderDetail GROUP BY ItemCode ORDER BY 1";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet resultSet = stm.executeQuery();

        List<MovableItem> movableItems = new ArrayList<>();
        while (resultSet.next()){
            movableItems.add(new MovableItem(
                    resultSet.getString(1),
                    resultSet.getInt(2)
            ));
        }

        return movableItems;
    }

    public double getSumOfTotal(String id) throws SQLException {
        double i=0.0;
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT SUM(Cost) Total FROM OrderDetail WHERE OrderId=?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            i=resultSet.getDouble(1);
        }
        return i;
    }

    public List<String> getDate(String from, String to) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT orderId FROM Orders WHERE orderDate BETWEEN ? AND ?";
        PreparedStatement stm = connection.prepareStatement(query);
        stm.setObject(1,from);
        stm.setObject(2,to);
        ResultSet resultSet = stm.executeQuery();
        List<String> orderList=new ArrayList<>();
        while (resultSet.next()) {
            orderList.add(resultSet.getString(1));
        }
        return orderList;
    }



}

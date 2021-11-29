package controller;

import Db.DbConnection;
import model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemController {

    public boolean checkItem(String id) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT ItemCode FROM Item WHERE ItemCode=?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return true;
        }else {
            return false;
        }
    }

    public boolean saveItem(Item item) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO  Item VALUE (?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,item.getItemCOde());
        stm.setObject(2,item.getDescription());
        stm.setObject(3,item.getPackSize());
        stm.setObject(4,item.getUnitPrice());
        stm.setObject(5,item.getQtyOnHand());
        stm.setObject(6,item.getDiscount());
        return stm.executeUpdate()>0;
    }

    public Item searchItem(String id) throws SQLException {

        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM Item WHERE itemCode = ?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);

        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new Item(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getDouble(4),
                resultSet.getInt(5),
                resultSet.getDouble(6)
            );
        }else{
            return null;
        }
    }

    public boolean updateItem(Item item) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "UPDATE Item set Description = ? , PackSize = ? , UnitPrice = ? , QtyOnHand = ? , Discount = ? WHERE ItemCode = ?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,item.getDescription());
        stm.setObject(2,item.getPackSize());
        stm.setObject(3,item.getUnitPrice());
        stm.setObject(4,item.getQtyOnHand());
        stm.setObject(5,item.getDiscount());
        stm.setObject(6,item.getItemCOde());

        return stm.executeUpdate()>0;
    }

    public boolean deleteItem(String id) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "DELETE FROM Item WHERE ItemCode = ?";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, id);
        return stm.executeUpdate() > 0;
    }

    public List<String> getItem() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM Item";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet resultSet = stm.executeQuery();
        List<String> items = new ArrayList();
        while (resultSet.next()){
            items.add(
                resultSet.getString(1)
            );
        }
        return items;
    }

    public List<Item> getAllItemDetails() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM Item WHERE QtyOnHand !=0";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet resultSet = stm.executeQuery();
        List<Item>itemList=new ArrayList();
        while (resultSet.next()){
            itemList.add(new Item(
              resultSet.getString(1),
              resultSet.getString(2),
              resultSet.getString(3),
              resultSet.getDouble(4),
              resultSet.getInt(5),
              resultSet.getDouble(6)
            ));
        }
        return itemList;
    }

    public List<Item> getAllItemForAdmin() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM Item";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet resultSet = stm.executeQuery();
        List<Item>itemList = new ArrayList<>();
        while (resultSet.next()){
            itemList.add(new Item(resultSet.getString(1),
                    resultSet.getString(2)));
        }
        return itemList;
    }
}

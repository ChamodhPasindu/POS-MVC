package controller;

import Db.DbConnection;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    public boolean saveCustomer(Customer c) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Customer VALUE (?,?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,c.getId());
        stm.setObject(2,c.getTitle());
        stm.setObject(3,c.getName());
        stm.setObject(4,c.getAddress());
        stm.setObject(5,c.getCity());
        stm.setObject(6,c.getPostalCode());
        stm.setObject(7,c.getPostalCode());
        return stm.executeUpdate()>0;
    }

    public List<String> getCustomer() throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM Customer";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet resultSet = stm.executeQuery();
        List<String> customers = new ArrayList();
        while (resultSet.next()){
            customers.add(
                resultSet.getString(1)
            );

        }
        return customers;
    }

    public Customer searchCustomer(String id) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "SELECT * FROM Customer WHERE CustId =? ";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1,id);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next()){
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)
                    );
        }else{
            return null;
        }
    }
}

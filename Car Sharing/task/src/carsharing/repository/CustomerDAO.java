package carsharing.repository;

import carsharing.db.ConnectionManager;
import carsharing.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerDAO implements GenericDAO<Customer> {

    private final ConnectionManager connectionManager;

    public CustomerDAO(String fileName) {
        this.connectionManager = new ConnectionManager(fileName);
    }

    @Override
    public Customer get(int id) {
        String query = "SELECT * FROM customer WHERE id = ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createCustomer(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = createCustomer(resultSet);
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customers;
    }

    @Override
    public void save(Customer customer) {
        String query = "INSERT INTO CUSTOMER(NAME, RENTED_CAR_ID) VALUES(?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try {
                preparedStatement.setString(1, customer.getName());
                if (Objects.isNull(customer.getRentedCarId())) {
                    preparedStatement.setNull(2, Types.INTEGER);
                } else {
                    preparedStatement.setInt(2, customer.getRentedCarId());
                }
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("Unable to insert customer.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Customer customer) {
        String query = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try {
                if (customer.getRentedCarId() == null) {
                    preparedStatement.setNull(1, Types.INTEGER);
                } else {
                    preparedStatement.setInt(1, customer.getRentedCarId());
                }
                preparedStatement.setInt(2, customer.getId());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("Unable to update customer.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Customer customer) {

    }

    private Customer createCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();

        customer.setId(resultSet.getInt("id"));
        customer.setName(resultSet.getString("name"));

        int rentedCarId = resultSet.getInt("rented_car_id");

        if (resultSet.wasNull()) {
            customer.setRentedCarId(null);
        } else {
            customer.setRentedCarId(rentedCarId);
        }

        return customer;
    }
}

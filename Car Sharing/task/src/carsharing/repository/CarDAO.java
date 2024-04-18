package carsharing.repository;

import carsharing.db.ConnectionManager;
import carsharing.entity.Car;
import carsharing.entity.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAO implements GenericDAO<Car> {

    private final ConnectionManager connectionManager;

    public CarDAO(String fileName) {
        this.connectionManager = new ConnectionManager(fileName);
    }

    @Override
    public Car get(int id) {
        String query = "SELECT * FROM car WHERE id = ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createCar(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM car";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Car car = createCar(resultSet);
                cars.add(car);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cars;
    }

    public List<Car> getAllByCompanyId(int companyId) {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM car " +
                       "LEFT JOIN customer " +
                       "ON car.id = customer.rented_car_id " +
                       "WHERE company_Id = ? AND customer.name IS NULL " +
                       "ORDER BY ID";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, companyId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Car company = createCar(resultSet);
                cars.add(company);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cars;
    }

    @Override
    public void save(Car car) {
        String query = "INSERT INTO CAR(NAME, COMPANY_ID) VALUES(?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try {
                preparedStatement.setString(1, car.getName());
                preparedStatement.setInt(2, car.getCompany_id());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("Unable to insert car.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Car car) {

    }

    @Override
    public void delete(Car car) {

    }

    private Car createCar(ResultSet resultSet) throws SQLException {
        Car car = new Car();

        car.setId(resultSet.getInt("id"));
        car.setName(resultSet.getString("name"));
        car.setCompany_id(resultSet.getInt("company_id"));

        return car;
    }
}

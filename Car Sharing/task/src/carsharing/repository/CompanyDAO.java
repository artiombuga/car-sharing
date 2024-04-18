package carsharing.repository;

import carsharing.db.ConnectionManager;
import carsharing.entity.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO implements GenericDAO<Company> {
    private final ConnectionManager connectionManager;

    public CompanyDAO(String fileName) {
        this.connectionManager = new ConnectionManager(fileName);
    }

    @Override
    public Company get(int id) {
        String query = "SELECT * FROM COMPANY WHERE id = ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createCompany(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Company> getAll() {
        List<Company> companies = new ArrayList<>();
        String query = "SELECT * FROM COMPANY";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Company company = createCompany(resultSet);
                companies.add(company);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return companies;
    }

    @Override
    public void save(Company company) {
        String query = "INSERT INTO COMPANY(NAME) VALUES(?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try {
                preparedStatement.setString(1, company.getName());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("Unable to insert company.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Company company) {
    }

    @Override
    public void delete(Company company) {
    }

    private Company createCompany(ResultSet resultSet) throws SQLException {
        Company company = new Company();

        company.setId(resultSet.getInt("id"));
        company.setName(resultSet.getString("name"));

        return company;
    }
}

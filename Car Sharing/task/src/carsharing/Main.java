package carsharing;

import carsharing.repository.CarDAO;
import carsharing.repository.CompanyDAO;
import carsharing.db.DatabaseInitializer;
import carsharing.repository.CustomerDAO;
import carsharing.utiltiy.UserMenu;

public class Main {
    public static void main(String[] args) {
        String databaseFileName = "carsharing";
        if (args.length != 0) {
            if (args[0].equals("-databaseFileName") &&
                args.length > 1) {
                databaseFileName = args[1];
            }
        }

        DatabaseInitializer.populateDb(databaseFileName);
        CompanyDAO companyDAO = new CompanyDAO(databaseFileName);
        CarDAO carDAO = new CarDAO(databaseFileName);
        CustomerDAO customerDAO = new CustomerDAO(databaseFileName);
        UserMenu userMenu = new UserMenu(companyDAO, carDAO, customerDAO);

        userMenu.showMainMenu();
    }
}
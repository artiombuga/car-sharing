package carsharing.utiltiy;

import carsharing.InputHandler;
import carsharing.entity.Car;
import carsharing.entity.Customer;
import carsharing.repository.CarDAO;
import carsharing.repository.CompanyDAO;
import carsharing.entity.Company;
import carsharing.repository.CustomerDAO;

import java.util.List;
import java.util.Objects;

public class UserMenu {
    InputHandler inputHandler = new InputHandler();
    private final CompanyDAO companyDao;
    private final CarDAO carDAO;
    private final CustomerDAO customerDAO;

    private int currentCustomerId = 0;

    public UserMenu(CompanyDAO companyDao, CarDAO carDAO, CustomerDAO customerDAO) {
        this.companyDao = companyDao;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");

            int choice = inputHandler.promptForInt("");

            switch (choice) {
                case 1 -> showCompanyView();
                case 2 -> listCustomers();
                case 3 -> registerCustomer();
                case 0 -> {
                    inputHandler.closeScanner();
                    System.exit(0);
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + choice);
            }
        }
    }


    private void registerCustomer() {
        String customerName = inputHandler.promptForString("Enter the customer name:");

        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setRentedCarId(null);
        customerDAO.save(customer);

        System.out.println("The customer was added!");
    }

    private void listCustomers() {
        List<Customer> customers = customerDAO.getAll();

        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        } else {
            System.out.println("Customer list:");
            for (Customer customer : customers) {
                System.out.println(customer.getId() + ". " + customer.getName());
            }
        }
        System.out.println("0. Back");

        int choice = inputHandler.promptForInt("");

        if (choice == 0) {
            return;
        }

        currentCustomerId = choice;
        showCustomerView(choice);
    }

    private void showCustomerView(int id) {
        while (true) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            int choice = inputHandler.promptForInt("");

            switch (choice) {
                case 1 -> rentCar(id);
                case 2 -> returnRentedCar(id);
                case 3 -> viewRentedCar(id);
                case 0 -> {
                    return;
                }

                default -> throw new IllegalArgumentException("Unexpected value: " + choice);
            }
        }
    }

    private void rentCar(int id) {
        if (customerDAO.get(id).getRentedCarId() != null) {
            System.out.println("You've already rented a car!");
            return;
        }

        showCompanyList(true);
    }

    private void returnRentedCar(int id) {
        Customer customer = customerDAO.get(id);

        if (Objects.isNull(customer.getRentedCarId())) {
            System.out.println("You didn't rent a car!");
            return;
        }

        customer.setRentedCarId(null);
        customerDAO.update(customer);

        System.out.println("You've returned a rented car!");
    }

    private void viewRentedCar(int id) {
        Customer customer = customerDAO.get(id);

        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        Car car = carDAO.get(customer.getRentedCarId());
        System.out.println("Your rented card:");
        System.out.println(car.getName());

        Company company = companyDao.get(car.getCompany_id());
        System.out.println("Company:");
        System.out.println(company.getName());
        System.out.println();
    }

    public void showCompanyView() {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            int choice = inputHandler.promptForInt("");

            switch (choice) {
                case 1 -> showCompanyList(false);
                case 2 -> createCompany();
                case 0 -> {
                    return;
                }

                default -> throw new IllegalArgumentException("Unexpected value: " + choice);
            }
        }
    }

    private void createCompany() {
        String companyName = inputHandler.promptForString("Enter the company name:");

        Company company = new Company();
        company.setName(companyName);
        companyDao.save(company);

        System.out.println("The company was created!");
    }

    public void showCompanyList(boolean isForRent) {
        List<Company> companies = companyDao.getAll();

        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        } else {
            System.out.println("Choose a company:");
            for (Company company : companies) {
                System.out.println(company);
            }
        }
        System.out.println("0. Back");

        int choice = inputHandler.promptForInt("");

        if (choice == 0) {
            return;
        }

        if (isForRent) {
            showCompaniesForRent(choice);
        } else {
            showCompanyOptions(choice);
        }
    }

    private void showCompaniesForRent(int companyId) {
        System.out.println("Choose a car:");
        showCarList(companyId);
        System.out.println("0. Back");

        int choice = inputHandler.promptForInt("");

        if (choice == 0) {
            return;
        }

        Car car = carDAO.get(choice);
        Customer customer = customerDAO.get(currentCustomerId);
        customer.setRentedCarId(car.getId());
        customerDAO.update(customer);

        System.out.println("You rented '" + car.getName() + "'" );
    }

    public void showCompanyOptions(int id) {
        Company company = companyDao.get(id);

        while (true) {
            System.out.println("'" + company.getName() + "'" + " company");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            int choice = inputHandler.promptForInt("");

            switch (choice) {
                case 1 -> showCarList(id);
                case 2 -> createCar(id);
                case 0 -> {
                    return;
                }

                default -> throw new IllegalArgumentException("Unexpected value: " + choice);
            }
        }
    }

    public void showCarList(int companyId) {
        List<Car> cars = carDAO.getAllByCompanyId(companyId);

        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            int id = 1;
            for (Car car : cars) {
                System.out.println(id++ + ". " + car.getName());
            }
        }
        System.out.println();
    }

    private void createCar(int companyId) {
        String carName = inputHandler.promptForString("Enter the car name:");

        Car car = new Car();
        car.setName(carName);
        car.setCompany_id(companyId);
        carDAO.save(car);

        System.out.println("The car was added!");
    }
}

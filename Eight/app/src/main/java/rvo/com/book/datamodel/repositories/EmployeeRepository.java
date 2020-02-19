package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Employee;

public class EmployeeRepository extends FirebaseRepository {
    private static final EmployeeRepository SINGLETON = new EmployeeRepository();
    private static final String COLLECTION = "employee";

    public EmployeeRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(Employee.class);
    }

    public static EmployeeRepository getInstance(){
        return SINGLETON;
    }
}

package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Employee;

public class EmployeeRepository extends FirebaseRepository {
    private static final EmployeeRepository SINGLETON = new EmployeeRepository();
    private static final String COLLECTION = "employees";

    private EmployeeRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(new Employee());
    }

    public static EmployeeRepository getInstance() {
        return SINGLETON;
    }
}

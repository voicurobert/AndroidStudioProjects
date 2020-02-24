package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Product;

public class ProductRepository extends FirebaseRepository {
    private static final ProductRepository SINGLETON = new ProductRepository();
    private static final String COLLECTION = "products";

    private ProductRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(new Product());
    }

    public static ProductRepository getInstance() {
        return SINGLETON;
    }
}

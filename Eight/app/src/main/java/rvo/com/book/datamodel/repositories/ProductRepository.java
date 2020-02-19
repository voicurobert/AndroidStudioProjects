package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Product;

public class ProductRepository extends FirebaseRepository {
    private static final ProductRepository SINGLETON = new ProductRepository();
    private static final String COLLECTION = "product";

    public ProductRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(Product.class);
    }

    public static ProductRepository getInstance(){
        return SINGLETON;
    }
}

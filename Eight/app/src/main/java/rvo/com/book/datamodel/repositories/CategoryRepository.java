package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Category;

public class CategoryRepository extends FirebaseRepository {
    private static final CategoryRepository SINGLETON = new CategoryRepository();
    private static final String COLLECTION = "categories";

    private CategoryRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(new Category());
    }

    public static CategoryRepository getInstance() {
        return SINGLETON;
    }
}

package rvo.com.book.datamodel.repositories;

import rvo.com.book.datamodel.entities.Schedule;

public class ScheduleRepository extends FirebaseRepository {
    private static final String COLLECTION = "schedule";
    private static final ScheduleRepository SINGLETON = new ScheduleRepository();

    public ScheduleRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(Schedule.class);
    }

    public static ScheduleRepository getInstance(){
        return SINGLETON;
    }
}

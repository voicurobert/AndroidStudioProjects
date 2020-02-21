package rvo.com.book.datamodel.repositories;


import rvo.com.book.datamodel.entities.Schedule;

public class ScheduleRepository extends FirebaseRepository {
    private static final String COLLECTION = "schedules";
    private static final ScheduleRepository SINGLETON = new ScheduleRepository();

    private ScheduleRepository() {
        super();
        initializeFirestore(COLLECTION);
        setObjectClass(new Schedule());
    }

    public static ScheduleRepository getInstance(){
        return SINGLETON;
    }

    public Schedule copySchedule(Schedule schedule){
        Schedule newSchedule = new Schedule();
        newSchedule.setMonday(schedule.getMonday());
        newSchedule.setTuesday(schedule.getTuesday());
        newSchedule.setWednesday(schedule.getWednesday());
        newSchedule.setThursday(schedule.getThursday());
        newSchedule.setFriday(schedule.getFriday());
        newSchedule.setSaturday(schedule.getSaturday());
        newSchedule.setSunday(schedule.getSunday());
        newSchedule.setMondayWorkingHours(schedule.getMondayWorkingHours());
        newSchedule.setTuesdayWorkingHours(schedule.getTuesdayWorkingHours());
        newSchedule.setWednesdayWorkingHours(schedule.getWednesdayWorkingHours());
        newSchedule.setThursdayWorkingHours(schedule.getThursdayWorkingHours());
        newSchedule.setFridayWorkingHours(schedule.getFridayWorkingHours());
        newSchedule.setSaturdayWorkingHours(schedule.getSaturdayWorkingHours());
        newSchedule.setSundayWorkingHours(schedule.getSundayWorkingHours());
        insertRecord(newSchedule);
        return newSchedule;
    }
}

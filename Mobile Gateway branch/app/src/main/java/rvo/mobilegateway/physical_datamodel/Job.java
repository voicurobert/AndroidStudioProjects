package rvo.mobilegateway.physical_datamodel;

/**
 * Created by Robert on 9/15/2015.
 */
public class Job {

    public int jobId; // lni job id
    public String networkOrder; // lni network order
    public int woId; // lni WO ID reference to WOM table
    public String domain; // lni Main domain
    public String impactOnTrafic;
    public int priority;
    public String customer;
    public String description;
    public String startDate;
    public String dueDate;
    public String deadline;

}

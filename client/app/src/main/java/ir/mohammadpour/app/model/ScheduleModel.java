package ir.mohammadpour.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amin on 2017-08-12.
 */
public class ScheduleModel {
    public String DateStr;
    public String daytype;
    public String weekday;
    public String daynumber;
    public String month;
    public List<Time> Dates;
    public ScheduleModel()
    {
        this.Dates=new ArrayList<>();
    }
    public void AddToList(String _Time,String _Date)
    {
        Time item=new Time();
        item.Time=_Time;
        item.Date=_Date;
        this.Dates.add(item);
    }

    public class Time
    {
        public String Time;
        public String Date;

    }
}

package ir.mohammadpour.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Amin on 2016-09-24.
 */
@Table(name = "Message")
public class Message extends Model {

    @Column(name = "s_name")
    public String s_name;

    @Column(name = "s_time")
    public String s_time;
    public Message()
    {
        super();
    }
   public Message(String s_name ,String s_time){
        this.s_name = s_name;
        this.s_time = s_time;
    }
}


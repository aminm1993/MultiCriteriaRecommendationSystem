package ir.mohammadpour.app.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Amin on 2017-09-03.
 */
public class PassengerOrder{

        public int ID;
        public int Status;
        public String ServiceName;
        public String CreateDate;
        public String Date1;
        public long Price;
        public List<OrderDetails> Services;


}

package ir.mohammadpour.app.model;

import java.io.Serializable;

/**
 * Created by Amin on 2017-09-03.
 */
public class SubService implements Serializable, Comparable<SubService>  {

        public int ID;
        public String Name;
        public double Price;
        public double Price_Strike;
        public String Unit;
        public String Description;
        public String ImgUrl;
        public String CategoryId;

        @Override
        public int compareTo(SubService another) {
                return this.Name.compareToIgnoreCase(another.Name);
        }
}

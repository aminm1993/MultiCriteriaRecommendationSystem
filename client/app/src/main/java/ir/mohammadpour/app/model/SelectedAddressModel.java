package ir.mohammadpour.app.model;

/**
 * Created by Amin on 2017-06-17.
 */
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "SelectedAddress")
public class SelectedAddressModel extends Model{
    @Column(name = "Name")
    public String Name;
    @Column(name = "lat")
    public String lat;
    @Column(name = "lng")
    public String lng;
    @Column(name = "description")
    public String description;

    public SelectedAddressModel()
    {
        super();
    }
    public SelectedAddressModel(String lat, String lng,String name,String description){
        this.lat = lat;
        this.lng = lng;
        this.Name = name;
        this.description = description;
    }
}

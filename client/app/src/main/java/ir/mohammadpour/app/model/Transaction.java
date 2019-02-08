package ir.mohammadpour.app.model;

/**
 * Created by Amin on 2016-09-24.
 */
public class Transaction {

    private  int Id ;
    private  int CityId ;
    private  int UserId ; // ==> 0 passenger , 1 driver
    private  boolean UserType ;
    private  double Value ;
    private  String Description ;
    private  int TransferId ;
    private  boolean Type ; // ==> 0 baraye kasr , 1 baraye afzayesh
    private  int ForType ;//==>0 baraye cancel kardane safar,1 commision e safar, 2 pule safar be hesabe ranande, 3 comision e ruzane,4 takhfif babate code e moarref
   // private  DateTime CreatedDate { get; set; }
    private  String UserFullName ;
    private  String CreateDateStr ;
    private  String UserPhone ;

    public int getId()
    {
        return Id;
    }
    public void setId(int _Id)
    {
        this.Id=_Id;
    }

    public int getCityId()
    {
        return CityId;
    }
    public void setCityId(int _CityId)
    {
        this.CityId=_CityId;
    }

    public int getUserId()
    {
        return UserId;
    }
    public void setUserId(int _UserId)
    {
        this.UserId=_UserId;
    }
    public double getValue()
    {
        return Value;
    }
    public void setValue(double _Value)
    {
        this.Value=_Value;
    }
    public String getDescription()
    {
        return Description;
    }
    public void setDescription(String _Description)
    {
        this.Description=_Description;
    }
    public boolean getType()
    {
        return Type;
    }
    public void setType(boolean _Type)
    {
        this.Type=_Type;
    }
    public String getCreateDateStr()
    {
        return CreateDateStr;
    }
    public void setCreateDateStr(String _CreateDateStr)
    {
        this.CreateDateStr=_CreateDateStr;
    }

}




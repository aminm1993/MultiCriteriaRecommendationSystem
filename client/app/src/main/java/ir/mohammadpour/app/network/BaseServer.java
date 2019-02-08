package ir.mohammadpour.app.network;




import java.util.List;

import ir.mohammadpour.app.model.BackUpMessageModel;
import ir.mohammadpour.app.model.ChatModel;
import ir.mohammadpour.app.model.Price;
import ir.mohammadpour.app.model.RouteModel;
import ir.mohammadpour.app.model.Shop;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by amin on 8/11/17.
 */

public interface BaseServer {


    @GET("/home/AddAndroidRequest2")
    Call<String> addTransferRequest(@Query("address") String address, @Query("lat") String lat,
                                    @Query("lng") String lng, @Query("mabda") String mabda,
                                    @Query("customer_lat") String customer_lat, @Query("customer_lng") String customer_lng,
                                    @Query("customer_address") String customer_address, @Query("tozihat") String tozihat,
                                    @Query("takhfif_unique_id") String takhfif_unique_id, @Query("maghsad2_lat") String maghsad2_lat,
                                    @Query("maghsad2_lng") String maghsad2_lng, @Query("raft_va_bargasht") String raft_va_bargasht,
                                    @Query("bar") String bar, @Query("tavaghof") String tavaghof,
                                    @Query("maghsad_discount") String maghsad_discount,
                                    @Query("phone") String phone,
                                    @Header("token") String token,@Header("User-Agent") String User_Agent,
                                    @Query("transfer_type") String transfer_type,
                                    @Query("payment_type") String payment_type);



    @GET("/home/AddNoDriverTransfer")
    Call<String> addNoDriverRequest(@Query("address") String address, @Query("lat") String lat,
                                    @Query("lng") String lng, @Query("mabda") String mabda,
                                    @Query("customer_lat") String customer_lat, @Query("customer_lng") String customer_lng,
                                    @Query("customer_address") String customer_address, @Query("tozihat") String tozihat,
                                    @Query("takhfif_unique_id") String takhfif_unique_id, @Query("maghsad2_lat") String maghsad2_lat,
                                    @Query("maghsad2_lng") String maghsad2_lng, @Query("raft_va_bargasht") String raft_va_bargasht,
                                    @Query("bar") String bar, @Query("tavaghof") String tavaghof,
                                    @Query("maghsad_discount") String maghsad_discount,
                                    @Query("phone") String phone,
                                    @Header("token") String token,@Header("User-Agent") String User_Agent);



    @GET("/home/GetAllShopsByPassengerPhone")
    Call<List<Shop>> getShops(@Query("phone") String phone);


    @GET("/home/GetPrice2")
    Call<Price> getPrice(@Query("lat") String lat, @Query("lng") String lng,
                         @Query("lat2") String lat2, @Query("lng2") String lng2,
                         @Query("lat3") String lat3, @Query("lng3") String lng3,
                         @Query("bar") Boolean bar, @Query("raftbargasht") Boolean raftbargasht,
                         @Query("waittime") int waittime, @Query("phone") String phone,
                         @Query("discount_code") String discount_code);

    @GET("/home/GetPriceForEdit")
    Call<Price> GetPriceForEdit(@Query("lat") String lat, @Query("lng") String lng,
                         @Query("lat2") String lat2, @Query("lng2") String lng2,
                         @Query("lat3") String lat3, @Query("lng3") String lng3,
                         @Query("bar") Boolean bar, @Query("raftbargasht") Boolean raftbargasht,
                         @Query("waittime") int waittime, @Query("phone") String phone
                         ,@Query("set") String set);


    @GET("/home/getAllMsgs")
    Call<List<BackUpMessageModel>> getAllMsgs(@Query("phone") String phone);


    @GET("/home/AddMsgToBackUp")
    Call<String> AddMsgToBackUp(@Query("phone") String phone,@Query("msg") String msg);


    @GET("/payanname/GetRoutes")
    Call<List<RouteModel.CompleteDirection>> getRoute(@Query("ori_lat") Double origin,
                                         @Query("ori_lng") Double ori_lng,
                                         @Query("dest_lat") Double dest_lat,
                                         @Query("dest_lng") Double dest_lng,
                                         @Query("soc") Integer soc);


}

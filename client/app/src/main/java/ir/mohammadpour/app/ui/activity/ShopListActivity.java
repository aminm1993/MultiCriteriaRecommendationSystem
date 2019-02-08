package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.model.Shop;
import ir.mohammadpour.app.network.BaseServer;
import ir.mohammadpour.app.network.ServiceGenerator;
import ir.mohammadpour.app.ui.adapter.ShopAdapter;
import ir.mohammadpour.app.ui.util.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShopListActivity extends AppCompatActivity {

    GridView lv;
    Context context;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        context=this;
        activity=this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String url=  prefs.getString("Server_MainUrl", null);
        lv=(GridView)findViewById(R.id.lv);
        ServiceGenerator.API_BASE_URL=url;
        String _phone = prefs.getString("customer_phone", null);
        GetShopList(_phone);

    }

    private void GetShopList(final String phone)
    {
        BaseServer apiService = ServiceGenerator.createService(BaseServer.class);
        if (new ConnectionDetector(context).isConnectingToInternet()) {
            //Connected to the Internet
            Call<List<Shop>> call = apiService.getShops(phone);
            call.enqueue(new Callback<List<Shop>>() {
                @Override
                public void onResponse(Call<List<Shop>> call, retrofit2.Response<List<Shop>> response) {
                    List<Shop> res = response.body();
                    if (res != null) {
                        ShopAdapter adapter=new ShopAdapter(activity,res);
                        lv.setAdapter(adapter);
                    }
                    else {

                    }
                }

                @Override
                public void onFailure(Call<List<Shop>> call, Throwable t) {

                    Toast.makeText(context,"مشکل در دریافت اطلاعات",Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            //loading.setVisibility(View.GONE);


            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("مشکل در برقراری ارتباط");
            dialog.setPositiveButton("سعی مجدد", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    GetShopList(phone);
                }
            });
            dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            dialog.show();

        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

package ir.mohammadpour.app.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ir.mohammadpour.app.R;
import ir.mohammadpour.app.ui.util.SwitchButton;


/**
 * Created by amin on 5/10/17.
 */

public class BottomHeader extends LinearLayout {

    private BottomClickedListener bottomClickedListener;
    private Context context;
    private LayoutInflater inflater;
    private TextView tv_origin, tv_destination, tv_second_destination,
            tv_price, tv_kilometer, tv_time, tv_pament, tv_count_text;
    private ImageView tv_add, tv_remove;
    private LinearLayout price_part, second_destination_container;
    private Button btn_request;
    private LinearLayout ll_container;
    private LinearLayout tv_count;
    private SwitchButton paymentType;

    private LinearLayout tayid_mabda_layout,darkhaste_safar_layout;
    private TextView tayid_mabda,payment_symbol,etebar_value,etebar_text
            ,payment_type_text,price_value,price_text,discount
            ,darkhast_safar;

    private TextView tv_origin_bottom, tv_destination_bottom, tv_second_bottom, tv_cost_bottom;


    public BottomHeader(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BottomHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public BottomHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        LinearLayout barView = ((LinearLayout) inflater.inflate(R.layout.bottom_header_layout, null));
        addView(barView);


        barView.findViewById(R.id.iv_setting).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomClickedListener.eventOccured(v.getId());
            }
        });

        final Typeface face = Typeface.createFromAsset( context.getAssets(),
                "BYekan.ttf");

        tayid_mabda_layout=(LinearLayout)barView.findViewById(R.id.tayid_mabda_layout);
        tayid_mabda=(TextView)barView.findViewById(R.id.tayid_mabda);
        darkhaste_safar_layout=(LinearLayout)barView.findViewById(R.id.darkhaste_safar_layout);
        payment_symbol=(TextView) barView.findViewById(R.id.payment_symbol);
        etebar_value=(TextView) barView.findViewById(R.id.etebar_value);
        etebar_text=(TextView) barView.findViewById(R.id.etebar_text);
        paymentType = ((SwitchButton) barView.findViewById(R.id.switch_payment_type));
        payment_type_text=(TextView) barView.findViewById(R.id.payment_type_text);
        price_value=(TextView) barView.findViewById(R.id.price_value);
        price_text=(TextView) barView.findViewById(R.id.price_text);
        discount=(TextView) barView.findViewById(R.id.discount);
        darkhast_safar=(TextView) barView.findViewById(R.id.darkhast_safar);

        tayid_mabda.setTypeface(face);
        payment_symbol.setTypeface(face);
        etebar_value.setTypeface(face);
        etebar_text.setTypeface(face);
        paymentType.setTypeface(face);
        payment_type_text.setTypeface(face);
        price_value.setTypeface(face);
        price_text.setTypeface(face);
        discount.setTypeface(face);
        darkhast_safar.setTypeface(face);

        paymentType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             /*   SharedPaymentType paymentType = new SharedPaymentType(context);
                if (isChecked) {
                    if (paymentType.isExist()) {
                        paymentType.clearBasket();
                    }
                    paymentType.addToPref(PAYMENT_TYPE.CASH);
                } else {
                    if (paymentType.isExist()) {
                        paymentType.clearBasket();
                    }
                    paymentType.addToPref(PAYMENT_TYPE.WALLET);
                }*/
            }
        });


        darkhast_safar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomClickedListener.eventOccured(v.getId());
            }
        });


        //setColorFilterForIcons();
    }

    private void setColorFilterForIcons() {
        Drawable drawable_add = tv_add.getDrawable();
        DrawableCompat.setTint(drawable_add, Color.BLACK);
    }

    public void changePaymentType() {
        paymentType.setChecked(true);
    }

    public void chengeTextDestinarion(boolean state) {
      /*  CustomFontApplication.currentLanguage = new ShareLanguage(context).getLanguage();
        if (CustomFontApplication.currentLanguage == CustomFontApplication.LANGUAGE_SELECT.EN) {
            tv_destination_bottom.setText(context.getResources().getString(R.string.destination_en));
        } else {
            if (state)
                tv_destination_bottom.setText(context.getResources().getString(R.string.destination));
            else {
                tv_destination_bottom.setText(context.getResources().getString(R.string.destination1));
            }
        }*/
    }

    public boolean isOriginFilled() {
        if (tv_origin.getText().toString().length() > 0)
            return true;
        else
            return false;
    }

    public boolean isDestinationFilled() {
        if (tv_destination.getText().toString().length() > 0)
            return true;
        else
            return false;
    }

    public boolean isSecondFilled() {
        if (tv_second_destination.getText().toString().length() > 0)
            return true;
        else
            return false;
    }

    public boolean isContainText() {
        if (tv_kilometer.getText().toString().length() > 0)
            return true;
        else
            return false;
    }

    public void setPaymentType(boolean type) {
        paymentType.setChecked(type);
    }

    public void displayCount(int count) {
//        tv_count.setText(count + "");
        String decorator = String.valueOf(count);
        //tv_count.setText(count, "", false);
    }

    public void showPricePart() {
        price_part.setVisibility(VISIBLE);
    }

    public void hidePricePart() {
        price_part.setVisibility(GONE);
        clearPrices();
    }

    public void setOriginAdress(String s) {
        tv_origin.setText(s);
    }

    public void showSecondDestinationField() {
        second_destination_container.setVisibility(VISIBLE);
    }

    public void hideSecondDestinationField() {
        second_destination_container.setVisibility(GONE);
    }

    public void setdestinationAdress(String s) {
        tv_destination.setText(s);
    }

    public void setSecondDestinationAddress(String s) {
        tv_second_destination.setText(s);
    }

    /*
    public void languageFill() {
        CustomFontApplication.currentLanguage = new ShareLanguage(context).getLanguage();
        if (CustomFontApplication.currentLanguage == CustomFontApplication.LANGUAGE_SELECT.EN) {
            tv_origin_bottom.setText(context.getResources().getString(R.string.origin_en));
            tv_destination_bottom.setText(context.getResources().getString(R.string.destination_en));
            tv_second_bottom.setText(context.getResources().getString(R.string.second_destination_en));
            tv_cost_bottom.setText(context.getResources().getString(R.string.cost_en));
            btn_request.setText(context.getResources().getString(R.string.request_driver_en));
            tv_pament.setText(getResources().getString(R.string.payment_bottom_header_en));
            tv_count_text.setText(getResources().getString(R.string.taxi_count_bottom_header_en));
        } else {
            tv_origin_bottom.setText(context.getResources().getString(R.string.origin));
            tv_destination_bottom.setText(context.getResources().getString(R.string.destination));
            tv_second_bottom.setText(context.getResources().getString(R.string.second_destination));
            tv_cost_bottom.setText(context.getResources().getString(R.string.cost));
            btn_request.setText(context.getResources().getString(R.string.request));
            tv_pament.setText(getResources().getString(R.string.payment_bottom_header));
            tv_count_text.setText(getResources().getString(R.string.taxi_count_bottom_header));
        }
    }
*/
    public void clear() {
        tv_origin.setText("");
        tv_destination.setText("");
        tv_second_destination.setText("");
        tv_price.setText("");
        tv_kilometer.setText("");
        tv_time.setText("");
    }

    public void clearPrices() {
        tv_price.setText("");
        tv_kilometer.setText("");
        tv_time.setText("");
    }

   /* public void Fill(ResponseCost cost) {
        NumberFormat formatter = new DecimalFormat("#0.0");
        if (cost.isTravel_two()) {
            CustomFontApplication.price = CustomFontApplication.lastPrice + cost.getPrice_two();
            tv_price.setText(CustomFontApplication.FormattedPrice(CustomFontApplication.price));
            float km = (float) ((CustomFontApplication.lastKilometerM + (float) (cost.getDistance_two().getValue() / 1000)));
            tv_kilometer.setText(km + "  کیلومتر");
            int time = CustomFontApplication.lastMinute + cost.getDuration_two().getValue();
            tv_time.setText((time / 60) + "  دقیقه");
        } else {
            CustomFontApplication.lastKilometerM = 0;
            CustomFontApplication.lastMinute = 0;
            tv_price.setText(CustomFontApplication.FormattedPrice(cost.getPrice_all()));
            float d = (float) cost.getDistance().getValue() / 1000;
            String x = formatter.format(Math.round(d));
            CustomFontApplication.lastKilometerM = d;
            CustomFontApplication.lastPrice = cost.getPrice_all();
            CustomFontApplication.lastMinute = cost.getDuration().getValue() / 60;
            String s = String.format("%.2f", d);
            tv_kilometer.setText(s + " کیلومتر ");
            tv_time.setText(CustomFontApplication.lastMinute + "  دقیقه");
        }
    }*/

   /* public void Fill2(Price cost) {
        NumberFormat formatter = new DecimalFormat("#0");
        tv_kilometer.setText(cost.dist + "  کیلومتر");
        tv_price.setText(cost.price+ " ریال");
        tv_time.setText(cost.time + "  دقیقه");
    }*/


    public interface BottomClickedListener {
        public void eventOccured(int id);
    }

    public void setOnItemClickLedistener(BottomClickedListener bottomClickedListener) {
        Log.e("navigateclicked", "ActionBar SetOnItemCLick");
        this.bottomClickedListener = bottomClickedListener;
    }

}

package ir.mohammadpour.app.ui.activity;

/**
 * Created by Amin on 2016-10-01.
 */
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.util.TypedValue;

public class CustomTypeFaceSpan extends TypefaceSpan {

    private final Typeface newType;

    Context c;
    public CustomTypeFaceSpan(String family, Typeface type,Context x) {
        super(family);
        newType = type;
        c=x;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }

    private void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);

        int MY_DIP_VALUE = 17; //18dp

        float pixel=  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                MY_DIP_VALUE, c.getResources().getDisplayMetrics());
        paint.setTextSize(pixel);
    }
}
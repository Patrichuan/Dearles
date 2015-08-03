package dear.dearles.custom_classes;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class TextInputLayout extends android.support.design.widget.TextInputLayout {

    public TextInputLayout(Context context) {
        super(context);
    }

    public TextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        if (ViewCompat.isLaidOut(this)) {
            super.onLayout(changed, left, top, right, bottom);
        } else {
            // Workaround for this terrible logic where onLayout gets called before the view is flagged as laid out.
            // The normal TextInputLayout is depending on isLaidOut when onLayout is called and failing the check which prevents initial drawing
            // If there are multiple layout passes this doesn't get broken
            post(new Runnable() {
                @SuppressLint("WrongCall")
                @Override
                public void run() {
                    TextInputLayout.super.onLayout(changed, left, top, right, bottom);
                }
            });
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            EditText editText = (EditText) child;
            String name = getResources().getResourceEntryName(editText.getId());

            /*
            usernametv
            emailtv
            agetv
            passwordtv
            confirmpasswordtv
            */

            if (editText.getText().toString().isEmpty()) {

                // Esto solo deberia lanzarse si tiene texto
                editText.setText("  "); // Esto sube la etiqueta flotante
                super.addView(child, index, params); // y la muestra

                // Y si no tiene no deberia hacer nada
                //editText.setText(""); // Set back to blank to cause the hint to animate in just in case the user sets text
                // This prevents the hint from being drawn over text that is set programmatically before the state is determined
                return;
            }
        }
        super.addView(child, index, params);
    }
}
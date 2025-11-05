package com.pbl.trafficlightsimulator;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.ViewGroup;
import android.util.TypedValue;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_BG_COLOR_RES = "key_bg_color_res";
    private static final String KEY_MESSAGE = "key_message";
    private static final String KEY_TEXT_COLOR_RES = "key_text_color_res";

    private ConstraintLayout rootLayout;
    private TextView statusText;
    private MaterialButton btnYellow;
    private MaterialButton btnRed;
    private MaterialButton btnGreen;
    private View buttonRow;

    private int currentBgColorRes;
    private String currentMessage;
    private int currentTextColorRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.root_layout);
        statusText = findViewById(R.id.status_text);
        btnYellow = findViewById(R.id.btn_yellow);
        btnRed = findViewById(R.id.btn_red);
        btnGreen = findViewById(R.id.btn_green);
        buttonRow = findViewById(R.id.button_row);

        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyState(R.color.yellow, getString(R.string.caution), R.color.black);
            }
        });

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyState(R.color.red, getString(R.string.stop), R.color.white);
            }
        });

        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyState(R.color.green, getString(R.string.go), R.color.white);
            }
        });

        // Adjust top margin of the button row to account for status bar / display cutout (notch)
        ViewCompat.setOnApplyWindowInsetsListener(buttonRow, (v, insets) -> {
            // get top inset for status bars and display cutout
            int topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.displayCutout()).top;
            // extra gap (defined in dimens)
            int extraGap = getResources().getDimensionPixelSize(R.dimen.button_row_extra_top_margin);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            // apply inset + extra gap
            lp.topMargin = topInset + extraGap;
            v.setLayoutParams(lp);
            // return the insets unconsumed so other views can use them if needed
            return insets;
        });

        if (savedInstanceState != null) {
            currentBgColorRes = savedInstanceState.getInt(KEY_BG_COLOR_RES, R.color.background);
            currentMessage = savedInstanceState.getString(KEY_MESSAGE, "");
            currentTextColorRes = savedInstanceState.getInt(KEY_TEXT_COLOR_RES, R.color.white);
            applyState(currentBgColorRes, currentMessage, currentTextColorRes, false);
        } else {
            applyState(R.color.background, "", R.color.white, true);
        }
    }

    private void applyState(int colorRes, String message, int textColorRes) {
        applyState(colorRes, message, textColorRes, true);
    }

    private void applyState(int colorRes, String message, int textColorRes, boolean saveToVars) {
        rootLayout.setBackgroundColor(ContextCompat.getColor(this, colorRes));
        if (message == null || message.isEmpty()) {
            statusText.setVisibility(View.GONE);
        } else {
            statusText.setVisibility(View.VISIBLE);
            statusText.setText(message);
            statusText.setTextColor(ContextCompat.getColor(this, textColorRes));
        }
        if (saveToVars) {
            currentBgColorRes = colorRes;
            currentMessage = message;
            currentTextColorRes = textColorRes;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_BG_COLOR_RES, currentBgColorRes);
        outState.putString(KEY_MESSAGE, currentMessage);
        outState.putInt(KEY_TEXT_COLOR_RES, currentTextColorRes);
    }
}
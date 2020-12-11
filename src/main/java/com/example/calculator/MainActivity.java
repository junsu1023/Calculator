package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    boolean isFirstInput = true;

    ScrollView scrollView;
    TextView resultOperatorTextView;
    TextView resultTextView;
    Button allClearButton, clearEntryButton, decimalButton;
    ImageButton backSpaceButton;
    Button[] numberButton = new Button[10];
    Button[] operatorButton = new Button[5];
    Calculator calculator = new Calculator(new DecimalFormat("###,###.##########"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scroll_view);
        resultOperatorTextView = findViewById(R.id.result_operator_text_view);
        resultTextView = findViewById(R.id.result_text_view);

        allClearButton = findViewById(R.id.all_clear_button);
        clearEntryButton = findViewById(R.id.clear_entry_button);
        backSpaceButton = findViewById(R.id.back_space_button);
        decimalButton = findViewById(R.id.decimal_button);

        for(int i=0; i<numberButton.length; i++){
            numberButton[i] = findViewById(R.id.number_button_0 + i);
        }

        for(int i=0; i<operatorButton.length; i++){
            operatorButton[i] = findViewById(R.id.operator_button_0 + i);
        }

        for(Button button : numberButton){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numberButtononClick(view);
                }
            });
        }

        for(Button button : operatorButton){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operatorButtononClick(view);
                }
            });
        }

        allClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allClearButtononClick(view);
            }
        });

        clearEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearEntryButtononClick(view);
            }
        });

        backSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backSpaceButtononClick(view);
            }
        });

        decimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decimalButtononClick(view);
            }
        });
    }

    private void clearText(){
        isFirstInput = true;
        resultTextView.setTextColor(0xFF666666);
        resultTextView.setText(calculator.getClearInputText());
        resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
    }
    private void decimalButtononClick(View view) {
        if(isFirstInput){
            resultTextView.setTextColor(0xFF000000);
            resultTextView.setText("0.");
            isFirstInput = false;
        }
        else{
            if(!resultTextView.getText().toString().contains("."))
            resultTextView.append(".");
        }
    }

    private void backSpaceButtononClick(View view) {
        if(!isFirstInput) {
            if(resultTextView.getText().toString().length() > 1){
                String getResultString = resultTextView.getText().toString().replace(",", "");
                String subString = getResultString.substring(0, getResultString.length()-1);
                String decimalString = calculator.getDecimalString(subString);
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                    resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getStringSize(decimalString));
                }
                resultTextView.setText(decimalString);
            }
            else{
                clearText();
            }
        }
    }

    private void clearEntryButtononClick(View view) {
        clearText();
    }

    private void allClearButtononClick(View view) {
        calculator.setAllClear();
        resultOperatorTextView.setText(calculator.getOperatorString());
        clearText();
    }

    private void operatorButtononClick(View view) {
        String getResultString = resultTextView.getText().toString();
        String operator = view.getTag().toString();
        String getResult = calculator.getResult(isFirstInput, getResultString, operator);
        String subOperatorString = (String) resultOperatorTextView.getText();
        resultTextView.setText(getResult);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getStringSize(getResult));
        }
        resultOperatorTextView.setText(calculator.getOperatorString());
        isFirstInput = true;
    }

    private void numberButtononClick(View view) {
        if(isFirstInput){
            resultTextView.setTextColor(0xFF000000);
            resultTextView.setText(view.getTag().toString());
            isFirstInput = false;
        }
        else{
            String getResultText = resultTextView.getText().toString().replace(",", "");
            if(getResultText.length() > 14){
                Toast.makeText(getApplicationContext(), "15자리까지 입력가능합니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                getResultText = getResultText + view.getTag().toString();
                String getDecimalString = calculator.getDecimalString(getResultText);
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                    resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, getStringSize(getDecimalString));
                }
                resultTextView.setText(getDecimalString);
            }
        }
    }

    private int getStringSize(String getDecimalString){
        if(getDecimalString.length() > 30) return 25;
        else if(getDecimalString.length() > 25) return 30;
        else if(getDecimalString.length() > 20) return 35;
        else if(getDecimalString.length() > 15) return 40;
        return 50;
    }
}
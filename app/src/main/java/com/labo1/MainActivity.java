package com.labo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView inputTextView, resultTextView;
    private boolean divideByZero = false, isNewInput = true;
    private double currentResult = 0;
    private String currentValue = "", currentOperator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTextView = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.result);
    }


    public void onNumberClick(View view) {
        if (isNewInput) {
            inputTextView.setText("");
            isNewInput = false;
        }

        String number = ((TextView) view).getText().toString();
        String currentText = inputTextView.getText().toString();

        if (!currentText.equals("0")) {
            if (number.equals(",") && !currentValue.contains(".")) {
                currentValue += ".";
                currentText += number;
            } else if (number.equals(",") && currentValue.contains(".")) {
                currentText = currentText;
                currentValue = currentValue;
            } else {
                currentValue += number;
                currentText += number;

            }

        } else {
            currentValue = number;
            currentText = number;
        }


        inputTextView.setText(currentText);
        if (!currentOperator.equals("")) {
            calculateResult(currentValue);
            displayResult(false, divideByZero);
        }
    }

    public void onOperatorClick(View view) {
        String operator = ((TextView) view).getText().toString();
        String inputText = inputTextView.getText().toString();

        if (!isNewInput) {
            if (currentOperator.equals(""))
                currentResult = Double.parseDouble(currentValue);
            currentValue = "";
            currentOperator = operator;
            inputTextView.setText(String.format("%s%s", inputText, operator));
        }

    }

    public void onOperator2Click(View view) {
        String operator = ((TextView) view).getText().toString();
        String inputText = inputTextView.getText().toString();
        switch (operator) {
            case "C":
            case "CE":
                inputTextView.setText("0");
                resultTextView.setText("");
                currentValue = "";
                currentOperator = "";
                currentResult = 0;
                divideByZero = false;
                break;
            case "←":
                if (!inputText.equals("0")) {
                    String sub = inputText.substring(0, inputText.length() - 1);
                    inputTextView.setText(sub.equals("") ? "0" : sub);
                }

                break;
        }
    }

    public void onEqualClick(View view) {
        if (!isNewInput) {
            displayResult(true, divideByZero);
        }
    }


    private void calculateResult(String number) {
        double value = Double.parseDouble(number);
        switch (currentOperator) {
            case "+":
                currentResult += value;
                break;
            case "-":
                currentResult -= value;
                break;
            case "*":
                currentResult *= value;
                break;
            case "÷":
                if (value != 0) {
                    currentResult /= value;
                } else {
//                    isNewInput = true;
                    divideByZero = true;
                }
                break;
        }
    }

    private void displayResult(boolean isClickEqual, boolean divideByZero) {
        if (divideByZero) {
            resultTextView.setText(R.string.handleDivisionByZero);
        } else {
            String[] res = String.valueOf(currentResult).split("\\.");
            System.out.println(Arrays.toString(res));
            String re = "";
            if (res[1].equals("0") || res[1].equals("00")) {
                re = res[0];
            } else {
                re = String.format("%s,%s", res[0], res[1]);
            }
            resultTextView.setText(isClickEqual ? "= " + re : re);
        }

    }
}
package com.labo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText inputTextView, inputTextViewOperation;
    private boolean divideByZero = false, isNewInput = true, onClickEqual = false;
    private double currentResult = 0;
    private String currentOperator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTextView = findViewById(R.id.editText);
        inputTextView.setShowSoftInputOnFocus(false);
        inputTextViewOperation = findViewById(R.id.editTextOperation);
    }


    public void onNumberClick(View view) {
        if (isNewInput) {
            inputTextView.setText("");
            if (onClickEqual) inputTextViewOperation.setText("");
            isNewInput = false;
        }

        String number = ((TextView) view).getText().toString();
        String currentText = inputTextView.getText().toString();

        if (!currentText.equals("0") && !currentText.equals("")) {
            if (!number.equals(",") || !currentText.contains(",")) {
                currentText += number;
            }
        } else {
            if (number.equals(",")) {
                currentText = "0" + number;
            } else {
                currentText = number;
            }
        }
        inputTextView.setText(currentText);
    }

    public void onClickOperatorTwoValue(View view) {
        String operator = ((TextView) view).getText().toString();
        String inputText = inputTextView.getText().toString();
        currentOperator = operator;
        onClickEqual = false;
        if (!isNewInput) {
            currentResult = Double.parseDouble(splitJoinResult(inputText, ",", "."));
            inputTextViewOperation.setText(String.format("%s%s", inputText, operator));
            isNewInput = true;
        } else {
            String re = splitJoinResult(String.valueOf(currentResult), "\\.", ",");
            inputTextViewOperation.setText(String.format("%s%s", re, operator));
        }

    }


    public void onClickOperatorOneValue(View view) {
        String operator = ((TextView) view).getText().toString();
        String inputText = inputTextView.getText().toString();
        double value = Double.parseDouble(splitJoinResult(inputText, ",", "."));
        switch (operator) {
            case "±":
                currentResult = value * (-1);
                inputTextView.setText(splitJoinResult(String.valueOf(currentResult), "\\.", ","));
                break;
            case "√":
                if (value > 0) {
                    currentResult = Math.sqrt(value);
                    inputTextView.setText(splitJoinResult(String.valueOf(currentResult), "\\.", ","));
                } else {
                    inputTextView.setText(R.string.dataNotValid);
                }
                break;
            case "%":
                currentResult = value % 2;
                inputTextView.setText(splitJoinResult(String.valueOf(currentResult), "\\.", ","));
                break;
            case "1/x":
                currentResult = 1 / value;
                inputTextView.setText(splitJoinResult(String.valueOf(currentResult), "\\.", ","));
                break;
        }
        isNewInput = true;
    }


    public void onClickOperatorClean(View view) {
        String operator = ((TextView) view).getText().toString();
        String inputText = inputTextView.getText().toString();
        switch (operator) {
            case "C":
            case "CE":
                inputTextView.setText("0");
                inputTextViewOperation.setText("");
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
            calculateResult();
            displayResult(divideByZero);
            isNewInput = true;
            onClickEqual = true;
        }
    }


    private void calculateResult() {
        double currentValue = Double.parseDouble(splitJoinResult(inputTextView.getText().toString(), ",", "."));
        switch (currentOperator) {
            case "+":
                currentResult += currentValue;
                break;
            case "-":
                currentResult -= currentValue;
                break;
            case "*":
                currentResult *= currentValue;
                break;
            case "÷":
                if (currentValue != 0) {
                    currentResult /= currentValue;
                } else {
                    isNewInput = true;
                    divideByZero = true;
                }
                break;
        }
    }

    private String splitJoinResult(String result, String splitOperator, String joinOperator) {
        String[] res = result.split(splitOperator);
        System.out.println(Arrays.toString(res));
        String re = result;
        if (res.length >= 2) {
            if (res[1].equals("0") || res[1].equals("00")) {
                re = res[0];
            } else {
                re = String.format("%s%s%s", res[0], joinOperator, res[1]);
            }
        }
        return re;
    }

    private void displayResult(boolean divideByZero) {
        if (divideByZero) {
            inputTextView.setText(R.string.handleDivisionByZero);
        } else {
            String re = splitJoinResult(String.valueOf(currentResult), "\\.", ",");
            inputTextViewOperation.setText(String.format("%s%s=", inputTextViewOperation.getText().toString(), inputTextView.getText().toString()));
            inputTextView.setText(re);
        }

    }
}
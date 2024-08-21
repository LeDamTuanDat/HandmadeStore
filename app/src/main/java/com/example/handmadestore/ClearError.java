package com.example.handmadestore;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ClearError implements TextWatcher {

    private TextInputEditText editText;
    private TextInputLayout inputLayout;

    public ClearError(TextInputEditText editText, TextInputLayout inputLayout){
        this.editText = editText;
        this.inputLayout = inputLayout;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputLayout.setError("");
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

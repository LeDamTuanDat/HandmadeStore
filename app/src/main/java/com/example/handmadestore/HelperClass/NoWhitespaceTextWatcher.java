package com.example.handmadestore.HelperClass;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NoWhitespaceTextWatcher implements TextWatcher {

    private EditText editText;

    public NoWhitespaceTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String input = charSequence.toString();
        if (input.contains(" ")) {
            editText.setText(input.replace(" ", ""));
            editText.setSelection(editText.getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

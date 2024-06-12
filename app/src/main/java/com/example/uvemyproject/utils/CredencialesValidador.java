package com.example.uvemyproject.utils;

import android.text.TextUtils;

public class CredencialesValidador {
    public static boolean esEmailValido(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}

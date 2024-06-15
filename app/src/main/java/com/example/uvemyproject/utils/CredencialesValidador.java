package com.example.uvemyproject.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CredencialesValidador {
    public static boolean esEmailValido(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean esContrasenaSegura(String contrasena) {
        boolean esContrasenaSegura;
        String patron = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$";

        long tiempoLimite = 500;

        try {
            Pattern pattern = Pattern.compile(patron);
            Matcher matcher = pattern.matcher(contrasena);

            long startTime = System.currentTimeMillis();
            esContrasenaSegura = matcher.find();
            long endTime = System.currentTimeMillis();

            if ((endTime - startTime) > tiempoLimite) {
                throw new PatternSyntaxException("Time limit exceeded", patron, -1);
            }
        } catch (PatternSyntaxException e) {
            esContrasenaSegura = false;
        }

        return esContrasenaSegura;
    }
}

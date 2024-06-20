package com.example.uvemyproject.interfaces;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public interface INotificacionReciboVideo {
    void notificarReciboExitoso(ByteArrayOutputStream output);
    void notificarReciboFallido();
}

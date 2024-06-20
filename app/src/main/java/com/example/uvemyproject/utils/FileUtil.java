package com.example.uvemyproject.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    public static File uriToFile(Context context, Uri uri, String nombre) {
        Log.i("nombre", uri.toString());
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = new File(context.getCacheDir(), nombre + ".pdf");
            inputStream = context.getContentResolver().openInputStream(uri);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String eliminarExtensionNombre(String filename) {
        if (filename == null || filename.isEmpty()) {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + ".*";
        return filename.replaceAll(extPattern, "");
    }

    public static File convertirAFileArrayByte(Context context, byte[] byteArray) {
        FileOutputStream fileOutputStream = null;
        File videoFile = null;
        try {
            File cacheDir = context.getCacheDir();
            videoFile = new File(cacheDir, "video.mp4");
            fileOutputStream = new FileOutputStream(videoFile);

            fileOutputStream.write(byteArray);
            fileOutputStream.flush();

            Log.v("FileWrite", "Archivo escrito con éxito: " + videoFile.getAbsolutePath());

        } catch (IOException e) {
            Log.e("FileWrite", "Error escribiendo el archivo: " + e.getMessage(), e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.e("FileWrite", "Error cerrando FileOutputStream: " + e.getMessage(), e);
                }
            }
        }
        return videoFile;
    }
}

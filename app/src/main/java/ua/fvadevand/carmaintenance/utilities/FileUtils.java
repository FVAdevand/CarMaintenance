package ua.fvadevand.carmaintenance.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {
    private static final String LOG_TAG = FileUtils.class.getSimpleName();

    private FileUtils() {
    }

    public static File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File externalStorage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Users avatar");
        if (!externalStorage.mkdirs()) {
            Log.i(LOG_TAG, "createImageFile: can't create dir " + externalStorage);
        }
        File image = new File(externalStorage, imageFileName);
        if (!image.createNewFile()) {
            Log.i(LOG_TAG, "createImageFile: can't create file " + image);
        }

        return image;
    }
}

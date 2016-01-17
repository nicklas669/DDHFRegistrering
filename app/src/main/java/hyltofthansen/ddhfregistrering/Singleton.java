package hyltofthansen.ddhfregistrering;

import android.app.Application;

/**
 * Created by hylle on 16-01-2016.
 */
public class Singleton extends Application {

    private static Singleton firstInstance = null;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if (firstInstance == null) {
            firstInstance = new Singleton();
        }
        return firstInstance;
    }

}

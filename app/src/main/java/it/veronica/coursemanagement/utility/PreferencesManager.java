package it.veronica.coursemanagement.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static PreferencesManager instance = null;
    private static SharedPreferences sharedpreferences = null;

    public static PreferencesManager getInstance(String preferences,Context ctx)
    {
        if (instance == null)
        {
            //Creo l'oggetto
            instance = new PreferencesManager();
            sharedpreferences = ctx.getSharedPreferences(preferences, 0);
        }
        return instance;
    }

    public String GetPreferenceByKey(String key)
    {
        return sharedpreferences.getString(key, null);
    }

    public void PutPreferenceByKey(String key, String value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
}
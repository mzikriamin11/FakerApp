package id.ac.kampusmelayu.amin.zikri.fakerapp.data.preferences

import android.content.Context
import id.ac.kampusmelayu.amin.zikri.fakerapp.util.Constants

class SettingsManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    var darkMode: Boolean
        get() = prefs.getBoolean(Constants.PREF_DARK_MODE, false)
        set(value) {
            prefs.edit().putBoolean(Constants.PREF_DARK_MODE, value).apply()
        }

    var defaultGender: String
        get() = prefs.getString(Constants.PREF_DEFAULT_GENDER, Constants.GENDER_ALL)
            ?: Constants.GENDER_ALL
        set(value) {
            prefs.edit().putString(Constants.PREF_DEFAULT_GENDER, value).apply()
        }

    var quantity: Int
        get() = prefs.getInt(Constants.PREF_QUANTITY, 10)
        set(value) {
            prefs.edit().putInt(Constants.PREF_QUANTITY, value).apply()
        }
}

package id.ac.kampusmelayu.amin.zikri.fakerapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import id.ac.kampusmelayu.amin.zikri.fakerapp.data.preferences.SettingsManager

class FakerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Terapkan tema sesuai pilihan user di Settings
        val sm = SettingsManager(this)
        val mode = if (sm.darkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}

package com.example.menupreferenceapp

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var etName: EditText
    private lateinit var switchNotifications: Switch
    private lateinit var tvResult: TextView

    private val prefs by lazy {
        getSharedPreferences("MyPrefs", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        switchNotifications =
            findViewById(R.id.switchNotifications)
        tvResult = findViewById(R.id.tvResult)

        // Load previously saved preferences
        etName.setText(prefs.getString("username", ""))
        switchNotifications.isChecked =
            prefs.getBoolean("notifications", false)

        showSavedPreferences()

        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val notifications =
                switchNotifications.isChecked

            // Save values permanently on this device
            prefs.edit()
                .putString("username", name)
                .putBoolean("notifications", notifications)
                .apply()

            showSavedPreferences()

            Toast.makeText(
                this,
                "Preferences saved successfully",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showSavedPreferences() {
        val name = prefs.getString("username", "Not set")
        val notifications =
            prefs.getBoolean("notifications", false)

        tvResult.text = """
            Saved name: $name
            Notifications: ${
                if (notifications) "Enabled" else "Disabled"
            }
        """.trimIndent()
    }

    // Display the three-dot options menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_home -> {
                showSavedPreferences()
                true
            }

            R.id.menu_clear -> {
                prefs.edit().clear().apply()

                etName.setText("")
                switchNotifications.isChecked = false
                showSavedPreferences()

                Toast.makeText(
                    this,
                    "Preferences cleared",
                    Toast.LENGTH_SHORT
                ).show()

                true
            }

            R.id.menu_about -> {
                Toast.makeText(
                    this,
                    "Menu and SharedPreferences Demo",
                    Toast.LENGTH_LONG
                ).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

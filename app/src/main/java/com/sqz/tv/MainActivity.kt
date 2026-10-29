package com.sqz.tv

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private val handler = Handler(Looper.getMainLooper())

    private val clockTick = object : Runnable {
        override fun run() {
            val now = Date()
            val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
            val fecha = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es")).format(now)

            findViewById<TextView>(R.id.clock).text = hora
            findViewById<TextView>(R.id.date).text =
                fecha.replaceFirstChar { it.uppercase() }

            handler.postDelayed(this, 30_000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler.post(clockTick)

        val lista = findViewById<LinearLayout>(R.id.appsList)
        getInstalledApps().forEach { app ->
            lista.addView(crearBoton(app))
        }
        // Botón de Ajustes del sistema al final
        lista.addView(crearBoton(App("⚙  Ajustes", "settings", null)))
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(clockTick)
    }

    private data class App(
        val nombre: String,
        val paquete: String,
        val icono: Drawable?
    )

    private fun getInstalledApps(): List<App> {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val apps: MutableList<ResolveInfo> = pm.queryIntentActivities(intent, 0)

        return apps
            .filter { it.activityInfo.packageName != packageName }
            .map { app ->
                App(
                    nombre = app.loadLabel(pm).toString(),
                    paquete = app.activityInfo.packageName,
                    icono = app.loadIcon(pm)
                )
            }
            .distinctBy { it.paquete }
            .sortedBy { it.nombre.lowercase(Locale.getDefault()) }
    }

    private fun crearBoton(app: App): TextView = TextView(this).apply {
        text = app.nombre
        gravity = Gravity.CENTER
        textSize = 17f
        setTextColor(0xFFF2F2F2.toInt())
        setPadding(40, 48, 40, 48)
        background = resources.getDrawable(R.drawable.item_fondo, theme)

        app.icono?.let { icono ->
            icono.setBounds(0, 0, 128, 128)
            setCompoundDrawables(null, icono, null, null)
            compoundDrawablePadding = 20
        }

        setOnClickListener {
            when (app.paquete) {
                "settings" -> startActivity(Intent(Settings.ACTION_SETTINGS))
                else -> packageManager
                    .getLaunchIntentForPackage(app.paquete)
                    ?.let(::startActivity)
            }
        }
    }
}

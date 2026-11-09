package com.sqz.tv

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private data class App(val nombre: String, val paquete: String, val icono: Drawable?)

    private val handler = Handler(Looper.getMainLooper())
    private var seccion = "Todas"
    private var restoDeApps: List<App> = emptyList()

    private val clockTick = object : Runnable {
        override fun run() {
            val ahora = Date()
            findViewById<TextView>(R.id.clock).text =
                SimpleDateFormat("h:mm", Locale("es")).format(ahora)

            val hora = SimpleDateFormat("H", Locale("es")).format(ahora).toIntOrNull() ?: 12
            val saludo = when (hora) {
                in 5..11 -> "☀️  Buenos días"
                in 12..18 -> "🌤️  Buenas tardes"
                else -> "🌙  Buenas noches"
            }
            findViewById<TextView>(R.id.saludo).text = saludo

            findViewById<TextView>(R.id.date).text =
                SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es"))
                    .format(ahora)
                    .replaceFirstChar { it.uppercase() }

            handler.postDelayed(this, 20_000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler.post(clockTick)

        val todas = getInstalledApps()

        // ─── FILA DE FAVORITOS ───
        val favRow = findViewById<LinearLayout>(R.id.favRow)
        val favoritos = ArrayList<App>()
        for (palabra in FAVORITOS) {
            val encontrada = todas.firstOrNull { app ->
                app.nombre.lowercase(Locale.getDefault()).contains(palabra) ||
                    app.paquete.lowercase(Locale.getDefault()).contains(palabra)
            }
            if (encontrada != null && favoritos.none { it.paquete == encontrada.paquete }) {
                favoritos.add(encontrada)
            }
        }
        favoritos.add(App("Ajustes", "settings", null))
        favoritos.forEach { favRow.addView(crearTarjeta(it, grande = true)) }

        // Apps restantes (las favoritas no se repiten en las secciones)
        restoDeApps = todas.filter { app -> favoritos.none { it.paquete == app.paquete } }

        // ─── PESTAÑAS DE SECCIONES ───
        val tabsRow = findViewById<LinearLayout>(R.id.tabsRow)
        SECCIONES.forEach { tabsRow.addView(crearPestana(it)) }
        pintarPestanas()
        renderGrid()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(clockTick)
    }

    private companion object {
        val FAVORITOS = listOf(
            "xuper",
            "youtube",
            "play store",
            "vending",
            "apkpure",
            "downloader",
            "open browser",
            "browser lite"

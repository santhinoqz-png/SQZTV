package com.sqz.tv

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Typeface
import android.graphics.drawable.Drawable
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

        // ─── FILAS DE APPS (5 por fila, construidas igual que Favoritos) ───
        val resto = todas.filter { app -> favoritos.none { it.paquete == app.paquete } }
        val contenedor = findViewById<LinearLayout>(R.id.gridContainer)

        if (resto.isEmpty()) {
            contenedor.addView(TextView(this).apply {
                text = "No hay más apps instaladas por ahora"
                textSize = 15f
                setTextColor(0xFF8E8E93.toInt())
                setPadding(0, dp(16), 0, 0)
            })
        } else {
            resto.chunked(5).forEach { fila ->
                val filaLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                }
                fila.forEach { app ->
                    filaLayout.addView(crearTarjeta(app, grande = false))
                }
                contenedor.addView(filaLayout)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(clockTick)
    }

    // Palabras clave para detectar tus favoritas (por nombre o paquete).
    // ¿Cambiar las favoritas? Edita esta lista.
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
        )
    }

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

    private fun crearTarjeta(app: App, grande: Boolean): TextView {
        return TextView(this).apply {
            text = app.nombre
            gravity = Gravity.CENTER
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
            textSize = if (grande) 15f else 13f
            setTextColor(0xFFEDEDF2.toInt())
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            setPadding(dp(22), dp(30), dp(22), dp(22))
            background = resources.getDrawable(R.drawable.card_fondo, theme)

            app.icono?.let { icono ->
                val tam = if (grande) dp(92) else dp(64)
                icono.setBounds(0, 0, tam, tam)
                setCompoundDrawables(null, icono, null, null)
                compoundDrawablePadding = dp(20)
            }

            layoutParams = if (grande) {
                LinearLayout.LayoutParams(dp(210), dp(250)).apply {
                    marginEnd = dp(20)
                }
            } else {
                LinearLayout.LayoutParams(dp(190), dp(240)).apply {
                    marginEnd = dp(18)
                    bottomMargin = dp(18)
                }
            }
            minimumWidth = if (grande) dp(200) else dp(180)
            minimumHeight = if (grande) dp(240) else dp(230)

            setOnFocusChangeListener { v, enfocado -> animarFoco(v, enfocado) }
            setOnClickListener { lanzar(app) }
        }
    }

    // Efecto Apple TV: la tarjeta enfocada crece con suavidad y se eleva
    private fun animarFoco(v: View, enfocado: Boolean) {
        val escala = if (enfocado) 1.08f else 1f
        v.animate()
            .scaleX(escala)
            .scaleY(escala)
            .setDuration(140L)
            .start()
        v.elevation = if (enfocado) dp(14).toFloat() else 0f
    }

    private fun lanzar(app: App) {
        when {
            app.paquete == "settings" ->
                startActivity(Intent(Settings.ACTION_SETTINGS))
            else ->
                packageManager.getLaunchIntentForPackage(app.paquete)
                    ?.let(::startActivity)
        }
    }

    private fun dp(n: Int): Int = (n * resources.displayMetrics.density).toInt()
}

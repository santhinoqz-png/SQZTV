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
                in 5..11 -> "\u2600\uFE0F  Buenos d\u00edas"
                in 12..18 -> "\uD83C\uDF24\uFE0F  Buenas tardes"
                else -> "\uD83C\uDF19  Buenas noches"
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
        )

        val SECCIONES = listOf(
            "Todas",
            "Juegos",
            "TV y video",
            "M\u00fasica",
            "Redes",
            "Herramientas",
            "Otros"
        )

        val JUEGOS = listOf("game", "juego")
        val VIDEO = listOf(
            "youtube", "xuper", "netflix", "prime", "disney", "hbo", "max", "video",
            "v\u00eddeo", "cine", "movie", "pel\u00edcula", "iptv", "plex", "twitch",
            "dazn", "paramount", "crackle", "pluto", "movistar", "claro"
        )
        val MUSICA = listOf("spotify", "music", "m\u00fasica", "audio", "radio", "podcast")
        val REDES = listOf(
            "whatsapp", "facebook", "instagram", "telegram", "tiktok", "twitter",
            "discord", "snap"
        )
        val HERRAMIENTAS = listOf(
            "downloader", "browser", "chrome", "firefox", "file", "archivo",
            "explorador", "cleaner", "store", "apkpure", "vending", "install",
            "instalad", "ajuste", "setting"
        )
    }

    private fun categorizar(app: App): String {
        val texto = (app.nombre + " " + app.paquete)
            .lowercase(Locale.getDefault())
        return when {
            JUEGOS.any { texto.contains(it) } -> "Juegos"
            VIDEO.any { texto.contains(it) } -> "TV y video"
            MUSICA.any { texto.contains(it) } -> "M\u00fasica"
            REDES.any { texto.contains(it) } -> "Redes"
            HERRAMIENTAS.any { texto.contains(it) } -> "Herramientas"
            else -> "Otros"
        }
    }

    private fun crearPestana(nombre: String): TextView = TextView(this).apply {
        text = nombre
        textSize = 15f
        gravity = Gravity.CENTER
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        setPadding(dp(30), dp(14), dp(30), dp(14))
        minimumWidth = dp(80)
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { marginEnd = dp(12) }
        setOnClickListener {
            seccion = nombre
            pintarPestanas()
            renderGrid()
        }
        setOnFocusChangeListener { v, enfocado -> animarFoco(v, enfocado) }
    }

    private fun fondoPestana(activa: Boolean): GradientDrawable =
        GradientDrawable().apply {
            cornerRadius = dp(44).toFloat()
            setColor(if (activa) 0xFF1D1D1F.toInt() else 0xFFFFFFFF.toInt())
            if (!activa) {
                setStroke(dp(1), 0xFFE3E3E8.toInt())
            }
        }

    private fun pintarPestanas() {
        val row = findViewById<LinearLayout>(R.id.tabsRow)
        for (i in 0 until row.childCount) {
            val chip = row.getChildAt(i) as TextView
            val activa = chip.text == seccion
            chip.background = fondoPestana(activa)
            chip.setTextColor(
                if (activa) 0xFFFFFFFF.toInt() else 0xFF6D6D72.toInt()
            )
            chip.elevation = (if (activa) dp(6) else dp(2)).toFloat()
        }
    }

    private fun renderGrid() {
        findViewById<TextView>(R.id.tituloApps).text = seccion
        val contenedor = findViewById<LinearLayout>(R.id.gridContainer)
        contenedor.removeAllViews()

        val lista = if (seccion == "Todas") {
            restoDeApps
        } else {
            restoDeApps.filter { categorizar(it) == seccion }
        }

        if (lista.isEmpty()) {
            contenedor.addView(TextView(this).apply {
                text = "Todav\u00eda no hay apps en esta secci\u00f3n"
                textSize = 15f
                setTextColor(0xFF86868B.toInt())
                setPadding(0, dp(16), 0, 0)
            })
            return
        }

        lista.chunked(5).forEach { fila ->
            val filaLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }
            fila.forEach { app ->
                filaLayout.addView(crearTarjeta(app, grande = false))
            }
            contenedor.addView(filaLayout)
        }
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
            setTextColor(0xFF1D1D1F.toInt())
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            setPadding(dp(22), dp(30), dp(22), dp(22))
            background = resources.getDrawable(R.drawable.card_fondo, theme)
            elevation = dp(4).toFloat()

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

    private fun animarFoco(v: View, enfocado: Boolean) {
        val escala = if (enfocado) 1.06f else 1f
        v.animate()
            .scaleX(escala)
            .scaleY(escala)
            .setDuration(140L)
            .start()
        v.elevation = (if (enfocado) dp(16) else dp(4)).toFloat()
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

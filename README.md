# 📺 SQZ TV — Launcher para TV Box

Sistema de pantalla de inicio personalizado para TV Box Android, hecho en Kotlin.

Al instalarlo, el TV Box lo detecta como **pantalla de inicio** y puedes
elegir que arranque siempre con SQZ TV.

---

## ✅ Paso 1 — Instalar Android Studio

1. Descárgalo de: **https://developer.android.com/studio**
2. Instala con las opciones por defecto (Siguiente → Siguiente → Finalizar)
3. Ya incluye todo lo necesario (SDK, emulador, etc.)

## ✅ Paso 2 — Abrir el proyecto

1. Abre **Android Studio**
2. Clic en **Open** (Abrir)
3. Selecciona la carpeta: `C:\Users\santhinoqz\SQZTV`
4. Espera a que termine de sincronizar (la primera vez descarga Gradle,
   puede tardar **5–15 minutos** según tu internet)

   > ⚠️ Si aparece un error de "Gradle wrapper", haz clic en
   > **File → Sync Project with Gradle Files** y espera.

## ✅ Paso 3 — Generar el APK

1. Menú **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Cuando termine, aparece un aviso: clic en **locate**
3. El archivo está en:
   ```
   app\build\outputs\apk\debug\app-debug.apk
   ```

## ✅ Paso 4 — Instalarlo en el TV Box

**Opción A — Copia fácil (portátil/USB):**
1. Copia `app-debug.apk` a un USB o envíalo por Bluetooth/almacenamiento
2. En el TV Box, abre el APK (si no tienes instalador de APK, descarga
   "Files by Google" o cualquier explorador de archivos desde la tienda)
3. Acepta "Instalar de fuentes desconocidas"

**Opción B — Por red (ADB, más pro):**
1. En el TV Box: Ajustes → Opciones del desarrollador → activa
   **Depuración por USB/Red**
2. En Android Studio, abre el **Terminal** y escribe:
   ```
   adb connect IP-DEL-TVBOX
   adb install app-debug.apk
   ```

## ✅ Paso 5 — Hacerlo tu pantalla de inicio

1. Presiona el botón **Home** del control remoto del TV Box
2. Aparecerá una ventana: *"¿Qué app usas siempre para esta acción?"*
3. Elige **SQZ TV** y marca **"Siempre"**

🎉 ¡Listo! El TV Box arrancará con SQZ TV.

---

## 🧩 ¿Qué trae?

- ✅ Reloj grande y fecha en español (se actualiza solo)
- ✅ Lista de todas las apps instaladas con su ícono real
- ✅ Navegación con el control remoto (flechas + OK)
- ✅ Botón de ⚙ Ajustes del sistema
- ✅ Se enfoca en azul cuando navegas con el control

## 🎨 ¿Qué personalizar después?

| Quiero cambiar... | Archivo |
|---|---|
| Color de fondo | `app/src/main/res/layout/activity_main.xml` (línea `background="#0B0F1A"`) |
| Nombre "SQZ TV" | `app/src/main/res/values/strings.xml` |
| Colores al enfocar | `app/src/main/res/drawable/item_fondo.xml` |
| Tamaño del texto/reloj | `activity_main.xml` (`textSize`) |
| Mostrar solo apps específicas | `MainActivity.kt` (función `getInstalledApps()`) |

## 🚀 Ideas para la versión 2

- Layout tipo **grilla** (grid) de apps en vez de lista
- Fondo personalizado con tu logo/foto
- Widget de clima
- Acceso directo a YouTube/Netflix con canales favoritos
- Modo "niños" con apps permitidas

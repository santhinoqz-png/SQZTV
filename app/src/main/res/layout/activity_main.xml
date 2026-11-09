<?xml version="1.0" encoding="utf-8"?>
<!-- Pantalla SQZ TV estilo Apple claro:
     panel con reloj + saludo, favoritos, pestañas de secciones
     y apps en filas construidas por código -->
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/gradient_fondo"
    android:fillViewport="true"
    android:scrollbars="none"
    android:overScrollMode="never">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingLeft="56dp"
        android:paddingTop="36dp"
        android:paddingRight="56dp"
        android:paddingBottom="40dp">

        <!-- PANEL con reloj y saludo -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@drawable/glass_panel"
            android:gravity="center_vertical"
            android:orientation="horizontal"
            android:paddingLeft="48dp"
            android:paddingTop="28dp"
            android:paddingRight="48dp"
            android:paddingBottom="28dp">

            <TextView
                android:id="@+id/clock"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:fontFamily="sans-serif-light"
                android:textSize="76sp"
                android:textColor="#1D1D1F" />

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="44dp"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/saludo"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="24sp"
                    android:textColor="#48484D" />

                <TextView
                    android:id="@+id/date"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:textSize="18sp"
                    android:textColor="#86868B" />

            </LinearLayout>
        </LinearLayout>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="44dp"
            android:layout_marginLeft="8dp"
            android:text="Favoritos"
            android:textSize="20sp"
            android:textColor="#6D6D72" />

        <HorizontalScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="14dp"
            android:scrollbars="none"
            android:overScrollMode="never">

            <LinearLayout
                android:id="@+id/favRow"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:paddingBottom="6dp" />

        </HorizontalScrollView>

        <!-- Pestañas de secciones -->
        <HorizontalScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="40dp"
            android:scrollbars="none"
            android:overScrollMode="never">

            <LinearLayout
                android:id="@+id/tabsRow"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:paddingBottom="6dp" />

        </HorizontalScrollView>

        <TextView
            android:id="@+id/tituloApps"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="18dp"
            android:layout_marginLeft="8dp"
            android:textSize="20sp"
            android:textColor="#6D6D72" />

        <!-- Las filas de apps se crean por código -->
        <LinearLayout
            android:id="@+id/gridContainer"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="14dp"
            android:orientation="vertical" />

    </LinearLayout>

</ScrollView>

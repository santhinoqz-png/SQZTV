package com.sqz.tv

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

// GridView propia del proyecto (por si luego queremos personalizarla).
// La cuadrícula hace su propio scroll dentro del espacio que le sobra
// de la pantalla, así las apps siempre se ven.
class FullGridView(context: Context, attrs: AttributeSet) : GridView(context, attrs)

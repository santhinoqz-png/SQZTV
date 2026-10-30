package com.sqz.tv

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

// GridView que se expande a su altura completa dentro del ScrollView,
// para que la cuadrícula de apps se vea entera al hacer scroll
class FullGridView(context: Context, attrs: AttributeSet) : GridView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        )
    }
}


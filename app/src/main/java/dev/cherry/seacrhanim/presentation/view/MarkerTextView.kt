package dev.cherry.seacrhanim.presentation.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.support.v4.content.res.ResourcesCompat
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import dev.cherry.seacrhanim.R

/**
 * Extended TextView that may be used as google maps marker icon. Call [asBitmapDescriptor]
 * to convert view to marker icon
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 * @since 22.02.2017
 */
class MarkerTextView(context: Context) : TextView(context) {

    // default style constants
    private val DEFAULT_MAX_LINES = 1
    private val DEFAULT_MAX_TEXT_LENGTH = 15
    private val DEFAULT_TEXT_SIZE = 12f

    init {
        // setup default style
        background = ResourcesCompat.getDrawable(resources, R.drawable.bg_city_name_marker, null)
        filters = arrayOf(android.text.InputFilter.LengthFilter(DEFAULT_MAX_TEXT_LENGTH))
        ellipsize = TextUtils.TruncateAt.END
        maxLines = DEFAULT_MAX_LINES
        setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE)
        setTextColor(Color.WHITE)
        setAllCaps(true)
    }

    /**
     * Convert view to [BitmapDescriptor] for able to use as Google Map marker icon
     *
     * @return [BitmapDescriptor] for view
     */
    fun asBitmapDescriptor(): BitmapDescriptor {
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        layout(0, 0, measuredWidth, measuredHeight)
        buildDrawingCache()

        // convert view to bitmap and return
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
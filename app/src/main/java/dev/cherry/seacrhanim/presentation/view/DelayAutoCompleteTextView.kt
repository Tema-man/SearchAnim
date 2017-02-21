package dev.cherry.seacrhanim.presentation.view

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar


/**
 * Extension of android [AutoCompleteTextView] with delay between request and text changing.
 * Also can manage progress bar associated with this view requests
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
class DelayAutoCompleteTextView(context: Context, attrs: AttributeSet) : AutoCompleteTextView(context, attrs) {

    // Handler message code
    private val MESSAGE_TEXT_CHANGED = 100

    // default delay value
    private val DEFAULT_AUTOCOMPLETE_DELAY = 250L

    // delay value
    var delay = DEFAULT_AUTOCOMPLETE_DELAY

    // progress bar
    var progressBar: ProgressBar? = null

    // handler for send text changed messages
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super@DelayAutoCompleteTextView.performFiltering(msg.obj as CharSequence, msg.arg1)
        }
    }

    /**
     * Intercepts filtering event for [AutoCompleteTextView]
     * @see [AutoCompleteTextView.performFiltering]
     */
    override fun performFiltering(text: CharSequence, keyCode: Int) {
        //show progressbar
        if (progressBar != null) {
            progressBar?.visibility = View.VISIBLE
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED)
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), delay)
    }

    /**
     * Filter complete callback
     * @see [AutoCompleteTextView.onFilterComplete]
     */
    override fun onFilterComplete(count: Int) {
        // hide progressbar
        if (progressBar != null) {
            progressBar?.visibility = View.GONE
        }
        super.onFilterComplete(count)
    }
}
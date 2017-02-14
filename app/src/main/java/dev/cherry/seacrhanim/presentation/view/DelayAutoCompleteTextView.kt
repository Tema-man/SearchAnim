package dev.cherry.seacrhanim.presentation.view

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar


/**
 * @author Artemii Vishnevskii
 * @since 14.02.2017.
 */
class DelayAutoCompleteTextView(context: Context, attrs: AttributeSet) : AutoCompleteTextView(context, attrs) {

    private val MESSAGE_TEXT_CHANGED = 100
    private val DEFAULT_AUTOCOMPLETE_DELAY = 250

    private var mDelay: Long = DEFAULT_AUTOCOMPLETE_DELAY.toLong()
    private var mProgressBar: ProgressBar? = null

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super@DelayAutoCompleteTextView.performFiltering(msg.obj as CharSequence, msg.arg1)
        }
    }

    fun setProgressBar(progressBar: ProgressBar) {
        mProgressBar = progressBar
    }

    fun setAutoCompleteDelay(autoCompleteDelay: Long) {
        mDelay = autoCompleteDelay
    }

    override fun performFiltering(text: CharSequence, keyCode: Int) {
        if (mProgressBar != null) {
            mProgressBar!!.visibility = View.VISIBLE
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED)
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mDelay)
    }

    override fun onFilterComplete(count: Int) {
        if (mProgressBar != null) {
            mProgressBar!!.visibility = View.GONE
        }
        super.onFilterComplete(count)
    }
}
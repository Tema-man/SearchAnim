package dev.cherry.seacrhanim

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AutoCompleteTextView

class MainActivity : AppCompatActivity() {

    private var mFromText: AutoCompleteTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFromText = findViewById(R.id.fromText) as AutoCompleteTextView
    }
}

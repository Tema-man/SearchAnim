package dev.cherry.seacrhanim.presentation.main

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Filter
import android.widget.ProgressBar
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import dev.cherry.seacrhanim.R
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.presentation.view.DelayAutoCompleteTextView

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mPresenter: MainPresenter
    lateinit var mFromText: DelayAutoCompleteTextView
    lateinit var mFromProgress: ProgressBar
    lateinit var mToText: DelayAutoCompleteTextView
    lateinit var mToProgress: ProgressBar
    lateinit var mCitiesAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViews()
    }

    fun prepareViews() {
        // toolbar setup
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

        // cities adapter setup
        mCitiesAdapter = CitiesAdapter(mFilter)

        // from field setup
        mFromProgress = findViewById(R.id.fromProgress) as ProgressBar
        mFromText = findViewById(R.id.fromText) as DelayAutoCompleteTextView
        mFromText.setAdapter(mCitiesAdapter)
        mFromText.setProgressBar(mFromProgress)

        // to field setup
        mToProgress = findViewById(R.id.toProgress) as ProgressBar
        mToText = findViewById(R.id.toText) as DelayAutoCompleteTextView
        mToText.setAdapter(mCitiesAdapter)
        mToText.setProgressBar(mToProgress)
    }

    override fun showError(t: Throwable?) {
        Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
    }

    val mFilter = object : Filter() {
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (0 != results?.count) {
                @Suppress("UNCHECKED_CAST")
                mCitiesAdapter.setData(results?.values as List<City>)
                mCitiesAdapter.notifyDataSetChanged()
            } else {
                mCitiesAdapter.notifyDataSetInvalidated()
            }
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults: FilterResults = FilterResults()
            val cities: List<City> = mPresenter.getCities(constraint as String, "ru")

            filterResults.values = cities
            filterResults.count = cities.size

            return filterResults
        }
    }
}

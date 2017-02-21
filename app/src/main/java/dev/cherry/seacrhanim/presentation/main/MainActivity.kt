package dev.cherry.seacrhanim.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import dev.cherry.seacrhanim.R
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.presentation.map.MapActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Main screen class. Subclass of moxy's [MvpAppCompatActivity]. Supports MVP.
 * Implementation of [MainView]
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mPresenter: MainPresenter
    lateinit var mCitiesAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViews()
    }

    /** Prepare activity views for usage */
    fun prepareViews() {
        // toolbar setup
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

        // cities adapter setup
        mCitiesAdapter = CitiesAdapter(mFilter)

        // from field setup
        fromText.setAdapter(mCitiesAdapter)
        fromText.progressBar = fromProgress
        fromText.setOnItemClickListener { adapterView, view, i, l ->
            mPresenter.sourceSelected(adapterView.getItemAtPosition(i) as City)
        }

        // to field setup
        toText.setAdapter(mCitiesAdapter)
        toText.progressBar = toProgress
        toText.setOnItemClickListener { adapterView, view, i, l ->
            mPresenter.destinationSelected(adapterView.getItemAtPosition(i) as City)
        }

        // fab setup
        fab.setOnClickListener { mPresenter.searchClick() }
    }

    /** @see [MainView.navigateToMap] */
    override fun navigateToMap(source: City, destination: City) {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra(MapActivity.SOURCE, source)
        intent.putExtra(MapActivity.DESTINATION, destination)
        startActivity(intent)
    }

    /** @see [MainView.showSourceNotSelectedError] */
    override fun showSourceNotSelectedError() {
        fromText.error = getString(R.string.error_sourceNotSelected)
    }

    /** @see [MainView.showDestinationNotSelectedError] */
    override fun showDestinationNotSelectedError() {
        toText.error = getString(R.string.error_destinationNotSelected)
    }

    /** @see [MainView.showError] */
    override fun showError(t: Throwable?) {
        Toast.makeText(this, "An error occurred!", Toast.LENGTH_SHORT).show()
    }

    /** [Filter] implementation. Asks presenter for cities list by specified text */
    val mFilter = object : Filter() {

        /** Send data to adapter for display. Invokes in main thread */
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            // check if we has some data
            if ((results?.count ?: 0) > 0) {
                @Suppress("UNCHECKED_CAST")
                // set data and update adapter
                mCitiesAdapter.setData(results?.values as List<City>)
                mCitiesAdapter.notifyDataSetChanged()
            } else {
                // notify adapter that data is invalid
                mCitiesAdapter.notifyDataSetInvalidated()
            }
        }

        /** Performs filtering. Invokes in background thread */
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            // create results storage
            val filterResults = FilterResults()

            // request presenter for cities
            val cities = mPresenter.getCities(constraint as String)

            // assign data to results and return
            filterResults.values = cities
            filterResults.count = cities.size
            return filterResults
        }
    }
}

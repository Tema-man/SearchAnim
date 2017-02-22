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
    lateinit var presenter: MainPresenter
    lateinit var citiesAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViews()
    }

    /** Prepare activity views for usage */
    fun prepareViews() {
        //toolbar setup
        setSupportActionBar(toolbar)

        // cities adapter setup
        citiesAdapter = CitiesAdapter(mFilter)

        // from field setup
        sourceView.setAdapter(citiesAdapter)
        sourceView.progressBar = sourceProgress
        sourceView.setOnItemClickListener { adapterView, view, i, l ->
            presenter.sourceSelected(adapterView.getItemAtPosition(i) as City)
        }

        // to field setup
        destinationView.setAdapter(citiesAdapter)
        destinationView.progressBar = destinationProgress
        destinationView.setOnItemClickListener { adapterView, view, i, l ->
            presenter.destinationSelected(adapterView.getItemAtPosition(i) as City)
        }

        // search button setup
        searchBtn.setOnClickListener { presenter.searchClick() }
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
        sourceView.error = getString(R.string.error_sourceNotSelected)
    }

    /** @see [MainView.showDestinationNotSelectedError] */
    override fun showDestinationNotSelectedError() {
        destinationView.error = getString(R.string.error_destinationNotSelected)
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
                citiesAdapter.setData(results?.values as List<City>)
                citiesAdapter.notifyDataSetChanged()
            } else {
                // notify adapter that data is invalid
                citiesAdapter.notifyDataSetInvalidated()
            }
        }

        /** Performs filtering. Invokes in background thread */
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            // create results storage
            val filterResults = FilterResults()

            // request presenter for cities
            val cities = presenter.getCities(constraint as String)

            // assign data to results and return
            filterResults.values = cities
            filterResults.count = cities.size
            return filterResults
        }
    }
}

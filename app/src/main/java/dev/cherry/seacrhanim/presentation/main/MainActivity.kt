package dev.cherry.seacrhanim.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import dev.cherry.seacrhanim.R
import dev.cherry.seacrhanim.entity.CitiesBean
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
        sourceView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                sourceView.setText("")
                presenter.sourceSelected(null)
            }
        }

        sourceView.setOnItemClickListener { adapterView, _, i, _ ->
            presenter.sourceSelected(adapterView.getItemAtPosition(i) as City)
        }

        // to field setup
        destinationView.setAdapter(citiesAdapter)
        destinationView.progressBar = destinationProgress
        destinationView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                destinationView.setText("")
                presenter.destinationSelected(null)
            }
        }

        destinationView.setOnItemClickListener { adapterView, _, i, _ ->
            presenter.destinationSelected(adapterView.getItemAtPosition(i) as City)
        }

        // search button setup
        searchBtn.setOnClickListener { presenter.searchClick() }
    }

    /** @see [MainView.navigateToMap] */
    override fun navigateToMap() {
        val intent = Intent(this, MapActivity::class.java)
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
                // set data and update adapter
                val cities = (results?.values as CitiesBean?)?.cities ?: emptyList()
                citiesAdapter.setData(cities)
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
            val citiesBean = presenter.getCities(constraint as String)

            // assign data to results and return
            filterResults.values = citiesBean
            filterResults.count = citiesBean.cities.size
            return filterResults
        }
    }
}

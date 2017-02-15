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

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mPresenter: MainPresenter
    lateinit var mCitiesAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViews()
    }

    fun prepareViews() {
        // toolbar setup
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

        // cities adapter setup
        mCitiesAdapter = CitiesAdapter(mFilter)

        // from field setup
        fromText.setAdapter(mCitiesAdapter)
        fromText.setProgressBar(fromProgress)
        fromText.setOnItemClickListener { adapterView, view, i, l ->
            mPresenter.sourceSelected(adapterView.getItemAtPosition(i) as City)
        }

        // to field setup
        toText.setAdapter(mCitiesAdapter)
        toText.setProgressBar(toProgress)
        toText.setOnItemClickListener { adapterView, view, i, l ->
            mPresenter.destinationSelected(adapterView.getItemAtPosition(i) as City)
        }

        // fab setup
        fab.setOnClickListener { mPresenter.fabClick() }
    }

    override fun navigateToMap(mSource: City, mDestination: City) {
        val intent: Intent = Intent(this, MapActivity::class.java)
        intent.putExtra(MapActivity.SOURCE, mSource)
        intent.putExtra(MapActivity.DESTINATION, mDestination)
        startActivity(intent)
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

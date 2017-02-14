package dev.cherry.seacrhanim.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import dev.cherry.seacrhanim.entity.City
import java.util.*


/**
 * @author DVLP_2
 * @since 14.02.2017.
 */
class CitiesAdapter(val mFilter: Filter) : BaseAdapter(), Filterable {

    var mCitiesList: ArrayList<City> = ArrayList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                    .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        }

        if (view?.tag == null) view?.tag = ViewHolder(view)
        (view?.tag as ViewHolder).bind(getItem(position))

        return view
    }

    fun setData(cities: List<City>) {
        mCitiesList.clear()
        mCitiesList.addAll(cities)
    }

    override fun getItem(position: Int): City {
        return mCitiesList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mCitiesList.size
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    class ViewHolder(view: View?) {

        var text: TextView = view?.findViewById(android.R.id.text1) as TextView

        fun bind(city: City) {
            text.text = city.toString()
        }
    }
}
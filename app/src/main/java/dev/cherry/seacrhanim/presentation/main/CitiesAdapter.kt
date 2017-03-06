package dev.cherry.seacrhanim.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import dev.cherry.seacrhanim.entity.City

/**
 * Adapter class for AutoCompleteTextView items. Uses [filter] to search appropriate items
 *
 * @author Artemii Vishnevskii
 * @since 14.02.2017.
 *
 * @param filter [Filter] implementation to search an items by specified text
 */
class CitiesAdapter(private val filter: Filter) : BaseAdapter(), Filterable {

    /** Store cities data */
    var citiesList: List<City> = emptyList()

    /** Android [BaseAdapter.getView] method implementation */
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

    /**
     * Set a new data list. Do not invalidate adapter automatically. [notifyDataSetChanged]
     * should be called after this method
     *
     * @param cities [List] a list of new cities
     */
    fun setData(cities: List<City>) {
        citiesList = cities
    }

    /**
     * Returns a list item by position
     *
     * @see [BaseAdapter.getItem]
     * @param position item position
     * @return [City] item from [citiesList] list
     */
    override fun getItem(position: Int): City {
        return citiesList[position]
    }

    /**
     * Returns an item id
     *
     * @see [BaseAdapter.getItemId]
     * @param position item position
     * @return item id
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Returns items count
     *
     * @see [BaseAdapter.getCount]
     * @return items list size
     */
    override fun getCount(): Int {
        return citiesList.size
    }

    /**
     * Returns [Filter] implementation
     * @see [Filterable.getFilter]
     */
    override fun getFilter(): Filter? {
        return filter
    }

    /**
     * ViewHolder pattern implementation class
     *
     * @param view [View] that hold by ViewHolder
     */
    private class ViewHolder(view: View?) {

        /** Text view for show [City] title */
        var text = view?.findViewById(android.R.id.text1) as TextView

        /**
         * Bind [City] data to view
         *
         * @param city [City] that will be bind
         */
        fun bind(city: City) {
            // get city string representation and display it.
            text.text = city.toString()
        }
    }
}
package com.gringo97.myapplication.defaultcluster.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import java.util.*

class MyItem : ClusterItem {
    private val mPosition: LatLng
    private var mTitle: String? = null
    private var mSnippet: String? = null

    constructor(lat: Double, lng: Double) {
        mPosition = LatLng(lat, lng)
        mTitle = null
        mSnippet = null
    }

    constructor(
        lat: Double,
        lng: Double,
        title: String?,
        snippet: String?
    ) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String? {
        return mTitle
    }

    override fun getSnippet(): String? {
        return mSnippet
    }

    /**
     * Set the title of the marker
     * @param title string to be set as title
     */
    fun setTitle(title: String) {
        mTitle = title
    }

    /**
     * Set the description of the marker
     * @param snippet string to be set as snippet
     */
    fun setSnippet(snippet: String) {
        mSnippet = snippet
    }
}

class MyItemReader {
    @Throws(JSONException::class)
    fun read(inputStream: InputStream): List<MyItem> {
        val items: MutableList<MyItem> = ArrayList()
        val json = Scanner(inputStream)
            .useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            var title: String? = null
            var snippet: String? = null
            val jsonObject = array.getJSONObject(i)
            val lat = jsonObject.getDouble("lat")
            val lng = jsonObject.getDouble("lng")
            if (!jsonObject.isNull("title")) {
                title = jsonObject.getString("title")
            }
            if (!jsonObject.isNull("snippet")) {
                snippet = jsonObject.getString("snippet")
            }

            items.add(MyItem(lat, lng, title, snippet))
        }
        return items
    }

    companion object {
        /*
     * This matches only once in whole input,
     * so Scanner.next returns whole InputStream as a String.
     * http://stackoverflow.com/a/5445161/2183804
     */
        private const val REGEX_INPUT_BOUNDARY_BEGINNING = "\\A"
    }
}

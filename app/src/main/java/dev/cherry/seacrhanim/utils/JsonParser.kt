package dev.cherry.seacrhanim.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.util.*

/**
 * An utility class for parsing json objects using Jackson data binding library

 * @author Artemii Vishnevskii
 * *
 * @author Temaa.mann@gmail.com
 */
object JsonParser {

    private val mapper: ObjectMapper = ObjectMapper()

    /**
     * Maps json string to specified class
     *
     * @param json   string to parse
     * @param tClass class of object in which json will be parsed
     * @param <T>    generic parameter for tClass
     * @return mapped T class instance
     * @throws IOException exception during parsing
    </T> */
    @Throws(IOException::class)
    fun <T> entity(json: String, tClass: Class<T>): T {
        return mapper.readValue(json, tClass)
    }

    /**
     * Maps json string to [ArrayList] of specified class object instances
     *
     * @param json   string to parse
     * @param tClass class of object in which json will be parsed
     * @param <T>    generic parameter for tClass
     * @return mapped T class instance
     * @throws IOException exception during parsing
     */
    @Throws(IOException::class)
    fun <T> arrayList(json: String, tClass: Class<T>): ArrayList<T> {
        val typeFactory = mapper.typeFactory
        val type = typeFactory.constructCollectionType(ArrayList::class.java, tClass)
        return mapper.readValue<ArrayList<T>>(json, type)
    }

    /**
     * Writes specified object as string
     *
     * @param object object to write
     * @return result json
     * @throws IOException exception during parsing
     */
    @Throws(IOException::class)
    fun toJson(`object`: Any): String {
        return mapper.writeValueAsString(`object`)
    }
}

package com.example.github.repositories.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

object TestUtil {

    fun <T> readJson(
        classLoader: ClassLoader? = TestUtil::class.java.classLoader,
        jsonFileName: String,
        resultClass: Class<T>
    ): T {
        val json: URL =
            classLoader?.getResource(jsonFileName) ?: throw FileNotFoundException(jsonFileName)
        return Gson().fromJson(json.readText(Charsets.UTF_8), resultClass)
            ?: throw IOException("Invalid JSON: $jsonFileName")
    }

    fun readHtml(classLoader: ClassLoader?, htmlFileName: String): String {
        val html: URL =
            classLoader?.getResource(htmlFileName) ?: throw FileNotFoundException(htmlFileName)
        return html.readText(Charsets.UTF_8)
    }

    inline fun <reified T : Any> observeData(liveData: LiveData<T>): MutableList<T> {
        val dataObserver = mockk<Observer<T>>()
        val dataStateSlot = slot<T>()
        val dataStates = mutableListOf<T>()
        every { dataObserver.onChanged(capture(dataStateSlot)) } answers {
            dataStates.add(
                dataStateSlot.captured
            )
        }
        liveData.observeForever(dataObserver)
        return dataStates
    }

    fun <T> countEvents(liveData: LiveData<T>): AtomicInteger {
        val counter = AtomicInteger(0)
        liveData.observeForever {
            counter.incrementAndGet()
        }
        return counter
    }
}

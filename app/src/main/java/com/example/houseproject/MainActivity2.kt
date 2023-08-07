package com.example.houseproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.houseproject.adapter.ScrollCustomAdapter
import com.example.houseproject.model.Fruit
import java.util.ArrayList

class MainActivity2 : AppCompatActivity() {

    private var TAG = "MainActivity"

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageArrayList: ArrayList<Fruit>
    private lateinit var adapter: ScrollCustomAdapter

    //RecyclerView data
    private var nameArray =
        listOf("Apple", "Mango", "Strawberry", "Pineapple", "Orange", "Blueberry", "Watermelon")
    private var imageArray = listOf(
        R.drawable.apple,
        R.drawable.mango,
        R.drawable.straw,
        R.drawable.pineapple,
        R.drawable.orange,
        R.drawable.blue,
        R.drawable.water
    )

    //handle scroll count
    var scrollCount: Int = 0

    private lateinit var layoutManager: LinearLayoutManager

    //handler for run auto scroll thread
    internal val handler = Handler()
    val displayMetrics = DisplayMetrics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        recyclerView = findViewById<RecyclerView>(R.id.recycler)
        imageArrayList = eatFruits()

        initLayoutManager()

    }

    private fun initLayoutManager() {

        layoutManager = object : LinearLayoutManager(this@MainActivity2) {
            override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
                val smoothScroller = object : LinearSmoothScroller(this@MainActivity2) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                        return 5.0f
                    }
                }
                smoothScroller.targetPosition = position
                startSmoothScroll(smoothScroller)
            }
        }
        adapter = object : ScrollCustomAdapter(this@MainActivity2, imageArrayList) {
            override fun load() {
                if (layoutManager.findFirstVisibleItemPosition() > 1) {
                    adapter.notifyItemMoved(0, imageArrayList.size - 1)
                }
            }
        }

        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(10)
        recyclerView.adapter = adapter
        autoScroll()
    }

    private fun autoScroll() {
        scrollCount = 0
        val speedScroll: Long = 1200
        val runnable = object : Runnable {
            override fun run() {
                if (layoutManager.findFirstVisibleItemPosition() >= imageArrayList.size / 2) {
                    adapter.load()
                    Log.e(TAG, "run: load $scrollCount")
                }
                recyclerView.smoothScrollToPosition(scrollCount++)
                Log.e(TAG, "run: $scrollCount")
                handler.postDelayed(this, speedScroll)
            }
        }
        handler.postDelayed(runnable, speedScroll)
    }

    private fun eatFruits(): ArrayList<Fruit> {
        val list = ArrayList<Fruit>()
        for (i in 0..6) {
            val fruitModel = Fruit(nameArray[i], imageArray[i])
            list.add(fruitModel)
        }
        return list
    }

}
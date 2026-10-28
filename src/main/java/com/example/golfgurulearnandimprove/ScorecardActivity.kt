package com.example.golfgurulearnandimprove

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ScorecardActivity : AppCompatActivity() {

    private lateinit var valueTextView: TextView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var stackOfLists: Stack<ArrayList<Int>>

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scorecard_layout)

        valueTextView = findViewById(R.id.valueTextView)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)

        // Initialize stack of ArrayLists with some data
        stackOfLists = Stack()
        val list1 = arrayListOf(10, 20, 30)
        val list2 = arrayListOf(40, 50, 60)
        stackOfLists.push(list1)
        stackOfLists.push(list2)

        // Display initial value
        displayValue()

        // Set click listeners
        prevButton.setOnClickListener { navigatePrevious() }
        nextButton.setOnClickListener { navigateNext() }
    }

    private fun displayValue() {
        if (!stackOfLists.isEmpty()) {
            val currentList = stackOfLists.get(currentIndex)
            val valueString = currentList?.joinToString(", ")
            valueTextView.text = valueString
        } else {
            valueTextView.text = "No data"
        }
    }

    private fun navigatePrevious() {
        if (currentIndex > 0) {
            currentIndex--
            displayValue()
        }
    }

    private fun navigateNext() {
        if (currentIndex < stackOfLists.size() - 1) {
            currentIndex++
            displayValue()
        }
    }
}

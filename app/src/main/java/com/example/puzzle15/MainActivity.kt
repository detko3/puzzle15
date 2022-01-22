package com.example.puzzle15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val buttons = listOf(
        R.id.button1,
        R.id.button2,
        R.id.button3,
        R.id.button4,
        R.id.button5,
        R.id.button6,
        R.id.button7,
        R.id.button8,
        R.id.button9,
        R.id.button10,
        R.id.button11,
        R.id.button12,
        R.id.button13,
        R.id.button14,
        R.id.button15,
        R.id.button16,
    )

    var counter = 0
    var isFirst = true
    var time = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    val timer = object: CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            time--
            runOnUiThread {
                val timeView = findViewById<TextView>(R.id.time)
                timeView.setText(time.toString())
            }
        }

        override fun onFinish() {
            if(!checkWinner()) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Lutujem, prehrali ste", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun onClick(v : View) {
        if (isFirst) {
            isFirst = false
           timer.start()
        }

        for((index, butID) in buttons.withIndex()) {
            if (v.id == butID) {
                val left = if (index % 4 == 0) index + 3 else index - 1
                val right = if (index % 4 == 3) index - 3 else index + 1
                val up = if (index < 4) index + 12 else index - 4
                val down = if (index > 11) index - 12 else index + 4
                if (check(left)) {
                    swapItems(index, left)
                }
                else if (check(right)) {
                    swapItems(index, right)
                }
                else if (check(up)) {
                    swapItems(index, up)
                }
                else if (check(down)) {
                    swapItems(index, down)
                }
            }
        }
        counter++
        val counterView = findViewById<TextView>(R.id.step)
        counterView.text = counter.toString()
        if (checkWinner() && time > 0) {
            timer.cancel()
            Toast.makeText(this, "Gratulujem k výhre", Toast.LENGTH_SHORT).show()
        }
    }

    fun check(index: Int): Boolean {
        val button = findViewById<Button>(buttons[index])
        if (button.visibility == View.INVISIBLE) return true
        return false
    }

    fun swapItems(myIndex: Int, emptyIndex: Int) {
//        Log.d("SWAP", " ${myIndex} , ${emptyIndex} ")
        val myButton = findViewById<Button>(buttons[myIndex])
        val emptyButton = findViewById<Button>(buttons[emptyIndex])
        emptyButton.visibility = View.VISIBLE
        emptyButton.isEnabled = true
        emptyButton.text = myButton.text
        myButton.visibility = View.INVISIBLE
        myButton.text = ""
        myButton.isEnabled = false
    }

    fun shuffle(v: View) {
        for (i in 1..100) {
            shuffleItems()
        }
        counter = 0
        val counterView = findViewById<TextView>(R.id.step)
        counterView.text = counter.toString()
        isFirst = true
        val timeView = findViewById<TextView>(R.id.time)
        time = 60
        timeView.text = time.toString()
        timer.cancel()
    }

    fun shuffleItems() {
        for((index, butID) in buttons.withIndex()) {
            val button = findViewById<Button>(butID)
            if (button.visibility == View.INVISIBLE) {
                val left = if (index % 4 == 0) index + 3 else index - 1
                val right = if (index % 4 == 3) index - 3 else index + 1
                val up = if (index < 4) index + 12 else index - 4
                val down = if (index > 11) index - 12 else index + 4
                val rndList = listOf(left, right, up, down)
                swapItems(rndList.random(), index)
                return
            }
        }
    }

    fun checkWinner(): Boolean {
        var winner = true
        for ((index, butID) in buttons.withIndex()) {
            val button = findViewById<Button>(butID)
            if (index < 15) {
                if (!button.text.equals((index + 1).toString())) {
                    winner = false
                }
            }
        }
        return winner
    }
}
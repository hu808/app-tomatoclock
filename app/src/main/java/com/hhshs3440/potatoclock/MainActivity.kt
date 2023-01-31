package com.hhshs3440.potatoclock

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private var isPaused = false
    private var isCancelled = false
    private var resumeFromMillis: Long = 0
    var denominator: Int = 0
    var counter: Int = 0


//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val millisInFutureforwork: Long = 10000
        val millisInFutureforrest: Long = 5000
        val countDownInterval: Long = 1000
        progressBar.setProgress(100)
        fabPause.hide()
        fabResume.hide()
        egg.visibility=View.INVISIBLE
        dayandnight.setOnClickListener {
            if(dayandnight.isChecked){
                my_ayout.setBackgroundResource(R.drawable.blacck)
            }else{
                my_ayout.setBackgroundResource(R.drawable.kitchen)
            }
        }
        fabStart.setOnClickListener {
            // Start the timer
            if (counter % 2 == 0) {
                denominator = millisInFutureforwork.toInt()
                timer(millisInFutureforwork, countDownInterval, denominator).start()
                status.text = "工作中"
            } else {
                denominator = millisInFutureforrest.toInt()
                timer(millisInFutureforrest, countDownInterval, denominator).start()
                status.text = "休息中"
            }
            it.isEnabled = false
            fabStop.isEnabled = true
            fabPause.isEnabled = true
            fabStart.hide()
            fabPause.show()
            fabResume.hide()
            fabStop.show()
            egg.visibility=View.VISIBLE


            isCancelled = false
            isPaused = false
        }
        fabStop.setOnClickListener {
            // Start the timer
            isCancelled = true
            isPaused = false

            it.isEnabled = false
            fabStart.isEnabled = true
            fabPause.isEnabled = false
            fabResume.isEnabled = false
            fabResume.hide()
            fabStart.show()
            time.setText("25:00")
            time.textSize= 40F
            egg.visibility=View.INVISIBLE

        }
        // Count down timer pause button
        fabPause.setOnClickListener {
            isPaused = true
            isCancelled = false
            it.isEnabled = false
            fabStart.isEnabled = false
            fabStop.isEnabled = true
            fabResume.isEnabled = true
            fabPause.hide()
            fabResume.show()
            egg.visibility=View.INVISIBLE
        }
        fabResume.setOnClickListener {
            // Resume the timer
            timer(resumeFromMillis, countDownInterval, denominator).start()

            isPaused = false
            isCancelled = false

            it.isEnabled = false
            fabPause.isEnabled = true
            fabStart.isEnabled = false
            fabStop.isEnabled = true
            fabStart.hide()
            fabResume.hide()
            fabPause.show()
            egg.visibility=View.VISIBLE
        }

        gotoplan.setOnClickListener {
            intent = Intent(this, plan::class.java)
            startActivity(intent)
        }

        gotodone.setOnClickListener {
            intent = Intent(this, done::class.java)
            startActivity(intent)
        }

//        gotostore.setOnClickListener {
//            intent = Intent(this, store::class.java)
//            startActivity(intent)
//        }

    }

    private fun timer(
        millisInFuture: Long,
        countDownInterval: Long,
        denominator: Int
    ): CountDownTimer {

        return object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {

                progressBar.setProgress(millisUntilFinished.toInt() / (denominator / 100))
                var minutesRemaining = millisUntilFinished / 60000
                var secondsRemaining = millisUntilFinished / 1000 - minutesRemaining * 60
                if (isPaused) {
                    if (minutesRemaining < 10)
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    else
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    resumeFromMillis = millisUntilFinished
                    cancel()
                } else if (isCancelled) {
                    if (minutesRemaining < 10)
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()

                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()

                    else
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()

                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    cancel()
                } else {
                    if (minutesRemaining < 10)
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                    else
                        if (secondsRemaining < 10)
                            time.text =
                                minutesRemaining.toString() + ":0" + secondsRemaining.toString()
                        else
                            time.text =
                                minutesRemaining.toString() + ":" + secondsRemaining.toString()
                }
            }

            override fun onFinish() {
                if (counter % 2 == 0) {
                    time.text = "休息五分鐘"

                } else {
                    time.text = "開工囉"
                }
                fabPause.hide()
                fabResume.hide()
                fabStop.hide()
                fabStart.show()
                fabStart.isEnabled = true
                counter++
                progressBar.setProgress(0)
                egg.visibility=View.INVISIBLE
            }
        }

    }
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun buttonBlack(){
//        mLayout = findViewById(R.id.my_ayout) as RelativeLayout
//        val res: Resources = this.resources
//        val drawab: Drawable
//        drawab = res.getDrawable(R.drawable.blacck , theme)
//        mLayout!!.setBackground(drawab)
//    }
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun buttonwhite(){
//        mLayout = findViewById(R.id.my_ayout) as RelativeLayout
//        val res: Resources = this.resources
//        val drawab: Drawable
//        drawab = res.getDrawable(R.drawable.kitchen , theme)
//        mLayout!!.setBackground(drawab)
//    }


    // Extension function to show toast message
//    fun Context.toast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }

}

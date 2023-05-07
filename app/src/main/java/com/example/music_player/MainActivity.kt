package com.example.music_player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    //variables
    var oneTimeOnly = 0
    var startTime=0.0
    var finalTime=0.0
    var forwardTime = 10000
    var backwardTime=10000

    //handler
    var handler:Handler= Handler()

    //media player
    var mediaPlayer = MediaPlayer()
    lateinit var time_txt :TextView
    lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var title_txt: TextView = findViewById(R.id.song_title)
        time_txt  = findViewById(R.id.time_txt)

        val play_btn: Button = findViewById(R.id.play_btn)
        val pause_btn: Button = findViewById(R.id.pause_btn)
        val back_btn: Button = findViewById(R.id.back_btn)
        val forward_btn: Button = findViewById(R.id.forward_btn)

        seekBar = findViewById(R.id.seekBar4)


        //Media player
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.john)

        seekBar.isClickable = false

        //adding functionalities to the buttons
        play_btn.setOnClickListener(){
            mediaPlayer.start()

            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }
            time_txt.text = startTime.toString()
            seekBar.setProgress(( startTime.toInt()))
            handler.postDelayed(UpdateSongTime, 100)
        }


        //Stop button
        pause_btn.setOnClickListener(){
            mediaPlayer.pause()
        }

        //forward button
        forward_btn.setOnClickListener(){
            var temp= startTime
            if((temp+ forwardTime)<= finalTime){
                startTime= startTime+forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else
                Toast.makeText(this,"cant Jump forward", Toast.LENGTH_LONG).show()
        }

        //back button
        back_btn.setOnClickListener(){
            var temp= startTime
            if((temp- backwardTime)>0){
                startTime= startTime- backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else
                Toast.makeText(this,"cant Jump backward", Toast.LENGTH_LONG).show()
        }

        //setting up music title
        title_txt.text=""+ resources.getResourceEntryName(R.raw.john)
    }

    //creating the Runnable
    val UpdateSongTime: Runnable= object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            time_txt.text= ""+
                    String.format(
                        "%d min , %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(
                            startTime.toLong()
                                    - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    startTime.toLong()
                                )
                            ))
                    )

            seekBar.progress= startTime.toInt()
            handler.postDelayed(this,100)
        }
    }
}
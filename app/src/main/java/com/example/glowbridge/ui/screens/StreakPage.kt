package com.example.glowbridge.ui.screens

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.glowbridge.R

@Composable
fun StreakPage(){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        Image(painter = painterResource(id = R.drawable.mainstar), contentDescription ="Main star",
            modifier = Modifier
                .size(150.dp)
                .clickable {
                    val mediaPlayer = MediaPlayer.create(context, R.raw.sparking)
                    mediaPlayer.start()  // Play audio

                }
        )
    }
}
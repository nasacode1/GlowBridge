package com.example.glowbridge.ui.screens

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.compose.runtime.livedata.observeAsState
import android.view.LayoutInflater
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.glowbridge.R
import com.example.glowbridge.data.repository.StreakTaskRepository
import com.example.glowbridge.viewmodel.StreakTaskViewModel
import com.example.glowbridge.viewmodel.StreakTaskViewModelFactory

@Composable
fun StreakPage(repository: StreakTaskRepository){
    val viewModel: StreakTaskViewModel = viewModel(
        factory = StreakTaskViewModelFactory(repository)
    )
    val task = viewModel.task.observeAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchRandomTask() // Fetch task when screen loads
    }
    Box(modifier = Modifier.fillMaxSize()){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Max streak count : ")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 140.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Curr streak count : ")
        }
        Column (modifier = Modifier.matchParentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            val context = LocalContext.current
            var expanded = remember{ mutableStateOf(false)}
            Image(painter = painterResource(id = R.drawable.mainstar), contentDescription ="Main star",
                modifier = Modifier
                    .height(if (expanded.value) 300.dp else 200.dp)
                    .clickable
                    {
                        if (!expanded.value) {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.sparking)
                            mediaPlayer.start()
                            expanded.value = !expanded.value
                        }
                    }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Today's Task:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.toString(),
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(30.dp)
            )
        }
    }
}


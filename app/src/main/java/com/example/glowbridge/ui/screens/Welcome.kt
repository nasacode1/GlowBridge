package com.example.glowbridge.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.glowbridge.R

@Composable
fun Welcome(onStartedSuccess: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center


    ){
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", Modifier.size(350.dp))
        GetStartedButton(onStartedSuccess = onStartedSuccess)
    }
}


@Composable
fun GetStartedButton(onStartedSuccess: () -> Unit){
    Button(onClick = {
        onStartedSuccess()
    }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green), modifier = Modifier.padding(20.dp).fillMaxWidth()) {
        Text(text = "Get Started")
    }
}
package com.example.glowbridge.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.glowbridge.R

@Composable
fun Welcome(){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center


    ){
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo", Modifier.size(8150.dp))

    }
}

@Composable
fun SubmitButton(){

}
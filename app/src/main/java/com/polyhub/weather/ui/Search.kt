package com.polyhub.weather.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    textFieldState: TextFieldState
){
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
//        SearchBar(
//            modifier = Modifier
//                .align(Alignment.TopCenter),
//            inputField = {
//                SearchBarDefaults.InputField(
//                    query = textFieldState.text.toString()
//                )
//            }
//        ) { }
    }
}
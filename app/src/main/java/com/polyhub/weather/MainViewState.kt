package com.polyhub.weather

sealed class MainViewState {

    object Loading : MainViewState()

    data class Success(val apiResponse: ApiResponse) : MainViewState()

    data class Error(val message: String) : MainViewState()
}
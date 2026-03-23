package com.polyhub.weather.ui

import android.app.appsearch.SearchResults
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.polyhub.weather.api.UIData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    textFieldState: TextFieldState,
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<UIData>,
    selectCity: (UIData) -> Unit,
    onCitySelected: () -> Unit
){

    val searchBarState = rememberSearchBarState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopSearchBar(
                    state = searchBarState,
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = textFieldState.text.toString(),
                            onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                            onSearch = {
                                onSearch(textFieldState.text.toString())
                            },
                            expanded = false,
                            onExpandedChange = {},
                            leadingIcon = {
                                IconButton(onClick = {
                                    onBackClick()
                                }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "back",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }},
                            placeholder = { Text("Find city") }
                        )
                    }
                )
            }
        )  { innerPadding ->
            SearchContent(
                modifier = Modifier
                    .padding(innerPadding),
                searchResults = searchResults,
                onCityClick = selectCity,
                onCitySelected = onCitySelected
            )
        }
    }
}


@Composable
fun SearchContent(
    modifier: Modifier,
    searchResults: List<UIData>,
    onCityClick: (UIData) -> Unit,
    onCitySelected: () -> Unit
){
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(searchResults) { result ->
            ListItem(
                headlineContent = { Text(result.weatherUI.locationName)},
                modifier = Modifier
                    .clickable{
                        onCityClick(result)
                        onCitySelected()
                    }
                    .fillMaxWidth()
            )
        }
    }
}
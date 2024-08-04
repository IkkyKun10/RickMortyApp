package com.riezki.rickmortyapp.component.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.riezki.rickmortyapp.ui.theme.RickAction
import com.riezki.rickmortyapp.ui.theme.RickTextPrimary

/**
 * @author riezky maisyar
 */

@Composable
fun DataPointComponent(dataPoint: DataPoint) {
    Column {
        Text(
            text = dataPoint.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = RickAction
        )
        Text(
            text = dataPoint.description,
            fontSize = 24.sp,
            color = RickTextPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

data class DataPoint(
    val title: String,
    val description: String
)

@Preview
@Composable
fun DataPointComponentPrev() {
    val data = DataPoint(title = "Last known location", description = "Citadel of Ricks")
    DataPointComponent(dataPoint = data)
}
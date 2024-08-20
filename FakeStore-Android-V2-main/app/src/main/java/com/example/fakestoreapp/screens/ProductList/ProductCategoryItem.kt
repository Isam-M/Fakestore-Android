package com.example.fakestoreapp.screens.ProductList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProductCategoryItem(
    category: String,
    isSelected: Boolean,
    onCategoryClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCategoryClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        contentColor = LocalContentColor.current
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isSelected) Color.Black else Color.Gray
                )
            )
        }
    }
}
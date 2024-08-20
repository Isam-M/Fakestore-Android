package com.example.fakestoreapp.screens.ProductList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fakestoreapp.data.Product
import com.example.fakestoreapp.design.AppTheme


@Composable
fun ProductItem(
    product: Product,
    onClick: (Product) -> Unit
) {
    AppTheme {
        Box(modifier = Modifier.shadow(elevation = 1.dp, shape = RoundedCornerShape(10))) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
                    .clip(RoundedCornerShape(15.dp))
                    .clickable { onClick(product) },
            ) {
                Card(
                    modifier = Modifier.heightIn(0.dp, 250.dp).width(200.dp), shape = RoundedCornerShape(10.dp)){
                    AsyncImage(
                        model = product.image,
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center,
                        contentDescription = "Image of ${product.title}"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.height(50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Text(
                        text = product.title,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$${product.price}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        modifier = Modifier
                            .padding(end = 4.dp, start = 4.dp)

                    )
                }
            }
        }
    }
}






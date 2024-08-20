package com.example.fakestoreapp.screens.ProductDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fakestoreapp.data.Review
import kotlinx.coroutines.delay


@Composable
fun RatingBar(
    maxRating: Int = 5,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    rating: Float
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxRating) {
            val icon = if (i <= rating) Icons.Default.Star else Icons.Default.Star
            val color = if (i <= rating) MaterialTheme.colorScheme.tertiary else Color.Gray

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRatingChanged(i.toFloat()) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewInput(
    productId: Int,
    viewModel: ProductDetailsViewModel,
    onReviewSubmit: (Int, String, Float, String) -> Unit
) {
    val (username, setUsername) = remember { mutableStateOf("") }
    val (rating, setRating) = remember { mutableStateOf(0f) }
    val (comment, setComment) = remember { mutableStateOf("") }
    val (isSubmitting, setIsSubmitting) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Leave a Review",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.tertiary
        )

        OutlinedTextField(
            value = username,
            onValueChange = setUsername,
            label = { Text("username", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        )

        OutlinedTextField(
            value = comment,
            onValueChange = setComment,
            label = { Text("Comment", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )
        RatingBar(
            rating = rating,
            onRatingChanged = { newRating ->
                setRating(newRating)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        LaunchedEffect(isSubmitting) {
            if (isSubmitting) {
                delay(1000)
                onReviewSubmit(productId, username, rating, comment)

                viewModel.addReview(productId, Review(productId, username, rating.toDouble(), comment))

                setUsername("")
                setRating(0f)
                setComment("")

                setIsSubmitting(false)
            }
        }

        Button(
            onClick = {
                setIsSubmitting(true)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text(
                text = "Submit Review",
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun ReviewList(productId: Int, viewModel: ProductDetailsViewModel) {
    val reviewsState by viewModel.getReviewsForProduct(productId).collectAsState()

    LaunchedEffect(reviewsState) {
    }

    if (reviewsState != null) {
        val reviews = reviewsState!!

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Reviews on the product",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            reviews.forEach { review ->
                ReviewCard(review = review)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } else {
        Text("Reviews not available")
    }
}


@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = review.username ?: "Unknown user",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = review.rating?.toString() ?: "?",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.comment ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

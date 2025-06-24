package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.droidkotlin.core.R

/**
 * `PlaceholderImage` is a composable function that displays an image from a provided URL with a placeholder.
 *
 * @param url The URL of the image to display. This can be any type that Coil's ImageRequest.Builder can handle (like a String, Uri, File, etc.).
 * @param contentDescription A description of the image. This will be used for accessibility purposes.
 * @param gender The gender of the person in the image. This is used to determine which placeholder image to use.
 * @param modifier A Modifier that will be applied to the Surface that wraps the image.
 *
 * Inside the function, a Surface is created with the provided modifier. A placeholder image is chosen based on the gender parameter.
 * An ImageRequest is built with the provided URL and a crossfade animation. This request is passed to the AsyncImage composable,
 * which handles the loading and displaying of the image.
 */
@Composable
fun PlaceholderImage(
    url: Any?,
    contentDescription: String?,
    gender: String?,
    modifier: Modifier = Modifier,
    size: Dp
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        val placeholder by rememberSaveable {
            mutableIntStateOf(if (gender?.contains("male")!!) {
                R.drawable.image_placeholder_man
            } else {
                R.drawable.image_placeholder_woman
            })
        }

        val model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build()

        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            placeholder = painterResource(placeholder),
            modifier = Modifier
                .clip(CircleShape)
                .size(size)
        )
    }
}
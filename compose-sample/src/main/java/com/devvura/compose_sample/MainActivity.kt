package com.devvura.compose_sample

import com.devvura.safe_image.SafeImage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HorizontalPager(
                state = rememberPagerState(pageCount = { testImages.size })
            ) { page ->
                val imageUrl = testImages[page]
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SafeImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = imageUrl,
                        contentDescription = "Sample Image",
                        loadingContent = { Text(text = "Loading...") },
                        errorContent = { Text(text = "Error loading image") },
                        onBlurContent = { OnBlurContent() },
                        blurRadius = 20.dp,
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}

@Composable
private fun OnBlurContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Sensitive Content",
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .background(
                    Color.Black.copy(
                        alpha = 0.7f
                    )
                )
                .padding(8.dp)
        )
    }
}

val testImages = listOf(
    "https://wallpapercave.com/wp/wp14373553.jpg",
    "https://cdn.britannica.com/82/152982-050-11159CF4/Daniel-Radcliffe-Rupert-Grint-Emma-Watson-Harry.jpg",
    "https://im.jdmagicbox.com/comp/jd_social/news/2020jun09/image-832345-8np8bbp9h7.jpg",
    "https://hentaidude.tv/wp-content/webp-express/webp-images/doc-root/wp-content/uploads/2022/06/Seika-Jogakuin-Kounin-Sao-Ojisan.jpg.webp"
)
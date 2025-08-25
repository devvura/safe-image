import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import androidx.compose.ui.layout.ContentScale as ComposeContentScale


@Composable
fun HalalImage(
    model: Any,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    loadingContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    onBlurContent: (@Composable () -> Unit)? = null,
    blurRadius: Dp = SafeImageDefaults.BlurRadius,
    contentScale: ContentScale = SafeImageDefaults.ContentScale,
) {
    val context = LocalPlatformContext.current
    var isBlurred by rememberSaveable { mutableStateOf(true) }
    val request = ImageRequest
        .Builder(context)
        .data(model)
        .transformations(
            getBlurHaramTransformation(
                onBlur = { _isBlurred: Boolean ->
                    isBlurred = _isBlurred
                },
                blurRadiusPx = blurRadius.value.toInt(),
                context = context
            )
        ).memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(policy = CachePolicy.ENABLED)
        .crossfade(true)
        .build()

    SubcomposeAsyncImage(
        modifier = modifier,
        model = request,
        contentDescription = contentDescription,
        contentScale = contentScale
    ) {
        val state by painter.state.collectAsState()
        when (state) {
            is AsyncImagePainter.State.Success -> {
                this@SubcomposeAsyncImage.SubcomposeAsyncImageContent()
                onBlurContent?.let { content ->
                    if (isBlurred) {
                        content()
                    }
                }
            }

            is AsyncImagePainter.State.Loading -> {
                loadingContent?.let { content ->
                    content()
                }
            }

            is AsyncImagePainter.State.Error -> {
                errorContent?.let { content ->
                    content()
                }
            }

            else -> Unit
        }
    }
}

internal object SafeImageDefaults {
    val BlurRadius = 16.dp
    val ContentScale = ComposeContentScale.Crop
}
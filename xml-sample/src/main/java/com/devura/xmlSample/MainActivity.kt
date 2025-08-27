package com.devura.xmlSample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.devfares.halal_view.loadSafeImage
import com.devura.xmlSample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView1.loadSafeImage(
            imageUrl = "https://wallpapers.com/images/high/spongebob-funny-pictures-b5nokjbvjo8xs3ht.webp",
            placeholderRes = R.drawable.loading_bar,
            errorDrawableRes = R.drawable.error_svgrepo_com
        )

        binding.imageView2.loadSafeImage(
            imageUrl =  "https://cdn.britannica.com/82/152982-050-11159CF4/Daniel-Radcliffe-Rupert-Grint-Emma-Watson-Harry.jpg",
            placeholderRes = R.drawable.loading_bar,
            errorDrawableRes = R.drawable.error_svgrepo_com
        )

        binding.imageView3.loadSafeImage(
            imageUrl = "https://im.jdmagicbox.com/comp/jd_social/news/2020jun09/image-832345-8np8bbp9h7.jpg",
            placeholderRes = R.drawable.loading_bar,
            errorDrawableRes = R.drawable.error_svgrepo_com
        )

        binding.imageView4.loadSafeImage(
            imageUrl = "https://hentaidude.tv/wp-content/webp-express/webp-images/doc-root/wp-content/uploads/2022/06/Seika-Jogakuin-Kounin-Sao-Ojisan.jpg.webp",
            placeholderRes = R.drawable.loading_bar,
            errorDrawableRes = R.drawable.error_svgrepo_com
        )
    }
}

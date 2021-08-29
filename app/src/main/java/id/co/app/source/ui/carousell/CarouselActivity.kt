package id.co.app.source.ui.carousell

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.co.app.components.carousell.CarouselClickListener
import id.co.app.components.carousell.CarouselItem
import id.co.app.components.carousell.CarouselUnify
import id.co.app.source.R

class CarouselActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carousel_sample_layout)

        title = "Carousel"

        val item = listOf(
            CarouselItem("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc", id=1),
            CarouselItem("https://cf.shopee.co.id/file/622dd62523c0d2d6a791c88f82b17aef", id=2),
            CarouselItem("https://cf.shopee.co.id/file/b6c08e521434dfaf476adb03f80f8e78", id=3)
        )
//        findViewById<CarouselUnify>(R.id.carousel).apply {
//            indicatorPosition = CarouselUnify.INDICATOR_BC
//            infinite = false
//            autoplay = true
//            addData(item)
//        }
        findViewById<CarouselUnify>(R.id.carousel3).apply {
            indicatorPosition = CarouselUnify.INDICATOR_BC
            centerMode = true
            infinite = true
            addData(item)
        }
//        findViewById<CarouselUnify>(R.id.carousel6).apply {
//            indicatorPosition = CarouselUnify.INDICATOR_BC
//            infinite = true
//            addData(item)
//        }
//        findViewById<CarouselUnify>(R.id.carousel7).apply {
//            indicatorPosition = CarouselUnify.INDICATOR_BL
//            actionTextPosition = CarouselUnify.INDICATOR_BR
//            actionText = "Lihat Semua"
//            infinite = true
//            autoplay = true
//            centerMode = true
//            cornerRadius = 16
//            listener = object : CarouselClickListener {
//                override fun onBannerClick(model: CarouselItem) {
//                    Toast.makeText(this@CarouselActivity, model.url, Toast.LENGTH_LONG).show()
//                }
//            }
//            addData(item)
//        }

//        val itemParam = { view: View, data: Any ->
//            val imageCarousel = view.findViewById<ImageUnify>(R.id.imageCarousel)
//
//            view.findViewById<CarouselUnify>(R.id.carousel6)?.post {
//                imageCarousel.initialWidth = view.findViewById<CarouselUnify>(R.id.carousel6).measuredWidth
//            }
//            imageCarousel.setImageUrl((data as SampleData).title, 0.333f)
//        }
//
//        findViewById<CarouselUnify>(R.id.carousel6).apply {
//            autoplayDuration = 5000
//            indicatorPosition = CarouselUnify.INDICATOR_BL
//            infinite = true
//
//            addItems(R.layout.carousel_item_sample_layout, itemArr, itemParam)
//        }
//
//        val bannerArr: ArrayList<String> = ArrayList(4)
//        bannerArr.add("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc")
//        bannerArr.add("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc")
//        bannerArr.add("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc")
//        bannerArr.add("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc")
//
//        findViewById<CarouselUnify>(R.id.carousel7).apply {
//            slideToShow = 1.5f
//            slideToScroll = 1
//            centerMode = true
//            infinite = true
//            indicatorPosition = CarouselUnify.INDICATOR_BL
//            actionTextPosition = CarouselUnify.INDICATOR_BR
//            actionText = "Lihat Semua"
//            onItemClick = {index ->
//                println("$index")
//            }
//
//            addBannerImages(bannerArr)
//        }
    }
}

class SampleData(var title: String, var background: Int)
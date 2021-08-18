package id.co.app.source.ui.carousell

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import id.co.app.components.carousell.CarouselUnify
import id.co.app.components.image.ImageUnify
import id.co.app.components.utils.toPx
import id.co.app.source.R

class CarouselActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carousel_sample_layout)

        title = "Carousel"

        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 130.toPx()).apply {
            setMargins(20, 20, 20, 20)
        }
        val mItem = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.RED)
        }
        val mItem2 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.BLUE)
        }

        val mItem3 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.GREEN)
        }

        val mItem4 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.YELLOW)
        }

        val mItem5 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.CYAN)
        }

        val mItem6 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.BLACK)
        }

        findViewById<CarouselUnify>(R.id.carousel).apply {
            slideToShow = 1.2f
            indicatorPosition = CarouselUnify.INDICATOR_BC
            freeMode = false
            centerMode = false
            autoplay = false
            addItem(mItem)
            addItem(mItem2)
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    println("loggg $prev $current")
                }
            }
            onDragEventListener = object : CarouselUnify.OnDragEventListener {
                override fun onDrag(progress: Float) {
                    println("loggg $progress")
                }
            }
        }

        val mItem7 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.RED)
        }
        val mItem8 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.BLUE)
        }

        val mItem9 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.GREEN)
        }

        val mItem10 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.YELLOW)
        }

        findViewById<CarouselUnify>(R.id.carousel2).apply {
            slideToShow = 1.3f
            indicatorPosition = CarouselUnify.INDICATOR_TL
            freeMode = false
            centerMode = false
            addItem(mItem7)
            addItem(mItem8)
            addItem(mItem9)
            addItem(mItem10)
        }

        val mItem11 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.RED)
        }
        val mItem12 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.BLUE)
        }

        val mItem13 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.GREEN)
        }

        val mItem14 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.YELLOW)
        }

        findViewById<CarouselUnify>(R.id.carousel3).apply {
            slideToShow = 1.1f
            indicatorPosition = CarouselUnify.INDICATOR_TR
            freeMode = false
            centerMode = true
            addItem(mItem11)
            addItem(mItem12)
            addItem(mItem13)
            addItem(mItem14)
        }

        val mItem15 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.RED)
        }
        val mItem16 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.BLUE)
        }

        val mItem17 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.GREEN)
        }

        val mItem18 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.YELLOW)
        }

        findViewById<CarouselUnify>(R.id.carousel4).apply {
            slideToShow = 1.5f
            indicatorPosition = CarouselUnify.INDICATOR_BR
            freeMode = true
            centerMode = false
            addItem(mItem15)
            addItem(mItem16)
            addItem(mItem17)
            addItem(mItem18)
        }

        val mItem19 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.RED)
        }
        val mItem20 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.BLUE)
        }

        val mItem21 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.GREEN)
        }

        val mItem22 = LinearLayout(this).apply {
            layoutParams = param
            setBackgroundColor(Color.YELLOW)
        }

        findViewById<CarouselUnify>(R.id.carousel5).apply {
            slideToShow = 2f
            slideToScroll = 2
            indicatorPosition = CarouselUnify.INDICATOR_BL
            freeMode = false
            centerMode = false
            addItem(mItem19)
            addItem(mItem20)
            addItem(mItem21)
            addItem(mItem22)
        }

        val itemArr: ArrayList<Any> = ArrayList(5)
        itemArr.add(SampleData("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc", Color.RED))
        itemArr.add(SampleData("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc", Color.RED))
        itemArr.add(SampleData("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc", Color.RED))
        itemArr.add(SampleData("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc", Color.RED))
        itemArr.add(SampleData("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc", Color.RED))

        val itemParam = { view: View, data: Any ->
            val imageCarousel = view.findViewById<ImageUnify>(R.id.imageCarousel)

            view.findViewById<CarouselUnify>(R.id.carousel6)?.post {
                imageCarousel.initialWidth = view.findViewById<CarouselUnify>(R.id.carousel6).measuredWidth
            }
            imageCarousel.setImageUrl((data as SampleData).title, 0.333f)
        }

        findViewById<CarouselUnify>(R.id.carousel6).apply {
            autoplayDuration = 5000
            indicatorPosition = CarouselUnify.INDICATOR_BL
            infinite = true

            addItems(R.layout.carousel_item_sample_layout, itemArr, itemParam)
        }

        val bannerArr: ArrayList<String> = ArrayList(4)
        bannerArr.add("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc")
        bannerArr.add("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc")
        bannerArr.add("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc")
        bannerArr.add("https://cf.shopee.co.id/file/9a8fb1724fe455d914f0f6f1f42fd3cc")

        findViewById<CarouselUnify>(R.id.carousel7).apply {
            slideToShow = 1.5f
            slideToScroll = 1
            centerMode = true
            indicatorPosition = CarouselUnify.INDICATOR_BL
            actionTextPosition = CarouselUnify.INDICATOR_BR
            actionText = "Lihat Semua"
            onItemClick = {index ->
                println("$index")
            }

            addBannerImages(bannerArr)
        }
    }
}

class SampleData(var title: String, var background: Int)
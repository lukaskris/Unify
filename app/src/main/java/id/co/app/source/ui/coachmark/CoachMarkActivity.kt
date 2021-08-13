package id.co.app.source.ui.coachmark

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.co.app.components.coachmark.CoachMark
import id.co.app.components.coachmark.CoachMarkItem
import id.co.app.source.R

class CoachMarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coachmark_sample_layout)


        val coachMarkItem = ArrayList<CoachMarkItem>()
        val coachMark = CoachMark(this)
        val btn1 = findViewById<View>(R.id.btn1)
        val btn2 = findViewById<View>(R.id.btn2)
        val btn3 = findViewById<View>(R.id.btn3)
        val btn4 = findViewById<View>(R.id.btn4)
        val btn5 = findViewById<View>(R.id.btn5)
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        coachMarkItem.add(
            CoachMarkItem(
                btn1,
                "Title to give the main context",
                "Body text to describe the highlighted feature, make it clear and concise, no more than 2 lines",
                CoachMark.POSITION_TOP
            )
        )

        coachMarkItem.add(
            CoachMarkItem(
                btn1,
                "Title to give the main context, make it clear and concise, no more than 2 lines",
                "Body text to describe the highlighted feature, make it clear and concise, no more than 2 lines",
                CoachMark.POSITION_TOP
            )
        )

        coachMarkItem.add(
            CoachMarkItem(
                btn2,
                "Title to give the main context, make it clear and concise, no more than 2 lines",
                "Body text to describe the highlighted feature, make it clear and concise, no more than 2 lines"
            )
        )

        coachMarkItem.add(
            CoachMarkItem(
                btn3,
                "",
                "Body text to describe the highlighted feature, make it clear and concise, no more than 2 lines"
            )
        )

        coachMarkItem.add(
            CoachMarkItem(
                btn4,
                "Step 4",
                "Ini Step 4"
            )
        )

        coachMarkItem.add(
            CoachMarkItem(
                btn5,
                "Title to give the main context, make it clear and concise, no more than 2 lines",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
            )
        )

//        simple coachMark margin left below
//        coachMark.simpleMarginLeft = 0.toPx()
//        coachMarkItem.clear()
        scrollView.post {
            scrollView.scrollTo(0, btn5.bottom)
            coachMark.showCoachMark(coachMarkItem, scrollView, 5)
        }

        coachMark.setStepListener(object: CoachMark.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMarkItem) {
                if(currentIndex == 1) {
                    coachMark.stepPagination?.visibility = View.GONE
                    Toast.makeText(this@CoachMarkActivity, "CoachMark on step ${currentIndex + 1} without pagination", Toast.LENGTH_LONG).show()
                } else {
                    coachMark.stepPagination?.visibility = View.VISIBLE
                }

            }
        })

        coachMark.onFinishListener = {
            Toast.makeText(this, "CoachMark is finished", Toast.LENGTH_LONG).show()
        }

        coachMark.onDismissListener = {
            Toast.makeText(this, "CoachMark is dismissed", Toast.LENGTH_LONG).show()
        }

        var isHide = true
        btn2.setOnClickListener {
            isHide = if(isHide) {
                coachMark.animateHide()
                false
            } else {
                coachMark.animateShow()
                true
            }
        }

        var isGone = true
        btn1.setOnClickListener {
            isGone = if(isGone) {
                coachMark.dismissCoachMark()
                false
            } else {
                coachMark.showCoachMark(coachMarkItem, scrollView)
                true
            }
        }

        scrollView.viewTreeObserver
            .addOnScrollChangedListener {
                val scrollBounds = Rect()
                scrollView.getHitRect(scrollBounds)

                if(coachMark.coachMarkItem.size == 1) {
                    if (coachMark.coachMarkItem[0].anchorView.getLocalVisibleRect(scrollBounds)) {
                        coachMark.showCoachMark(coachMarkItem)
                    } else {
                        coachMark.hideCoachMark()
                    }
                }
            }
    }


}
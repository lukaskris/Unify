package id.co.app.source.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.co.app.source.ui.feed.FeedFragment
import id.co.app.source.ui.home.HomeFragment

const val HOME_PAGE_INDEX = 0
const val FEED_PAGE_INDEX = 1

class MainPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        HOME_PAGE_INDEX to { HomeFragment() },
        FEED_PAGE_INDEX to { FeedFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}
package swu.xl.secondhouse.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ImagePageAdapter(fm: FragmentManager, behavior: Int, private val fragments: List<Fragment>, private val titles: List<String>) : FragmentPagerAdapter(fm, behavior) {
    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = titles[position]
}
package swu.xl.secondhouse.util

import swu.xl.secondhouse.R
import swu.xl.secondhouse.base.BaseFragment
import swu.xl.secondhouse.ui.fragment.AnalysisFragment
import swu.xl.secondhouse.ui.fragment.HomeFragment
import swu.xl.secondhouse.ui.fragment.HouseFragment
import swu.xl.secondhouse.ui.fragment.UserFragment

class FragmentUtil {
    companion object {
        val fragmentUtil by lazy { FragmentUtil() }
    }

    private val homeFragment by lazy { HomeFragment() }
    private val houseFragment by lazy { HouseFragment() }
    private val analysisFragment by lazy { AnalysisFragment() }
    private val userFragment by lazy { UserFragment() }

    fun getFragment(tabId: Int): BaseFragment {
        return when(tabId) {
            R.id.tab_home -> homeFragment
            R.id.tab_house -> houseFragment
            R.id.tab_analysis -> analysisFragment
            R.id.tab_user -> userFragment
            else -> homeFragment
        }
    }
}
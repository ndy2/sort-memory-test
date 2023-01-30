package tests

import helper.Data
import test.SortTest

class KotlinTests {
    companion object {
        val kotlinComparableTest = SortTest { (it as ArrayList).sort() }

        val kotlinSortByTest = SortTest { (it as ArrayList).sortBy { data -> data.a } }

        val kotlinInlineCompareByTest = SortTest { (it as ArrayList).sortWith(compareBy { data -> data.a }) }

        val kotlinOutlineCompareByTest = SortTest {
            val compA = compareBy<Data> { data -> data.a }
            (it as ArrayList).sortWith(compA)
        }
    }
}

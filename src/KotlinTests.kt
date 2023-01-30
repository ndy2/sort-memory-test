package tests

import test.SortTest

class KotlinTests {
    companion object {
        // [group 1 - comparable]
        val kotlinComparableTest = SortTest { (it as ArrayList).sort() }

        // [group 2 - comparator]
        val kotlinComparatorTest =
            SortTest { (it as ArrayList).sortWith(Comparator { o1, o2 -> Integer.compare(o1.a, o2.a) }) }

        // [group 3 -  inline tests]
        val kotlinInlineSortByTest = SortTest { (it as ArrayList).sortBy { data -> data.a } }

        val kotlinInlineCompareByTest = SortTest { (it as ArrayList).sortWith(compareBy { data -> data.a }) }

        // [group 4 -  extracted inline tests]
        val kotlinExtractedInlineComparingValuesByTest =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> compareValuesBy(a, b) { data -> data.a } }) }

        val kotlinExtractedInlineCompareValuesTest =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> compareValues(a.a, b.a) }) }

    }
}

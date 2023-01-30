package tests

import test.SortTest

@Suppress("UNCHECKED_CAST")
class KotlinTests {
    companion object {
        // [group 1 - comparable]
        val kotlinComparableTest = SortTest { (it as ArrayList).sort() }

        // [group 2 - comparator]
        val kotlinComparatorTest =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> Integer.compare(a.a, b.a) }) }

        // [group 3 - inline tests]
        val kotlinInlineSortByTest = SortTest { (it as ArrayList).sortBy { data -> data.a } }

        val kotlinInlineCompareByTest = SortTest { (it as ArrayList).sortWith(compareBy { data -> data.a }) }

        // [group 4 - extracted inline tests]
        val kotlinExtractedInlineComparingValuesByTest =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> compareValuesBy(a, b) { data -> data.a } }) }

        val kotlinExtractedInlineCompareValuesTest =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> compareValues(a.a, b.a) }) }

        // [group 5 - my compare values with === comparison]
        val kotlinMyNullableCompareValues1Test =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> myNullableCompareValues1(a.a, b.a) }) }

        val kotlinMyNotNullableCompareValues1Test =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> myNotNullableCompareValues1(a.a, b.a) }) }

        val kotlinMyNotNullableCompareValues2Test =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> myNotNullableCompareValues2(a.a, b.a) }) }


        // [group 6 - my compare values with no === comparison]
        val kotlinMyNullableCompareValues2Test =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> myNullableCompareValues2(a.a, b.a) }) }

        val kotlinMyNotNullableCompareValues3Test =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> myNotNullableCompareValues3(a.a, b.a) }) }

        val kotlinMyNotNullableCompareValues4Test =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> (a as Comparable<Any>).compareTo(b) }) }

        // [group 7 - int parameters]
        val kotlinIntParameters1 =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> comparingInt1(a.a, b.a) }) }

        val kotlinIntParameters2 =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> comparingInt2(a.a, b.a) }) }

        val kotlinIntParameters3 =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> comparingInt3(a.a, b.a) }) }

        val kotlinIntParameters4 =
            SortTest { (it as ArrayList).sortWith(Comparator { a, b -> comparingInt4(a.a, b.a) }) }


        // [group 5 - my compare values with === comparison] helper methods
        // ComparisonsKt.comparesValues 와 똑같다.
        private fun <T : Comparable<*>> myNullableCompareValues1(a: T?, b: T?): Int {
            if (a === b) return 0
            if (a == null) return -1
            if (b == null) return 1

            return (a as Comparable<Any>).compareTo(b)
        }

        private fun <T : Comparable<*>> myNotNullableCompareValues1(a: T, b: T): Int {
            if (a === b) return 0
            if (a == null) return -1
            if (b == null) return 1  // 여기 null 비교는 넣어도 상관 없다 always false!

            return (a as Comparable<Any>).compareTo(b)
        }

        private fun <T : Comparable<*>> myNotNullableCompareValues2(a: T, b: T): Int {
            if (a === b) return 0
            return (a as Comparable<Any>).compareTo(b)
        }

        // [group 6 - my compare values with 'no' === comparison] helper methods
        private fun <T : Comparable<*>> myNullableCompareValues2(a: T?, b: T?): Int {
            if (a == null) return -1
            if (b == null) return 1

            return (a as Comparable<Any>).compareTo(b)
        }

        private fun <T : Comparable<*>> myNotNullableCompareValues3(a: T, b: T): Int {
            return (a as Comparable<Any>).compareTo(b)
        }


        // [group 7 Int parameters]
        // 문제 있음 - auto boxing 발생함 + reference 비교 함
        private fun comparingInt1(a: Int?, b: Int?): Int {
            if (a === b) return 0
            if (a == null) return -1
            if (b == null) return 1

            return (a as Comparable<Any>).compareTo(b)
        }

        // 문제 없음 - auto boxing 발생함, null 체크 만 함 (reference 비교는 안 함)
        private fun comparingInt2(a: Int?, b: Int?): Int {
            if (a == null) return -1
            if (b == null) return 1

            return (a as Comparable<Any>).compareTo(b)
        }



        // note - For values represented by primitive types at runtime (for example, Int),
        // the === equality check is equivalent to the == check.
        //
        // see -  https://kotlinlang.org/docs/equality.html#referential-equality
        // 문제 없음 (autoboxing 없음)
        private fun comparingInt3(a: Int, b: Int): Int {
            if (a === b) return 0
            if (a == null) return -1 // always false
            if (b == null) return 1

            return (a as Comparable<Any>).compareTo(b)
        }



        // 문제 없음 (auto boxing 없음)
        private fun comparingInt4(a: Int, b: Int): Int {
            if (a == null) return -1
            if (b == null) return 1 // always false

            return (a as Comparable<Any>).compareTo(b)
        }


    }

}

package tests

import JavaTests
import helper.Data
import test.PrintUsedMemorySortTest

const val DATA_AMOUNT = 10000000

fun main() {

    val tests = listOf(
        // [group 1 - comparable]
        "group 1 - java comparable" to JavaTests.javaComparableTest,
        "group 1 - kotlin comparable" to KotlinTests.kotlinComparableTest,

        // [group 2 - comparator]
        "group 2 - java comparator" to JavaTests.javaComparatorTest,
        "group 2 - kotlin comparator" to KotlinTests.kotlinComparatorTest,

        // [group 3 - inline comparator]
        "group 3 - kotlin inline sortBy" to KotlinTests.kotlinInlineSortByTest,
        "group 3 - kotlin inline compareBy" to KotlinTests.kotlinInlineCompareByTest,

        // [group 4 - extracted inline comparator]
        "group 4 - kotlin extracted inline comparing values by" to KotlinTests.kotlinExtractedInlineComparingValuesByTest,
        "group 4 - kotlin extracted inline compare values" to KotlinTests.kotlinExtractedInlineCompareValuesTest,

        // [group 5 - my compare values with === comparison]
        "group 5 - kotlin my nullable compare values1 test" to KotlinTests.kotlinMyNullableCompareValues1Test,
        "group 5 - kotlin my not nullable compare values1" to KotlinTests.kotlinMyNotNullableCompareValues1Test,
        "group 5 - kotlin my not nullable compare values2" to KotlinTests.kotlinMyNotNullableCompareValues2Test,

        // [group 6 - my compare values with no === comparison]
        "group 6 - kotlin my nullable compare values2" to KotlinTests.kotlinMyNullableCompareValues2Test,
        "group 6 - kotlin my not nullable compare values3" to KotlinTests.kotlinMyNotNullableCompareValues3Test,
        "group 6 - kotlin my not nullable compare values4" to KotlinTests.kotlinMyNotNullableCompareValues4Test,

        // [group 7 - Int, Int?]
        "group 7 - kotlin comparing int 1" to KotlinTests.kotlinIntParameters1,
        "group 7 - kotlin comparing int 2" to KotlinTests.kotlinIntParameters2,
        "group 7 - kotlin comparing int 3" to KotlinTests.kotlinIntParameters3,
        "group 7 - kotlin comparing int 4" to KotlinTests.kotlinIntParameters4,
        )
        .map { (name, test) -> PrintUsedMemorySortTest(name, test) }

    repeat(10) {
        val data = List(DATA_AMOUNT) { Data() }

        tests.shuffled().forEach { it.dummyRun(data) }
        tests.shuffled().forEach { it.runSortTestOn(data) }
        println()
    }

    tests.sortedByDescending { it.totalUsedMemory }
        .forEach { it.printTotalMemoryUsage() }
}


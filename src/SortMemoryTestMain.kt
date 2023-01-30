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

        // [group 3 -  inline tests]
        "group 3 - kotlin inline sortBy" to KotlinTests.kotlinInlineSortByTest,
        "group 3 - kotlin inline compareBy" to KotlinTests.kotlinInlineCompareByTest,

        // [group 4 - extracted inline tests]
        "group 4 - kotlin extracted inline comparing values by test" to KotlinTests.kotlinExtractedInlineComparingValuesByTest,
        "group 4 - kotlin extracted inline compare values test" to KotlinTests.kotlinExtractedInlineCompareValuesTest,

        // [group 5 - extracted inline comparable casting tests]
        "group 5 - kotlin extracted inline comparable casting test" to KotlinTests.kotlinExtractedInlineComparableCastingTest,
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


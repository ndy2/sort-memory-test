package tests

import JavaTests
import helper.Data
import test.PrintUsedMemorySortTest

const val DATA_AMOUNT = 1000000

fun main() {

    val tests = listOf(
        "java comparable" to JavaTests.javaComparableTest,
        "java comparator" to JavaTests.javaComparatorTest,
        "kotlin comparable" to KotlinTests.kotlinComparableTest,
        "kotlin sortBy" to KotlinTests.kotlinSortByTest,
        "kotlin inline compareBy" to KotlinTests.kotlinInlineCompareByTest,
        "kotlin outline compareBy" to KotlinTests.kotlinOutlineCompareByTest
    )
        .map { (name, test) -> PrintUsedMemorySortTest(name, test) }

    repeat(10) {
        val data = MutableList(DATA_AMOUNT) { Data() }
        tests.shuffled().forEach { it.dummyRun(data) }
        tests.shuffled().forEach { it.runSortTestOn(data) }
        println()
    }

    tests.sortedByDescending { it.totalUsedMemory }
        .forEach { it.printTotalMemoryUsage() }
}


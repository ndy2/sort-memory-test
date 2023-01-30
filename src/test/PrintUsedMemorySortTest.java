package test;

import helper.Data;

import java.util.List;

public class PrintUsedMemorySortTest implements SortTest {
    public String name;
    public SortTest target;

    public long totalUsedMemory;

    public PrintUsedMemorySortTest(String name, SortTest sortTest) {
        this.name = name;
        this.target = sortTest;
    }

    @Override
    public void runSortTestOn(List<Data> data) {
        Runtime.getRuntime().gc();

        target.runSortTestOn(data);

        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        totalUsedMemory += usedMemory;
        System.out.printf("[%s] %d Mb\n", name, usedMemory / (1024 * 1024));
    }

    // run for memory loading
    public void dummyRun(List<Data> data) {
        target.runSortTestOn(data);
    }

    public void printTotalMemoryUsage() {
        System.out.printf("[%s] %d Mb\n", name, totalUsedMemory / (1024 * 1024));
    }
}

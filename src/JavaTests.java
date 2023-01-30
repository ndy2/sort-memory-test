import test.SortTest;

public class JavaTests {

    // [group 1 - comparable]
    public static SortTest javaComparableTest = data -> data.sort(null);

    // [group 2 - comparator]
    public static SortTest javaComparatorTest = data -> data.sort((o1, o2) -> Integer.compare(o1.a, o2.a));
}

import test.SortTest;

public class JavaTests {
    public static SortTest javaComparableTest = data -> data.sort(null);

    public static SortTest javaComparatorTest = data -> data.sort((o1, o2) -> Integer.compare(o1.a, o2.a));
}

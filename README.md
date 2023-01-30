### Java & Kotlin 의 정렬 API 메모리 사용량 실험

여러 방식으로 구성된 정렬 API 를 통해 랜덤 숫자 천 만개 10000000개를 정렬 하고 사용된 메모리 총량을 확인해보자!

### 진행 방식

랜덤한 숫자 10000000 개를 생성하여 이를 `Data` 에 담고 정렬한다.

공정한 실험을 위해 한번의 반복에 대해 같은 데이터를 모든 테스트에 제공하였으며
JVM 로딩을 고려하여 dummy run 을 모두 한번씩 해당 데이터로 돌려주고 순서도 램덤으로 하였다.

테스트는 총 18 가지 방식으로 수행하였다. 아래와 같은 그룹으로 나누었다.
각각의 정렬 api 활용 방식은 `JavaTests.java` 와 `KotlinTests.kt` 에서 확인 할 수 있다.

[group 1 - comparable]

1. java comparable
2. kotlin comparable

[group 2 - comparator]

3. java comparator
4. kotlin comparator

[group 3 - inline comparator]

5. kotlin inline sortBy
6. kotlin inline compareBy

[group 4 - extracted inline comparator]

7. kotlin extracted inline comparing values by
8. kotlin extracted inline compare values

[group 5 - my compare values with === comparison]

9. kotlin my nullable compare values1 test
10. kotlin my not nullable compare values1
11. kotlin my not nullable compare values2

[group 6 - my compare values with no === comparison]

12. kotlin my nullable compare values2
13. kotlin my not nullable compare values3
14. kotlin my not nullable compare values4

[group 7 - Int parameters]

15. comparingInt1
16. comparingInt2
17. comparingInt3
18. comparingInt4

사실 각 그룹이 각각 생성하는 바이트 코드는 거의 유사하다.
하지만 문제의 소지를 명확하게 하기 위해 단계를 나누어 여러 방식으로 코드를 작성해 보았다.

[group 1 ~ 4] 는 기본적인 java 와 kotlin 의 comparable 과 comparator 을 이용한 정렬이다.
[group 5] 번은 가장 처음 문제를 발견하였던 kotlin 의 `sortBy` 정렬 api 를 이용한 것이고
아래로 갈 수록 해당 api 를 점점 풀어 해친 며 여러 형태로 실험해본 것들이다.

---

### 실행 결과
```
[group 3 - kotlin inline compareBy] 4258 Mb
[group 3 - kotlin inline sortBy] 4258 Mb
[group 4 - kotlin extracted inline comparing values by] 4258 Mb
[group 4 - kotlin extracted inline compare values] 4258 Mb
[group 5 - kotlin my not nullable compare values1] 4258 Mb
[group 5 - kotlin my not nullable compare values2] 4258 Mb
[group 7 - kotlin comparing int 1] 4258 Mb
[group 5 - kotlin my nullable compare values1 test] 4257 Mb

[group 1 - java comparable] 2738 Mb
[group 1 - kotlin comparable] 2738 Mb
[group 2 - java comparator] 2738 Mb
[group 2 - kotlin comparator] 2738 Mb
[group 6 - kotlin my not nullable compare values3] 2738 Mb
[group 6 - kotlin my nullable compare values2] 2738 Mb
[group 6 - kotlin my not nullable compare values4] 2738 Mb
[group 7 - kotlin comparing int 2] 2738 Mb
[group 7 - kotlin comparing int 3] 2738 Mb
[group 7 - kotlin comparing int 4] 2738 Mb
```

PS. 여러번 실행하여도 꽤 일정한 경향성을 보인다.

### 결론

메모리 사용량 - [group 1] = [group 2] = [group 6] < [group 3] = [group 4] = [group 5]

PS. 100 만개의 데이터로 실험 해도 항상은 아니지만 많은 경우 비슷한 경향성을 보인다. 그보다 작은 데이터에 대해서는 메모리 사용량 차이가 크게 의미 없다.

### 그 이유는?

사실 처음에는 이 문제를 코틀린에서 발견하고 문제가 Comparable 과 Comparator 의 차이에서 발생하는 줄 알았다. Arrays.sort 코드를 따라가다보니 Comparable 은
`java.util.ComparableTimsort` 를 사용하고 Comparator 케이스는 `java.util.Timsort` 를 사용하는데 주석을 보니 성능 이야기도 조금 있고 해서 그렇게 결론 내리려고
했었는데 자바로도 실험해 보고 또한 다시 생각해보니 두 팀소트 구현의 차이에는 Comparator 를 재활용한다는 전제하에 Space Complexity 의 차이가 없어야 했다.

그렇게 실험을 해보고 디버깅을 계속 해보다 보니 그 다음에는 Inline 함수에 뭔가 문제가 있는 줄 알았다. Comparator 를 inline 으로 넘겨서 계속 `compareBy { }` 를 호출하고 이를 통해
Comparator 가 계속 생성되는 줄 알았다.
람다식에 inline method 까지 포함한 stack trace 를 디버깅하는 것이 익숙하지 않아서 자꾸 햇갈리고 그렇게 결론을 내리려고 했었다.

그런데 이를 group 6 케이스 까지 분해해 놓고 보니 문제의 원인을 확실히 알 수 있었다. 그 이유는 바로바로 `kotlin.comparisons` 패키지 `Comparisons.Kt` 파일의 _
compareValues_ 메서드였다. 이 메서드는 nullable 한, Comparable 을 구현한 T 타입의 인자를 두개 입력 받는다. 이를 === 비교 해 보고 같다면 0 그리고 주석에 있듯이 null 값을
최소 로 치기 때문에 그에 대한 분기문이 세 줄 나오고 먀지막으로 Comparable._compareTo_ 를 이용한다.

`kotlin.comparisons - Comparsions.Kt#compareValues`

```kotlin
/**
 * Compares two nullable [Comparable] values. Null is considered less than any value.
 *
 * @sample samples.comparisons.Comparisons.compareValues
 */
public fun <T : Comparable<*>> compareValues(a: T?, b: T?): Int {
    if (a === b) return 0
    if (a == null) return -1
    if (b == null) return 1

    @Suppress("UNCHECKED_CAST")
    return (a as Comparable<Any>).compareTo(b)
}
```
앞으로 메모리를 많이 사용 하는 것을 문제라고 하겠다. 문제가 나오는 부분을 명확하게 하기 위해 해당 메서드를 그대로 복사하여 조금 씩 변경해보며 문제가 터지는 원인을 찾고자 하였다. **_문제는 바로 === 비교이다_**. [group 5] 에는 문제가 발생하고 [group 6]
에는 문제가 발생하지 않는데 두 그룹의 차이는 바로 === 비교를 하는지 이다.

### 자세한 이유를 알아보기 위해 바이트 코드를 까보자

문제의 경계인 group 5 와 group 6 의 helper method 의 bytecode 를 들여다 보자.

#### 1. group 5 - 문제 있음!

```java 
public static final class Companion {
    // ...
    // [group 5]
    private final int myNullableCompareValues1(Comparable a, Comparable b) {
         if (a == b) {
            return 0;
         } else if (a == null) {
            return -1;
         } else {
            return b == null ? 1 : a.compareTo(b);
         }
    }
    
    private final int myNotNullableCompareValues1(Comparable a, Comparable b) {
     if (a == b) {
        return 0;
     } else if (a == null) {
        return -1;
     } else {
        return b == null ? 1 : a.compareTo(b);
     }
    }
    
    private final int myNotNullableCompareValues2(Comparable a, Comparable b) {
     if (a == b) {
        return 0;
     } else if (a == null) {
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Comparable<kotlin.Any>");
     } else {
        return a.compareTo(b);
     }
    }
    
    // [group 7 의 문제 있는 코드]
    private final int comparingNullableInt1(Integer a, Integer b) {
        if (a == b) {
            return 0;
        } else if (a == null) {
            return -1;
        } else {
            return b == null ? 1 : ((Comparable) a).compareTo(b);
        }
    }
}
```

#### 2. group 6 - 문제 없음!

```java
public static final class Companion {
    // ...
    // [group 6]

    private final int myNullableCompareValues2(Comparable a, Comparable b) {
        if (a == null) {
            return -1;
        } else {
            return b == null ? 1 : a.compareTo(b);
        }
    }

    private final int myNotNullableCompareValues3(Comparable a, Comparable b) {
        if (a == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Comparable<kotlin.Any>");
        } else {
            return a.compareTo(b);
        }
    }

    // [group 7 의 문제 없는 코드]
    private final int comparingNullableInt2(Integer a, Integer b) {
        if (a == null) {
            return -1;
        } else {
            return b == null ? 1 : ((Comparable) a).compareTo(b);
        }
    }

    private final int comparingNullableInt3(int a, int b) {
        return a == b ? 0 : Intrinsics.compare(a, b);
    }

    private final int comparingNullableInt4(int a, int b) {
        return Intrinsics.compare(a, b);
    }
}
```

### 바이트 코드를 확인해본 결과

자세한 이유를 모르겠다. Autoboxing 에 관한 문제 일 것이라고 예상 했는데 [group 7] 의 `comparingNullableInt2` 를 보면 auto boxing 이 발생하였음에도 메모리를 적게 사용 하였다. 사실 문제가 없는 [group 6] 의 경우에도 바이트 코드를 보면 Comparable 타입을 전달 받기 때문에 각각의 `코틀린 Int` 가 Primitive 타입으로 전달 될 것이라고 생각되지는 않는데 메모리를 적게 사용한다.

### 결론

이유는 잘 모르겠지만 문제가 발생하는 상황을 정리해 보면 `kotlin.comparisons - Comparsions.Kt#compareValues` Nullable 한 아규먼트에 Primitive 한 값을 전달하여 참조 비교를 하는 경우 메모리를 낭비 할 수 있다. 특히 정렬 API 를 사용할때 앞으로 `compareValues` 를 기반으로 한 `compareBy` 나 `sortBy` 사용을 주의해야 겠다. 사실 뭔가 AutoBoxing 과 관련된 문제가 맞는것 같은데 확실하게 확인 하는 법을 모르겠다. 사실 위 바이트 코드만 보고 auto boxing 발생 여부를 판단 할 수 있는지도 확실하지 않다.

개인적으로 PS 할때 `Comparable` 을 확장 하면 코드가 더 길어져서 `sortBy` 를 애용 했었는데 이런 문제가 있을 줄 몰랐다...
앞으로는 그냥 [group 2] 방식 처럼 Comparator 를 직접 생성하여 전달해야겠다.







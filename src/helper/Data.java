package helper;

public class Data implements Comparable<Data> {

    public int a;
    public int b;
    public int c;

    public Data() {
        this.a = Util.nextInt();
        this.b = Util.nextInt();
        this.c = Util.nextInt();
    }

    @Override
    public int compareTo(Data o) {
        return Integer.compare(a, o.a);
    }
}

// tests for compile time failure on mismatched types in arithmetic
class cse401c_ctfail_15 {
    public static void main(String[] args) {
        System.out.println(new TestMismatchTypes().test(1, 1.0));
    }
}

class TestMismatchTypes {
    public int test(int x, double y) {
        int test1;
        double test2;
        test1 = x;
        test2 = y;
        return test1 + test2;
    }
}
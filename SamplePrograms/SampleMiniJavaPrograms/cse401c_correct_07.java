// tests int variables
class cse401c_correct_07 {
    public static void main(String[] args) {
        System.out.println(new TestIntVars().test(0));
    }
}

class TestIntVars {
    public int test(int x) {
        int a;
        a = x;
        System.out.println(a);
        a = a + 1;
        return a;
    }
}
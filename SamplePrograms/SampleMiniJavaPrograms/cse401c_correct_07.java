// tests int variables
class cse401c_correct_07 {
    public static void main(String[] args) {
        System.out.println(new TestIntVars().test(0));
    }
}

class TestIntVars {
    public int test(int x) {
        System.out.println(x);
        return x+1;
    }
}
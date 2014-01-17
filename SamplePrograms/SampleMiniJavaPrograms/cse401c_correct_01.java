// test to see if integer arithmetic works on int constants

class cse401c_correct_01 {
    public static void main(String[] args) {
        System.out.println(new TestIntLiteralArithmetic().test());
    }
}

class TestIntLiteralArithmetic {
    public int test() {
        System.out.println(1 + 1); // 2
        System.out.println(3 - 2); // 1
        System.out.println(2 * 2); // 4
        System.out.println(2 / 2); // 1
        System.out.println(3 % 2); // 1
        return 1;
    }
}
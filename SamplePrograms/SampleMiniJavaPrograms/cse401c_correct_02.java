// test if/then/else with integer constants
class cse401c_correct_02 {
    public static void main(String[] args) {
        System.out.println(new TestIntLiteralCompareBranch().test());
    }
}

class TestIntLiteralCompareBranch {
    public int test() {
        if (1 == 1) {
            return 1;
        } else {
            return 0;
        }
    }
}
// test while statements with int literals
class cse401c_correct_03 {
    public static void main(String[] args) {
        System.out.println(new TestWhileLoopIntLiteral().test());
    }
}

class TestWhileLoopIntLiteral {
    public int test() {
        int i;
        i = 0;
        while (i < 5) {
            i = i + 1;
            if (i == 5) {
                System.out.println(i);
            } else {
                System.out.println(i);
            }
        }
        return 5;
    }
}
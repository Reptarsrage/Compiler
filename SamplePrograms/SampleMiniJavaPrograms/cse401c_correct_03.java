// test while statements with int literals
class cse401c_correct_03 {
    public static void main(String[] args) {
        System.out.println(new TestWhileLoopIntLiteral().test());
    }
}

class TestWhileLoopIntLiteral {
    public int test() {
        int i = 0;

        while (1) {
            i ++;
            if (i == 5) {
                break;
            } else {
                System.out.println(i);
            }
        }
        return 5;
    }
}
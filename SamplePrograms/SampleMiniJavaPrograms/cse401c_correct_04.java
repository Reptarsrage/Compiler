// test some basic object functionality such as fields, constructors, methods
class cse401c_correct_04 {
    public static void main(String[] args) {
        System.out.println(new TestObjects(100).value());
    }
}

class TestObjects {
    private int value;

    public TestObjects(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}


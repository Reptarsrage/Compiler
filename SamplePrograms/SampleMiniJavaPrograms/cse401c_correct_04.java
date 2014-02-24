// test some basic object functionality such as fields, constructors, methods
class cse401c_correct_04 {
    public static void main(String[] args) {
        System.out.println(new TestObjects().run(100));
    }
}

class TestObjects {
    public int run(int v) {
        Obj o;
        int res;
        o = new Obj();
        res = o.setValue(v);
        return o.getValue();
    }
}

class Obj {
    int value;

    //public TestObjects(int value) {
    //    this.value = value;
    //}

    public int setValue(int v) {
        value = v;
        return value;
    }

    public int getValue() {
        return value;
    }
}
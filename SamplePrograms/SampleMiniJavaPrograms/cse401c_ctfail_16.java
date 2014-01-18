// tests to make sure minijava compiler doesnt allow overloading
// note, java will allow this

class cse401c_ctfail_16 {
    public static void main(String[] args) {
        System.out.println(new TestOverload().a(0));
    }
}

class TestOverload {
    public int a() {
        return 1;
    }

    public int a(int x) {
        return x;
    }
}

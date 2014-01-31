// test to make sure a non existant method fails

class cse401c_ctfail_13 {
	public static void main(String[] args) {
        System.out.println(new TestFailure().test());
	}
}

class TestFailure {
	public int test1() {
		System.out.println(1);
        return 1;
	}
}

// test to make sure a non existant method fails

class cse401c_ctfail_13 {
	public static void main(String[] args) {
		TestFailure test = new TestFailure();
		test.test();
	}
}

class TestFailure {
	public void test1() {
		System.out.println(1);
	}
}

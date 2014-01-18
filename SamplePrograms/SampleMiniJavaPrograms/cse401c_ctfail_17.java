// test to make sure method must have return statement

class cse401c_ctfail_17 {
	public static void main(String[] args) {
		TestFailure test = new TestFailure();
		test.test();
	}
}

class TestFailure {
	public int test() {
		System.out.println(1);
	}
}
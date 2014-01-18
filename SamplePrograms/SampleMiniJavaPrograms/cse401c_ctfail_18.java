// test to make sure there is no unreachable code

class cse401c_ctfail_18 {
	public static void main(String[] args) {
		TestFailure test = new TestFailure();
		test.test();
	}
}

class TestFailure {
	public int test() {
		return 1;
		System.out.println(1);
	}
}
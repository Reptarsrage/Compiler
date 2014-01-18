// test to make sure method cannot return something if it is declared void

class cse401c_ctfail_20 {
	public static void main(String[] args) {
		TestFailure test = new TestFailure();
		test.test();
	}
}

class TestFailure {
	public void test() {
		return 1;
		System.out.println(1);
	}
}
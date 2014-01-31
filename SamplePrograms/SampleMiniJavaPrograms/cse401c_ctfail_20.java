// test to make sure method must return correct type

class cse401c_ctfail_20 {
	public static void main(String[] args) {
        System.out.println(new TestFailure().test());
	}
}

class TestFailure {
	public double test() {
		return 1;
	}
}
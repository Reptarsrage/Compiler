// test to make sure method must have return statement (parse fail)

class cse401c_ctfail_17 {
	public static void main(String[] args) {
        System.out.println(new Test().test());
	}
}

class TestFailure {
	public int test() {
		System.out.println(1);
	}
}
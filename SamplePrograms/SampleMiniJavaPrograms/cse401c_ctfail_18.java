// test to make sure there is no unreachable code -- fail at parse time?

class cse401c_ctfail_18 {
	public static void main(String[] args) {
        System.out.println(new Test().test());
	}
}

class TestFailure {
	public int test() {
		return 1;
		System.out.println(1);
	}
}
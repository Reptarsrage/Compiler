// test to make sure unrecognized identifiers are caught

class cse401c_ctfail_21 {
	public static void main(String[] args) {
		System.out.println(new Test().test());
	}
}

class Test {
    public int test() {
		foo i;
        return 1;
    }
}

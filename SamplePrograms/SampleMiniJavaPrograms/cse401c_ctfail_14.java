// test to make sure two variables with the same name cannot exist together in one scope

class cse401c_ctfail_14 {
	public static void main(String[] args) {
		System.out.println(new Test().test());
	}
}

class Test {
    public int test() {
        int i;
		int i;
        i = 0;
        i = 0;
		return i;
    }
}

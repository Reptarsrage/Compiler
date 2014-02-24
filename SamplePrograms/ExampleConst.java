// test to make sure a uninitiated variable fails

class cse401c_ctfail_11 {
	public static void main(String[] args) {
		System.out.println(new Test().test());
	}
}

class Test {
    public int test() {
        int unin;
        unin = unin + 1;
        return unin;
    }
}
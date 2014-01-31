// test to make sure operators don't work on objects there not supposed to work on

class cse401c_ctfail_19 {
	public static void main(String[] args) {
        System.out.println(new Test().test());
	}
}

class Test {
    public int test() {
        int[] i;
		int[] j;
        i = new int[10];
        j = new int[10];
		i = j / i;
        return i;
    }
}
// test to make sure a runtime failure exists (array out of bounds)

class cse401c_rtfail_12 {
	public static void main(String[] args) {
        System.out.println(new TestUnin().init());
	}
}

class TestUnin {
    public int init() {
        int[] arr;
        arr = new int[1];
        return this.test(arr);
    }

	public int test(int[] i) {
		int count;
        count = 0;
		while (true){
			i[count] = 1;
			count = count + 1;
		}
        return 1;
	}
}

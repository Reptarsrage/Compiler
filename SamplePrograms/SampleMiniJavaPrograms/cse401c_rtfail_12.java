// test to make sure a runtime failure exists

class cse401c_rtfail_12 {
	public static void main(String[] args) {
		int[] arr = new int[1];
		TestUnin test = new TestUnin();
		test.test(arr);
	}
}

class TestUnin {
	public void test(int[] i) {
		int count = 0;
		while (true){
			i[count] = 1;
			count = count + 1;
		}
	}
}

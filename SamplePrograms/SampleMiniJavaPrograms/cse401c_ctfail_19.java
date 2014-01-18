// test to make sure operators don't work on objects there not supposed to work on

class cse401c_ctfail_19 {
	public static void main(String[] args) {
		int[] i = new int[10];
		int[] j = new int[10];
		i = j / i;
		System.out.println(i);
	}
}
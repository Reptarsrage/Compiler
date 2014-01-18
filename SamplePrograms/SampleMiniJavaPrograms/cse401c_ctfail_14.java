// test to make sure two variables with the same name cannot exist together in one scope

class cse401c_ctfail_14 {
	public static void main(String[] args) {
		int i = 0;
		int i = 0;
		System.out.println(i);
	}
}

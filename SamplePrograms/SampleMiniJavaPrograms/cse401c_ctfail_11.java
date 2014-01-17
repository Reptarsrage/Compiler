// test to make sure a uninitiated variable fails

class cse401c_ctfail_11 {
	public static void main(String[] args) {
		int unin;
		unin = unin + 1;
		System.out.print(unin);
	}
}

class ExampleConst {
  public static void main(String[] a) {
    if (1){
		display(6);
		display(1 + 5);
		display(7 - 1);
		display(2 * 3);
		display(18 / 3);
		display(27 % 7);
	} else {
		display(23 > 8);
		display(8 < 23);
		display(8 <= 8);
		display(9 >= 9);
		display(10 == 10);
		display(11 != 10);
		display(!(12 == 11));
		display((12 == 12) && (5 > 2));
		display((5 != 4) || (6 != 6));
	}
  }
}

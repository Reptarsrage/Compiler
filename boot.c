/*
 *  boot.c: Main program for CSE minijava compiled code
 *          Robert R. Henry
 *
 *  Contents:
 *    Main program that calls the compiled code as a function;
 *    Functions get and put that can be used by compiled code
 *      for integer I/O;
 *    Function mjmalloc to allocate bytes for minijava's new operator
 *
 *  Additional functions used by compiled code can be added as desired.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#include "number_converter.h"

/*
 * This is the main function in compiled code
 * Change the function name here if your
 * compiled MiniJava main entry point has a different label.
 */
extern void asm_main(void);

/*
 * Prompt for input, then return
 * next integer from standard input.
 * (This is not used in MiniJava.)
 */
int64_t get(void)
{
  int64_t k;
  printf("get: ");
  scanf("%lld", &k);
  return k;
}

/*
 * Array errors
 *
 *
 */
void check_bounds(int64_t * addr, int64_t index, int64_t line_number) {
	int64_t length = *(addr - 1);
	if (index >= length || index < 0) {
		printf("%s%d%s\n", "Runtime Error on line: ", line_number,". Array out of bounds exception!");
		exit(1);
	}
}

void check_initial_bounds(int64_t length, int64_t line_number) {
	if (length < 0) {
	  printf("%s%d%s\n", "Runtime Error on line: ", line_number,". A new array must have non-negative size!");
		exit(1);
	}
}

/*
 * Write an int64_t x to standard output.
 * This emulates what java's System.out.println does when given an integer,
 * so we can diff the output of the known-good expect run
 * with the actual output of the mini java compiler.
 */
void put(int64_t x)
{
  printf("%lld\n", x);
}

void put_double(double d)
{
  char buf[BUFSIZ];
  convert_double(d, buf, sizeof(buf));
  printf("%s\n", buf);
}

/*
 * The function mjmalloc returns a pointer to a chunk of memory
 * at least num_bytes large.
 * Returns NULL if there is insufficient memory available.
 */
int64_t *mjmalloc(size_t num_bytes)
{
  return (int64_t *)calloc(num_bytes, sizeof(char));
}

/*
 * Execute the compiled mini Java program asm_main.
 */
int main(void)
{
  asm_main();
  return 0;
}

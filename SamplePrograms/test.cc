struct A {
   void f(int i) { i++; }
};

struct B: A {
   void f(int i) { i++; }
};

void g(A& arg) {
   arg.f(99);
}

int main() {
   B x;
   g(x);
}
* [31mf29bc3d	[32m (origin/master, origin/HEAD)[0m DONE WITH ASSIGNMET 5, added boolean literals to gencode. [33m(Justin Robb)[0m
* [31m22229a0	[32m[0m bracketing comments now appear in generated code [33m(Adam Croissant)[0m
* [31ma6c4b71	[32m[0m fixed blocks in mainclass [33m(Adam Croissant)[0m
*   [31m2da1bf5	[32m[0m Merge branch 'master' of https://bitbucket.org/reptarsrage/401_project [33m(Adam Croissant)[0m
[32m|[m[33m\[m  
[32m|[m * [31m3477a00	[32m[0m Fixed shortcircuitand and or for GOOD. ass5 test still fails. [33m(Justin Robb)[0m
[32m|[m * [31md22d6db	[32m[0m Changed all uses of rbx as the secondary register to rcx. [33m(Justin Robb)[0m
[32m|[m * [31mb627b27	[32m[0m Fixed order of parameters for function calls. [33m(Justin Robb)[0m
* [33m|[m [31m92f4470	[32m[0m fixed nested curly braces in main class issue in parser's grammar [33m(Adam Croissant)[0m
[33m|[m[33m/[m  
* [31m775f83c	[32m[0m Fixed issues with function calls [33m(Justin Robb)[0m
* [31mf0c6d2b	[32m (HEAD, master)[0m Visitor now calls code gen for function exit [33m(Adam Croissant)[0m
* [31m995cef6	[32m[0m Added code gen for moving params in registers to stack on callee side (method entrance) [33m(Adam Croissant)[0m
* [31mcdbbd6b	[32m[0m fixed issue with params -> registers on call, made so compiler crashes if more than 6 args are passed to a function [33m(Adam Croissant)[0m
* [31ma8d1541	[32m[0m Got code generation for method call done (popping args into registers and calling then storing return) [33m(Adam Croissant)[0m
* [31m2ffee80	[32m[0m Added short circuit or. Done with control flow section. [33m(Justin Robb)[0m
* [31m0fb7ec2	[32m[0m Added while to codegen. [33m(Justin Robb)[0m
* [31m67b5e07	[32m[0m Fixed short circuit and issues, added labelcounter, added ifelse statement to codegen. [33m(Justin Robb)[0m
* [31m7de31c7	[32m[0m made And short circuited [33m(Adam Croissant)[0m
* [31m4065181	[32m[0m Added and and or, we still need to make them short circuit. [33m(Justin Robb)[0m
* [31mfd2efac	[32m[0m Added integerliteral to codegen, fixed a scanner error with notequals. Programs now can be built and executed using all operators. [33m(Justin Robb)[0m
* [31m1a80f83	[32m[0m Fixed minor issues in codegenMain and codegenvisitor. [33m(Justin Robb)[0m
* [31m8688a97	[32m[0m Added codegen for opporators minus, times, divide, mod, equals, notequals, less, greater, lessorequal, greaterorequal and not. [33m(Justin Robb)[0m
* [31m522cd3b	[32m[0m prepping for turnin of Assignment 4, also changed prettyprint to add a new line before method declarations [33m(Adam Croissant)[0m
* [31m2282207	[32m[0m log with correct results [33m(Adam Croissant)[0m
* [31m06af7e0	[32m[0m changed MainClass to use a statement list so that Assignment4.java would parse correctly [33m(Adam Croissant)[0m
* [31m321a45d	[32m[0m changed test-scanner and test-parser targets to use SampleMiniJavaPrograms/Assignment4.java  . test-parser also prints output to Assignment.output [33m(Adam Croissant)[0m
* [31mec822fb	[32m[0m modified test 08 to check mainclass with multiple statements [33m(Adam Croissant)[0m
* [31m78fc3cf	[32m[0m fixed rtfail_12 [33m(Adam Croissant)[0m
* [31md7d3a2c	[32m[0m fixed ctfail_13 and ctfail_11 [33m(Adam Croissant)[0m
* [31me3aeda1	[32m[0m fixed ctfail_14 and ctfail_15 [33m(Adam Croissant)[0m
* [31mdad9d15	[32m[0m fixed ctfail_17 & ctfail_18 -- both fail at parse time due to incorrect syntax (they are supposed to) [33m(Adam Croissant)[0m
* [31m236e671	[32m[0m got ctfail_19 working [33m(Adam Croissant)[0m
* [31m9bc41ab	[32m[0m fixed test programs 21 and 20 [33m(Adam Croissant)[0m
* [31m8aa2ea3	[32m[0m Fixed edge cases for if and while statements, got rid of unecessary comments. [33m(Justin Robb)[0m
* [31m4e2af44	[32m[0m fixed the cup file to allow Pi.java to parse - had to uncomment one of the rules, also uncommented one other that was similar, to allow Pi to work [33m(Adam Croissant)[0m
* [31m03e1a6b	[32m[0m forgot to commit cup file [33m(Adam Croissant)[0m
* [31m9cc5f83	[32m[0m got everything but Pi.java and the cse401c_*tfail files working. Removed Parens requirements from classDeclarations with extends after looking at provided test minijava code in TreeVisitor [33m(Adam Croissant)[0m
* [31m10d3659	[32m[0m fixed correct_05 - you have to surround extends statements with parens. wierd. [33m(Adam Croissant)[0m
* [31m526682c	[32m[0m got all working up to correct_10, except for correct_05 [33m(Adam Croissant)[0m
* [31mbb0c876	[32m[0m got tests 01-04 & 06 working, cant get 05 to work [33m(Adam Croissant)[0m
* [31m6be0a66	[32m[0m added if else curly braces and fixed errors. [33m(Justin Robb)[0m
* [31m26f40b6	[32m[0m made output of test-parser easier to read. [33m(Justin Robb)[0m
* [31m7987c1f	[32m[0m fixed case with formal not returning a result. [33m(Justin Robb)[0m
* [31m0791b56	[32m[0m Finally got parser working for while statements. Added integer literals to scanner and parser. [33m(Justin Robb)[0m
* [31m92794e4	[32m[0m Added cases for all lists handling the empty list case. Added more to while statement. [33m(Justin Robb)[0m
* [31m72a72ed	[32m[0m removed comment.java from AST [33m(Justin Robb)[0m
* [31m6d99153	[32m[0m Fixed comments. They are no longer parsed. [33m(Justin Robb)[0m
* [31m1e0b29b	[32m[0m IMPORTANT: test-parser-all was not working correctly. It now is. [33m(Justin Robb)[0m
* [31m695cc57	[32m[0m added a real definition for a program. Modified testParser to handle this new definition. [33m(Justin Robb)[0m
* [31m109581c	[32m[0m added test parser all command to build xml file. [33m(Justin Robb)[0m
* [31mef3aa85	[32m[0m wrote parser code for all of the lists, IMPORTANT-cant get Statement ::= StatementList to work [33m(Adam Croissant)[0m
* [31m7938aa9	[32m[0m Fixed capitolization of System out println, added TODO statement where needed. [33m(Justin Robb)[0m
* [31m3168e7a	[32m[0m Added main to scanner, added classdeclaration and mainclass to parser. [33m(Justin Robb)[0m
* [31m7ecd91b	[32m[0m Added commented out missing statement. [33m(Justin Robb)[0m
* [31mdada927	[32m[0m added skeleton for higher level methoddeclaration, added skeleton code for other higher level lists. [33m(Justin Robb)[0m
*   [31m5a5f2c4	[32m[0m Merge branch 'master' of https://bitbucket.org/reptarsrage/401_project [33m(Adam Croissant)[0m
[34m|[m[35m\[m  
[34m|[m * [31m645d229	[32m[0m Added all statements except for the statement list case. [33m(Justin Robb)[0m
[34m|[m * [31m25071c8	[32m[0m Precedence of oporators fixed problems with the Not expression. Added Not back into Expressions. [33m(Justin Robb)[0m
[34m|[m * [31m90a9945	[32m[0m added all precedences. Added not equal to scanner and parser. [33m(Justin Robb)[0m
[34m|[m * [31m1cb0677	[32m[0m Added types and variable declarations. Still need to implement higher level declarations for parser to work. [33m(Justin Robb)[0m
[34m|[m * [31m4580342	[32m[0m Added the rest of the expressions (escept for not). Expressions NOT working properly at this point but I believe we just need to implement the rest of the parser to fix this. [33m(Justin Robb)[0m
[34m|[m * [31m6302706	[32m[0m Added comments to parser. IMPORTANT: issue with chaining comparioson statements not an issue for some reason. [33m(Justin Robb)[0m
[34m|[m * [31mf583c44	[32m[0m added <, >, <=, >=, and == to the parser. IMPORTANT: we must figure out a way to fix chaining these. [33m(Justin Robb)[0m
[34m|[m * [31m63ade22	[32m[0m Added * / and - oporators to parser. [33m(Justin Robb)[0m
[34m|[m *   [31m7338e96	[32m[0m Merge remote-tracking branch 'template/master' [33m(Justin Robb)[0m
[34m|[m [36m|[m[1;31m\[m  
[34m|[m [36m|[m * [31m2894ac5	[32m (template/master)[0m Fix test-parser target [33m(Nathaniel Mote)[0m
[34m|[m * [1;31m|[m [31m497d0d1	[32m[0m added , to scanner, fixed bug with double. Scanner finished, correctly scans all example programs. [33m(Justin Robb)[0m
[34m|[m * [1;31m|[m [31m7b7ef75	[32m[0m split up .lengh into two seperate tokens of . and length in scanner. [33m(Justin Robb)[0m
* [1;31m|[m [1;31m|[m [31mda85149	[32m[0m pushing up parser from 333 as example [33m(Adam Croissant)[0m
[1;31m|[m[1;31m/[m [1;31m/[m  
* [1;31m|[m [31mec4fbb4	[32m[0m fixed jflex compile error. [33m(Justin Robb)[0m
* [1;31m|[m [31mcf01f6e	[32m[0m added extends, return and println to scanner. [33m(Justin Robb)[0m
* [1;31m|[m [31m696d369	[32m[0m added reserved term for .length [33m(Adam Croissant)[0m
* [1;31m|[m [31maf947a8	[32m[0m added reserved words static, void, String [33m(Adam Croissant)[0m
* [1;31m|[m [31m2f82d2e	[32m[0m added if, else, while to scanner. [33m(Justin Robb)[0m
* [1;31m|[m [31m328c4fb	[32m[0m added stuff causing conflict back into jflex file [33m(Adam Croissant)[0m
* [1;31m|[m   [31mc9de643	[32m[0m Merge branch 'master' of https://bitbucket.org/reptarsrage/401_project [33m(Adam Croissant)[0m
[1;32m|[m[1;33m\[m [1;31m\[m  
[1;32m|[m * [1;31m|[m [31m2ed5cba	[32m[0m added {}, []  and ! to scanner [33m(Justin Robb)[0m
* [1;33m|[m [1;31m|[m [31me2b4014	[32m[0m forgot to add new reserved words to other parts of files [33m(Adam Croissant)[0m
[1;33m|[m[1;33m/[m [1;31m/[m  
* [1;31m|[m   [31md0130d6	[32m[0m Merge branch 'master' of https://bitbucket.org/reptarsrage/401_project [33m(Adam Croissant)[0m
[1;34m|[m[1;35m\[m [1;31m\[m  
[1;34m|[m * [1;31m|[m [31m4205e71	[32m[0m added comment to scanner [33m(Justin Robb)[0m
* [1;35m|[m [1;31m|[m [31ma42c1c7	[32m[0m added reserved words public, class, this, new, true, false, int, double, boolean [33m(Adam Croissant)[0m
[1;35m|[m[1;35m/[m [1;31m/[m  
* [1;31m|[m   [31m5c89f07	[32m[0m Merge branch 'master' of https://bitbucket.org/reptarsrage/401_project [33m(Adam Croissant)[0m
[1;36m|[m[31m\[m [1;31m\[m  
[1;36m|[m * [1;31m|[m [31m7d6537f	[32m[0m added DOUBLE to scanner [33m(Justin Robb)[0m
* [31m|[m [1;31m|[m [31m8bb66a1	[32m[0m added all operators to jflex and cup files [33m(Adam Croissant)[0m
[31m|[m[31m/[m [1;31m/[m  
* [1;31m|[m [31md742cf3	[32m[0m finished adding tests 0-21 [33m(Justin Robb)[0m
* [1;31m|[m [31m54efef0	[32m[0m wrote test for overloading [33m(Adam Croissant)[0m
* [1;31m|[m [31m7a62e11	[32m[0m wrote test for mismatched types in expressions [33m(Adam Croissant)[0m
* [1;31m|[m   [31m2a988fd	[32m[0m  Merge branch 'master' of https://bitbucket.org/reptarsrage/401_project [33m(Adam Croissant)[0m
[32m|[m[33m\[m [1;31m\[m  
[32m|[m * [1;31m|[m [31m9fc66ff	[32m[0m added tests 10-13 [33m(Justin Robb)[0m
* [33m|[m [1;31m|[m   [31m8e1f646	[32m[0m Merge branch 'master' of https://bitbucket.org/reptarsrage/401_project [33m(Adam Croissant)[0m
[34m|[m[33m\[m [33m\[m [1;31m\[m  
[34m|[m [33m|[m[33m/[m [1;31m/[m  
[34m|[m * [1;31m|[m [31mee62546	[32m[0m Added test 8-9 [33m(Justin Robb)[0m
* [35m|[m [1;31m|[m [31m677d29b	[32m[0m fixed some errors in test programs [33m(Adam Croissant)[0m
[35m|[m[35m/[m [1;31m/[m  
* [1;31m|[m [31md44a760	[32m[0m wrote test for int variable assignment and manipulation [33m(Adam Croissant)[0m
* [1;31m|[m [31md89d7a4	[32m[0m wrote test for function dispatch [33m(Adam Croissant)[0m
* [1;31m|[m [31m87638ee	[32m[0m wrote test for basic inheritance [33m(Adam Croissant)[0m
* [1;31m|[m [31mf285e71	[32m[0m wrote test for basic object functionality [33m(Adam Croissant)[0m
* [1;31m|[m [31mb0f35ba	[32m[0m wrote test for while loops on int literals [33m(Adam Croissant)[0m
* [1;31m|[m [31m096dd4f	[32m[0m Wrote test for int literal comparison and branching (if/else) [33m(Adam Croissant)[0m
* [1;31m|[m [31m8540fc7	[32m[0m Wrote test for int literal arithmetic [33m(Adam Croissant)[0m
* [1;31m|[m [31md71ef15	[32m[0m wrote test for printing int literal [33m(Adam Croissant)[0m
[1;31m|[m[1;31m/[m  
* [31m80f1fff	[32m[0m initial project code [33m(Nat Mote)[0m

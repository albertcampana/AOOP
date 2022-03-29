# Assignment 1: Intset

This assignment will serve as the introductory assignment to the course. As such, this assignment is not a good indication of the difficulty of the next two assignments; these will require significantly more work.

The purpose of this assignment is to use Test-Driven Development to implement a class for representing sets of integers. You will implement the set class in Java after writing your tests. Java already offers some set classes in its libraries, but you are not allowed to use these, otherwise, the actual set implementation becomes very trivial. You are allowed to use either a simple array (i.e. int[] array) or a List as the backing data structure. Using a simple array is more challenging, but also better demonstrates the usefulness of testing. Using a simple array instead of a List will give you 0.5 bonus points (provided the implementation works correctly). Regardless of which data structure you choose here, the requirements stay the same.

For this assignment, you have already been provided with a test file and an incomplete stub class.
Before you start, read through the Testing chapter of the reader. Throughout this course we expect you to use JUnit 5.

## Sets

A quick recap on sets: a set is a collection of elements (in this case, integers) in which each element appears no more than once. A set supports the following operations:

![](https://tadream.team/setoperations.png)

- **Union:** set C is the union of sets A and B if C contains only elements of A or B or both and no other elements.  
Notation: C = A <img src="https://render.githubusercontent.com/render/math?math=\cup"> B
- **Intersection:** set C is the intersection of sets A and B if C contains only elements that are present in both A and B.  
Notation: C = A <img src="https://render.githubusercontent.com/render/math?math=\cap"> B
- **Difference:** set C is the difference of sets A and B if C contains only elements that are present in A, but not in B.  
Notation: C = A <img src="https://render.githubusercontent.com/render/math?math=\backslash"> B  
Note that C = A <img src="https://render.githubusercontent.com/render/math?math=\backslash"> B is different from C = B <img src="https://render.githubusercontent.com/render/math?math=\backslash"> A
- **Symmetric Difference:** set C is the symmetric difference of sets A and B if C contains only elements that are present in either A or B, but not in both.  
Notation: C = A <img src="https://render.githubusercontent.com/render/math?math=\oplus"> B or C = A <img src="https://render.githubusercontent.com/render/math?math=\triangle"> B

# Assignment

To start, open the given `pom.xml` in an IDE of your choice ( \***cough**\* IntelliJ \***cough**\*).
Most modern IDEs allow you to open the project by opening the pom file. This means you won't have to create a new project yourself.

The work for this assignment will be done in two files: `IntSet.java` and `IntSetTest.java`.
The `IntSet.java` is a stub class. It contains most of the methods your class should support. Not all operations you are supposed to implement are provided as a stub though. Do not forget to add, implement and test these yourself!

The `IntSetTest.java` is an empty test class. You will have to add the test methods here.

Use the approach as described in the lectures and reader for using JUnit to test each of the given operations carefully and completely. Take care to also test the constructor itself. Always write the stubs first, then the test(s), and *then* implement the corresponding code in such a way that it passes all tests.

The set also has a capacity, i.e. a maximum number of elements it can contain. This capacity is assigned to a set when the set is created and remains fixed. You should not be able to add new items when the set has reached its capacity. There are two ways to deal with this:

- Throw an exception
- Change the method signature to a boolean (this is the only method signature you are allowed to change)

It is up to you which of these two you choose. We will ask about this during the demo, so be prepared to argue for your decision.

The capacity of the resulting set C for any of the above operations should be as follows:

- **Union:** Capacity of C = A <img src="https://render.githubusercontent.com/render/math?math=\cup"> B is equal to the sum of the capacities of A and B.  
- **Intersection:** Capacity of C = A <img src="https://render.githubusercontent.com/render/math?math=\cap"> B is equal to the minimum of the capacities of A and B.
- **Difference:** Capacity of C = A <img src="https://render.githubusercontent.com/render/math?math=\backslash"> B is equal to the capacity of A.
- **Symmetric Difference:** Capacity of C = A <img src="https://render.githubusercontent.com/render/math?math=\oplus"> B is equal to the sum of the capacities of A and B.  

## Operations

Below you will find a list of all the operations your class has to support. We expect every single one of these operations to be tested!
When writing the tests, think about all the possible cases that your code could encounter. At the same time, take into consideration all the corner cases (null, empty sets, illegal examples, etc.). **Do not just test what a method should do, also test what a method shouldn't do!**

The operations are as follows:

- Creating a new empty set (constructor). Make sure to check that the creation is successful and that indeed the new set is empty.
- Checking if a set is empty (`isEmpty`).
- Checking if a set contains a value (`has`).
- Adding elements to the set (`add`). Make sure to test adding the same element twice.
- Removing elements from the set (`remove`). Make sure to test removing non-existing items.
- Union (`union`).
- Intersection (`intersect`).
- Difference (`difference`).
- Symmetric difference (`symmetricDifference`)
- Retrieving an array representation of the set (`getArray`). Also test that this method does not influence the content of the set itself.
- Retrieving the number of elements in the set (`getCount`).
- Retrieving the capacity of the set (`getCapacity`).
- Retrieving a string representation of the set (`toString`). Again, test that this method does not influence the content of the set itself. Make sure to test all possible types of sets with this method.

Please note that you have to think of explicit test cases yourself and that not all of these are listed here. We will, among other things, be looking at how many and which cases you managed to test, so try to be thorough. At the same time, pay attention that you are not testing the same thing multiple times. It goes without saying that all test cases should pass in the end.
Note that in the given code, not all operations have a stub yet and for some of them you will have to implement the entire method.

## General remarks

Documentation is important for all the assignments. Therefore, please make sure you properly document your code with meaningful comments and explanations, while at the same not being too overzealous. In most cases, this means that Javadoc is sufficient. Note that you might have to update the existing Javadoc. While test classes are technically not part of the actual code, they should still be somewhat readable and maintainable.

The `@pre` and `@post` tags are given to you to help you. They are not an official Javadoc construct and we do not expect you to add them yourselves for everything (although it might make things easier for you).

# Handing in + Grading

There is a contributions.md file in your repository. Here you should write a short summary of how you divided the work. This is very important, so don't forget it. Once you are done, create a pull request from the `intset` branch into the `main` branch.

The point distribution for your grade will look as follows:

| Category     				| Max points    |
| ------------------------- |:-------------:| 
| Free point				| 1				|
| Testing 					| 6             |
| JUnit conventions        	| 1             |
| Documentation 			| 1             |
| Clean code/code quality 	| 1   			| 
| Bonus         			| 0.5           |
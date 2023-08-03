package lectures.part0

/** In computer programming terms, an algorithm is a set of well-defined instructions to solve a particular problem. It
  * takes a set of input(s) and produces the desired output.
  *
  * How does a good algorithm looks like:
  *
  *   - Input and output should be defined precisely.
  *   - Each step in the algorithm should be clear and unambiguous.
  *   - Algorithms should be most effective among many different ways to solve a problem.
  *   - An algorithm shouldn't include computer code. Instead, the algorithm should be written in such a way that it can
  *     be used in different programming languages.
  *
  * # Examples:
  *
  *   - Algorithm to add two numbers: [x] Step 1: Start [x] Step 2: Declare variables num1, num2 and sum. [x] Step 3:
  *     Read values num1 and num2. [x] Step 4: Add num1 and num2 and assign the result to sum. sum←num1+num2 [x] Step 5:
  *     Display sum [x] Step 6: Stop
  *
  *   - Find the largest number among three numbers: [x] Step 1: Start [x] Step 2: Declare variables a,b and c. [x] Step
  *     3: Read variables a,b and c. [x] Step 4: Step 4: If a > b If a > c Display a is the largest number. Else Display
  *     c is the largest number. Else If b > c Display b is the largest number. Else Display c is the greatest number.
  *     [x] Step 5: Stop
  *
  * Data structure is a storage that is used to store and organize data. Data structure and data types are slightly
  * different. Data structure is the collection of data types arranged in a specific order.
  *
  * There are two types of `DS`
  *
  *   - Linear data structure
  *   - Non-linear data structure
  *
  * In linear data structures, the elements are arranged in sequence one after the other. Since elements are arranged in
  * particular order, they are easy to implement.
  *
  * Popular linear data structures are:
  *
  *   - Array
  *   - Stack
  *   - Queue
  *   - Linked List
  *
  * Elements in non-linear data structures are not in any sequence. Instead they are arranged in a hierarchical manner
  * where one element will be connected to one or more elements.
  *
  * Non-linear data structures are further divided into graph and tree based data structures.
  *
  * Popular non-linear data structures are:
  *
  *   - Graph:
  *     - Spanning Tree and Minimum Spanning Tree
  *     - Strongly Connected Components
  *     - Adjacency Matrix
  *     - Adjacency List
  *   - Trees
  *     - Binary Tree
  *     - Binary Search Tree
  *     - AVL Tree
  *     - B-Tree
  *     - B+ Tree
  *     - Red-Black Tree
  *
  * | Linear Data Structures                                                | Non Linear Data Structures                                                             |
  * |:----------------------------------------------------------------------|:---------------------------------------------------------------------------------------|
  * | The data items are arranged in sequential order, one after the other. | The data items are arranged in non-sequential order (hierarchical manner).             |
  * | The data items are present at different layers.                       | The data items are present at different layers.                                        |
  * | It can be traversed on a single run.                                  | It requires multiple runs.                                                             |
  * | The memory utilization is not efficient.                              | Different structures utilize memory in different efficient ways depending on the need. |
  * | The time complexity increase with the data size.                      | Time complexity remains the same.                                                      |
  * | Example: Arrays, Stack, Queue                                         | Example: Tree, Graph, Map                                                              |
  *
  * # Asymptotic Analysis
  *
  * The efficiency of an algorithm depends on the amount of time, storage and other resources required to execute the
  * algorithm. The efficiency is measured with the help of asymptotic notations.
  *
  * The study of change in performance of the algorithm with the change in the order of the input size is defined as
  * asymptotic analysis.
  *
  * There are mainly three asymptotic notations:
  *
  *   - Big-O notation: Big-O notation represents the upper bound of the running time of an algorithm. Thus, it gives
  *     the worst-case complexity of an algorithm.
  *   - Omega notation: Omega notation represents the lower bound of the running time of an algorithm. Thus, it provides
  *     the best case complexity of an algorithm.
  *   - Theta notation: Theta notation encloses the function from above and below. Since it represents the upper and the
  *     lower bound of the running time of an algorithm, it is used for analyzing the average-case complexity of an
  *     algorithm.
  *
  * # Master Theorem
  *
  * T(n) = aT(n/b) + f(n) where a >= 1, b > 1 and f(n) = Omega(n**k log**p(n))
  *
  *   - Case 1: if log**a(b) > k then Omega(n**log**a(b))
  *   - Case 2: if log**a(b) = k:
  *     - if p > -1 Omega(n**k log**p+1(n))
  *     - if p = -1 Omega(n**k log(log(n)))
  *     - if p < -1 Omega(n**k)
  *   - Case 3: if log**a(b) < k:
  *     - if p >= 0 Omega(n**k log**p(n))
  *     - if p < 0 Omega(n**k)
  *
  * # Divide and Conquer Algorithm
  *
  * A divide and conquer algorithm is a strategy of solving a large problem by:
  *
  *   - breaking the problem into smaller sub-problems
  *   - solving the sub-problems, and
  *   - combining them to get the desired output.
  *
  * To use the divide and conquer algorithm, recursion is used.
  *
  * Here are the steps that take place:
  *
  *   - Divide: Divide the given problem into sub-problems using recursion.
  *   - Conquer: Solve the smaller sub-problems recursively. If the subproblem is small enough, then solve it directly.
  *   - Combine: Combine the solutions of the sub-problems that are part of the recursive process to solve the actual
  *     problem.
  *
  * One of the applications of Divide and Conquer is to sort an array (i.e. Merge Sort)
  *
  * Let an array be: [ 7 | 6 | 1 | 5 | 4 | 3 ]
  *
  *   1. Divide the array into two halves:
  *
  * [ 7 | 6 | 1 | 5 | 4 | 3 ]
  * |\
  * | \
  * | \________
  * | \
  * | \ [7 | 6 | 1] [5 | 4 | 3]
  *
  * 2. Again, divide each subpart recursively into two halves until you get individual elements, i.e: [7 | 6 | 1]
  * |\____
  * | \
  * | \ [7 | 6] [1]
  * |\_
  * | \ [7] [6]
  *
  * 3. Now, combine the individual elements in a sorted manner. Here, conquer and combine steps go side by side.
  *
  * [1 | 3 | 4 | 5 | 6 | 7]
  *
  * # Time Complexity
  *
  * To calculate the complexity of a D&C algorithm we use the Master Theorem, for a Merge Sort it'd be something like
  *
  * T(n) = aT(n/b) + f(n)
  * = 2T(n/2) + O(n) Where, a = 2 (each time, a problem is divided into 2 subproblems) n/b = n/2 (size of each sub
  * problem is half of the input) f(n) = time taken to divide the problem and merging the subproblems T(n/2) = O(n log
  * n)
  *
  * Now, T(n) = 2T(n log n) + O(n) ≈ O(n log n)
  *
  * # Dynamic Programming
  *
  * The divide and conquer approach divides a problem into smaller subproblems; these subproblems are further solved
  * recursively. The result of each subproblem is not stored for future reference, whereas, in a dynamic approach, the
  * result of each subproblem is stored for future reference.
  *
  * Use the divide and conquer approach when the same subproblem is not solved multiple times. Use the dynamic approach
  * when the result of a subproblem is to be used multiple times in the future.
  */

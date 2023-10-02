# CSC-172 Project 2 - *Huffman Coding*

Submitted by: **Jassem Toumi && Hesham Elshafey **
Emails:
Author 1: **jtoumi@u.rochester.edu**
Author 2: **Helshafe@u.rochester.edu**

Description: **This code provides a Java implementation of the Huffman coding algorithm for compressing and decompressing files. The Huffman algorithm uses variable-length code words to represent characters in a file based on their frequency of occurrence. Huffman coding assigns shorter codes to more frequently used characters and longer codes to less frequently used characters.**

## Required Features

The following **required** functionalities/Methods are completed:

+------- Encoding (Developed by Jassem Toumi):
 1. Read the file and store the frequency of each character
 2. Build the huffman tree (using the frequency map) : implement a priority queue & min heap
 3. Traverse huffman tree and build a code map : pairs (character , huffman-code)
 4. Read the file again and write the encoded file ; replace each character with its huffman code
 5. Write the frequency file: pairs (8-bit representaion of each char : frequency)

+------- Decoding (Developed by Hesham Elshafey)
1. Read the frequency file and create a map that stores pairs (character (from code) : frequnecy)
2. Build a huffman tree from the map your created in step 1
3. Read the encoded file (input file) and for each bit we traverse the tree(if false
   we move left & vice versa) until the current node has no children then we write the corresponding character
4. Keep repeating step 3 until we decode the entire encoded file
 



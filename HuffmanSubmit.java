/*
 - Steps for Encoding:
 1. Read the file and store the frequency of each character
 2. Build the huffman tree (using the frequency map) : implement a priority queue & min heap
 3. Traverse huffman tree and build a code map : pairs (character , huffman-code)
 4. Read the file again and write the encoded file ; replace each character with its huffman code
 5. Write the frequency file: pairs (8-bit representaion of each char : frequency)

- Steps of Decoding
1. Read the frequency file and create a map that stores pairs (character (from code) : frequnecy)
2. Build a huffman tree from the map your created in step 1
3. Read the encoded file (input file) and for each bit we traverse the tree(if false
   we move left & vice versa) until the current node has no children then we write the corresponding character
4. Keep repeating step 3 until we decode the entire encoded file
 */

 

// Import any package as required
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanSubmit implements Huffman {

	private class Node implements Comparable<Node> {
		char chaR;
		int freq;
		Node left;
		Node right;

		Node(char c, int freq, Node left, Node right) {
			this.chaR = c;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		boolean hasChildren() {
			return this.left != null || this.right != null;
		}

		@Override
		// compare the nodes based on their frequency
		public int compareTo(Node n) {
			return this.freq - n.freq;
		}
	}

	public void encode(String inputFile, String outputFile, String freqFile) {
		// read the file
		BinaryIn inputBinary = new BinaryIn(inputFile);
		Map<Character, Integer> freqMap = new HashMap<>();
		// read the file and store the frequency of each character
		while (!inputBinary.isEmpty()) {
			char currentChar = inputBinary.readChar();
			freqMap.put(currentChar, freqMap.getOrDefault(currentChar, 0) + 1);
		}
		// System.out.println("frequency map: " + freqMap);
		try {
			createFreqFile(freqMap, freqFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Node root = buildHuftree(freqMap);
		Map<Character, String> HuffcodeMap = new HashMap<>();
		traverseHuffTree(root, HuffcodeMap, "");
		// System.out.println("Huffman code map: " + HuffcodeMap);

		// encoding
		BinaryIn input = new BinaryIn(inputFile);
		BinaryOut codeWritter = new BinaryOut(outputFile);
		while (!input.isEmpty()) {
			// System.out.println("I am here");
			char currentChar = input.readChar();
			String huffCode = HuffcodeMap.get(currentChar);
			for (int i = 0; i < huffCode.length(); i++) {
				char CodeBit = huffCode.charAt(i);
				// System.out.println("code Bits: "+CodeBit);
				if (CodeBit == '0') {
					codeWritter.write(false);
				}
				if (CodeBit == '1') {
					codeWritter.write(true);
				}
			}
		}
		codeWritter.flush();
		codeWritter.close();
	}

	private void createFreqFile(Map<Character, Integer> freqMap, String outputFreqFile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFreqFile));
		Boolean firstLine = true;
		for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
			char currentChar = entry.getKey();
			int freq = entry.getValue();
			// change the char to 8-bit binary representation
			String binaryString = String.format("%8s", Integer.toBinaryString(currentChar)).replace(' ', '0');
			if (firstLine) {
				bw.write(binaryString + " : " + freq);
				firstLine = false;
			} else {
				bw.write("\n" + binaryString + " : " + freq);
			}
		}
		bw.close();
	}

	private void traverseHuffTree(Node CurrNode, Map<Character, String> map, String str) {
		if (CurrNode.hasChildren()) {
			traverseHuffTree(CurrNode.left, map, str + "0");
			traverseHuffTree(CurrNode.right, map, str + "1");
		} else {
			map.put(CurrNode.chaR, str);
		}
	}

	private Node buildHuftree(Map<Character, Integer> freqMap) {
		PriorityQueue<Node> pq = new PriorityQueue<>();

		// Create a leaf node for each character and add it to the priority queue.
		for (Map.Entry<Character, Integer> obj : freqMap.entrySet()) {
			pq.add(new Node(obj.getKey(), obj.getValue(), null, null));
		}

		// Merge the two nodes of the smallest frequency until there is only one node
		// left.
		while (pq.size() > 1) {
			Node left = pq.poll();
			Node right = pq.poll();
			Node parent = new Node('\0', left.freq + right.freq, left, right);
			pq.add(parent);
		}

		// The remaining node is the root node and the tree is complete.
		return pq.poll();
	}


	public void decode(String inputFile, String outputFile, String freqFile)  {
		Map <Character, Integer> decodinMap = new HashMap <Character, Integer>();
			BufferedReader buffReader = null;
			try {
				buffReader = new BufferedReader(new FileReader(freqFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String line = null;
			try {
				line = buffReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			while(line != null){
				String [] input = line.split(":");
				int intValue = Integer.parseInt(input[0].trim(), 2);
				char chaR = (char) intValue;
				int freq = Integer.parseInt(input[1].trim());
				decodinMap.put(chaR, freq);
				try {
					line = buffReader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Node root = buildHuftree(decodinMap);
			Node currNode = root;
			BinaryIn inputReader = new BinaryIn(inputFile);
			BinaryOut outputWriter = new BinaryOut(outputFile);
			while(!inputReader.isEmpty()){
				boolean currChar = inputReader.readBoolean();
				if(!currNode.hasChildren()){
					outputWriter.write(currNode.chaR);
					outputWriter.flush();
					currNode = root;
				}
				if(currChar){
					currNode = currNode.right;
				}else{
					currNode = currNode.left;
				}
			}
			outputWriter.flush();
			outputWriter.close();
		// System.out.println("decoding map: "+ decodinMap);
	}

	public static void main(String[] args) throws IOException {
		Huffman huffman = new HuffmanSubmit();
		// huffman.encode("ur.jpg", "ur.enc", "freq.txt");
		// huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		huffman.encode("alice30.txt", "enc_alice30.enc", "freq.txt");
		huffman.decode("enc_alice30.enc", "dec_alice30.dec", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same.
		// On linux and mac, you can use `diff' command to check if they are the same.
	}

}

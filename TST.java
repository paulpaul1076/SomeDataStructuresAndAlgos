import java.util.ArrayDeque;

public class TST<Value> {

	private class Node {
		public Node(char ch) {
			this.ch = ch;
		}

		public char ch;
		public int size;
		public Value val;
		public Node left, mid, right;
	}

	private Node root = new Node('$');

	public TST() {

	}

	public void put(String key, Value val) {
		key = "$" + key;
		Node x = getNode(key);
		if (x != null && x.val != null) {
			x.val = val;
			return;
		}
		x = root;
		for (int i = 0; i < key.length(); ++i) {
			char ch = key.charAt(i);
			++x.size;
			if (ch < x.ch) {
				if (x.left == null)
					x.left = new Node(ch);
				x = x.left;
				--i;
			} else if (ch > x.ch) {
				if (x.right == null)
					x.right = new Node(ch);
				x = x.right;
				--i;
			} else if (i == key.length() - 1) {
				x.val = val;
			} else {
				if (x.mid == null)
					x.mid = new Node(key.charAt(i + 1));
				x = x.mid;
			}
		}
	}

	public void putRecursive(String key, Value val) {
		key = "$" + key;
		Node x = getNode(key);
		if (x != null && x.val != null) {
			x.val = val;
			return;
		}
		root = putRecursiveHelper(root, key, val, 0);
	}

	public Value get(String key) {
		Node x = getNode("$" + key);
		if (x != null)
			return x.val;
		return null;
	}

	public Value getRecursive(String key) {
		Node x = getRecursiveHelper(root, "$" + key, 0);
		if (x != null)
			return x.val;
		return null;
	}

	public void delete(String key) { // no $
		if (!contains(key))
			return;
		key = "$" + key;
		Node x = root;
		for (int i = 0; i < key.length(); ++i) {
			char ch = key.charAt(i);
			--x.size;
			if (ch < x.ch) {
				if (x.left.size == 1) {
					x.left = null;
					return;
				}
				x = x.left;
				--i;
			} else if (ch > x.ch) {
				if (x.right.size == 1) {
					x.right = null;
					return;
				}
				x = x.right;
				--i;
			} else {
				if (x.mid.size == 1) {
					x.mid = null;
					return;
				}
				x = x.mid;
			}
		}
	}

	public void deleteRecursive(String key) {
		if (!contains(key))
			return;
		root = deleteRecursiveHelper(root, "$" + key, 0);
		if (root == null)
			root = new Node('$');
	}

	public String longestPrefixOf(String s) {
		int pos = goDown(s);
		if (pos == -1)
			return null;
		return s.substring(0, pos);
	}

	public String longestPrefixOfRecursive(String s) {
		int pos = goDownRecursive(root, -1, "$" + s, 0);
		if (pos == -1)
			return null;
		return s.substring(0, pos);
	}

	private class Pair {
		public Pair(Node node, String str) {
			this.node = node;
			this.str = str;
		}

		public Node node;
		public String str;
	}

	public Iterable<String> keysThatMatch(String pattern) {
		ArrayDeque<Pair> argQ = new ArrayDeque<>();
		argQ.add(new Pair(root.mid, ""));
		for (int i = 0; i < pattern.length(); ++i) {
			ArrayDeque<Pair> tempQ = new ArrayDeque<>();
			char ch = pattern.charAt(i);
			while (!argQ.isEmpty()) {
				Pair p = argQ.pop();
				if (ch == '.') {
					ArrayDeque<Node> chars = new ArrayDeque<>();
					ArrayDeque<Node> dfsStack = new ArrayDeque<>();
					Node crawler = p.node;
					while (crawler != null) {
						dfsStack.push(crawler);
						crawler = crawler.left;
					}
					while (!dfsStack.isEmpty()) {
						Node top = dfsStack.pop();
						chars.add(top);
						crawler = top.right;
						while (crawler != null) {
							dfsStack.push(crawler);
							crawler = crawler.left;
						}
					}
					for (Node node : chars)
						if (i < pattern.length() - 1) {
							if (node.mid != null)
								tempQ.add(new Pair(node.mid, p.str + node.ch));
						} else
							tempQ.add(new Pair(node, p.str + node.ch));
				} else {
					if (ch < p.node.ch) {
						if (p.node.left != null)
							argQ.push(new Pair(p.node.left, p.str));
					} else if (ch > p.node.ch) {
						if (p.node.right != null)
							argQ.push(new Pair(p.node.right, p.str));
					} else if (i < pattern.length() - 1) {
						if (p.node.mid != null)
							tempQ.add(new Pair(p.node.mid, p.str + p.node.ch));
					} else {
						tempQ.add(new Pair(p.node, p.str + p.node.ch));
					}
				}
			}
			argQ = tempQ;
		}
		ArrayDeque<String> Q = new ArrayDeque<>();
		for (Pair p : argQ) {
			if (p.node.val != null) {
				Q.add(p.str);
			}
		}
		if (Q.isEmpty() && pattern.length() == 0 && root.val != null)
			Q.add("");
		return Q;
	}

	public Iterable<String> keysThatMatchRecursive(String pattern) {
		ArrayDeque<String> Q = new ArrayDeque<>();
		collectRecursive(root.mid, pattern, "", Q, 0);
		if (Q.isEmpty() && pattern.length() == 0 && root.val != null)
			Q.add("");
		return Q;
	}

	public Iterable<String> keysWithPrefix(String prefix) {
		ArrayDeque<String> Q = new ArrayDeque<>();
		Node x = getNode("$" + prefix);
		if (x == null)
			return Q;
		if (x.val != null)
			Q.add(prefix);

		ArrayDeque<Pair> dfsStack = new ArrayDeque<>();

		Node crawler = x.mid;
		while (crawler != null) {
			dfsStack.push(new Pair(crawler, prefix));
			crawler = crawler.left;
		}

		while (!dfsStack.isEmpty()) {
			Pair p = dfsStack.pop();
			if (p.node.val != null) {
				Q.add(p.str + p.node.ch);
			}
			crawler = p.node.right;
			while (crawler != null) {
				dfsStack.push(new Pair(crawler, p.str));
				crawler = crawler.left;
			}
			crawler = p.node.mid;
			while (crawler != null) {
				dfsStack.push(new Pair(crawler, p.str + p.node.ch));
				crawler = crawler.left;
			}
		}
		return Q;
	}

	public Iterable<String> keysWithPrefixRecursive(String prefix) {
		ArrayDeque<String> Q = new ArrayDeque<>();
		Node x = getNode("$" + prefix);
		if (x == null)
			return Q;
		if (x.val != null)
			Q.add(prefix);
		collectRecursive(x.mid, prefix, Q);
		return Q;
	}

	public Iterable<String> keys() {
		return keysWithPrefix("");
	}

	public Iterable<String> keysRecursive() {
		return keysWithPrefixRecursive("");
	}

	public String floor(String s) {
		int leftNodeDepth = -1;
		int prevKeyNodeDepth = -1;
		Node leftNode = null;
		Node prevKeyNode = null;
		boolean upperLeft = false;
		String key = s;
		s = "$" + s;
		Node x = root;
		for (int i = 0; i < s.length(); ++i) {
			char ch = s.charAt(i);
			if (ch < x.ch) {
				if (x.left == null) {
					x = null;
					break;
				}
				x = x.left;
				--i;
			} else if (ch > x.ch) {
				leftNode = x;
				leftNodeDepth = i;
				upperLeft = true;
				if (x.right == null) {
					x = null;
					break;
				}
				x = x.right;
				--i;
			} else {
				if (x.left != null) {
					leftNode = x.left;
					leftNodeDepth = i;
					upperLeft = false;
				}
				if (i == s.length() - 1) {
					break;
				}
				if (x.mid == null) {
					x = null;
					break;
				}
				if (x.val != null) {
					prevKeyNode = x;
					prevKeyNodeDepth = i + 1;
				}
				x = x.mid;
			}
		}
		if (x == null || x.val == null) {
			if (prevKeyNode == null && leftNode == null) {
				// System.out.println("1");
				return null;
			}
			if (prevKeyNodeDepth - 1 >= leftNodeDepth) {
				// System.out.println("2");
				return s.substring(1, prevKeyNodeDepth);
			} else {
				if (upperLeft) {
					// System.out.println("3");
					return s.substring(1, leftNodeDepth) + leftNode.ch + getMax(leftNode.mid);
				}
				// System.out.println("4");
				return s.substring(1, leftNodeDepth) + getMax(leftNode);
			}
		}
		// System.out.println("5");
		return key;
	}

	private String getMax(Node crawler) {
		StringBuilder result = new StringBuilder();
		while (crawler != null) {
			while (crawler.right != null) {
				crawler = crawler.right;
			}
			result.append(crawler.ch);
			crawler = crawler.mid;
		}
		return result.toString();
	}

	public String floorRecursive(String s) {
		String key = s;
		s = "$" + s;
		TupleFloor tuple = goDown(root, s, 0, null, -1, null, -1, false);
		Node x = tuple.x;
		Node leftNode = tuple.leftNode;
		int leftNodeDepth = tuple.leftNodeDepth;
		Node prevKeyNode = tuple.prevKeyNode;
		int prevKeyNodeDepth = tuple.prevKeyNodeDepth;
		boolean upperLeft = tuple.upperLeft;
		if (x == null || x.val == null) {
			if (prevKeyNode == null && leftNode == null) {
				System.out.println("1");
				return null;
			}
			if (prevKeyNodeDepth - 1 >= leftNodeDepth) {
				System.out.println("2");
				return s.substring(1, prevKeyNodeDepth);
			} else {
				if (upperLeft) {
					System.out.println("3");
					return s.substring(1, leftNodeDepth) + leftNode.ch + getMax(leftNode.mid);
				}
				System.out.println("4");
				return s.substring(1, leftNodeDepth) + getMax(leftNode);
			}
		}
		System.out.println("5");
		return key;
	}

	private class TupleFloor {
		public TupleFloor(Node x, Node leftNode, int leftNodeDepth, Node prevKeyNode, int prevKeyNodeDepth,
				boolean upperLeft) {
			this.x = x;
			this.leftNode = leftNode;
			this.leftNodeDepth = leftNodeDepth;
			this.prevKeyNode = prevKeyNode;
			this.prevKeyNodeDepth = prevKeyNodeDepth;
			this.upperLeft = upperLeft;
		}

		public boolean upperLeft;
		public Node x;
		public Node leftNode;
		public int leftNodeDepth;
		public Node prevKeyNode;
		public int prevKeyNodeDepth;
	}

	private TupleFloor goDown(Node x, String s, int d, Node leftNode, int leftNodeDepth, Node prevKeyNode,
			int prevKeyNodeDepth, boolean upperLeft) {
		if (x == null)
			return new TupleFloor(null, leftNode, leftNodeDepth, prevKeyNode, prevKeyNodeDepth, upperLeft);
		char ch = s.charAt(d);
		if (ch > x.ch) {
			leftNode = x;
			leftNodeDepth = d;
			upperLeft = true;
			return goDown(x.right, s, d, leftNode, leftNodeDepth, prevKeyNode, prevKeyNodeDepth, true);
		} else if (ch < x.ch) {
			return goDown(x.left, s, d, leftNode, leftNodeDepth, prevKeyNode, prevKeyNodeDepth, upperLeft);
		}
		if (x.left != null) {
			leftNode = x.left;
			leftNodeDepth = d;
			upperLeft = false;
		}
		if (d == s.length() - 1) {
			return new TupleFloor(x, leftNode, leftNodeDepth, prevKeyNode, prevKeyNodeDepth, upperLeft);
		}
		if (x.val != null) {
			prevKeyNode = x;
			prevKeyNodeDepth = d + 1;
		}
		return goDown(x.mid, s, d + 1, leftNode, leftNodeDepth, prevKeyNode, prevKeyNodeDepth, upperLeft);
	}

	public String ceil(String s) {
		Node rightNode = null;
		int rightNodeDepth = -1;
		String key = s;
		s = "$" + s;
		Node x = root;
		boolean upperRight = false;
		for (int i = 0; i < s.length(); ++i) {
			char ch = s.charAt(i);
			if (ch > x.ch) {
				if (x.right == null) {
					x = null;
					break;
				}
				x = x.right;
				--i;
			} else if (ch < x.ch) {
				rightNode = x;
				rightNodeDepth = i;
				upperRight = true;
				if (x.left == null) {
					x = null;
					break;
				}
				x = x.left;
				--i;
			} else {
				if (x.right != null) {
					rightNodeDepth = i;
					rightNode = x;
					upperRight = false;
				}
				if (i == s.length() - 1) {
					break;
				}
				if (x.mid == null) {
					x = null;
					break;
				}
				x = x.mid;
			}
		}
		// don't forget about x.mid
		if (x == null || x.val == null) {
			if (x != null && x.mid != null) {
				return s.substring(1) + getMin(x.mid);
			} else if (rightNode != null) {
				if (upperRight) {
					return s.substring(1, rightNodeDepth) + rightNode.ch + getMin(rightNode.mid);
				}
				return s.substring(1, rightNodeDepth) + getMin(rightNode.right);
			} else {
				return null;
			}
		}
		return key;
	}

	private String getMin(Node crawler) {
		StringBuilder result = new StringBuilder();
		while (crawler != null) {
			while (crawler.left != null) {
				crawler = crawler.left;
			}
			result.append(crawler.ch);
			crawler = crawler.mid;
		}
		return result.toString();
	}

	public String ceilRecursive(String s) {
		// same points as described in the floorRecursive function
		String key = s;
		s = "$" + s;
		TupleCeil tuple = goDown(root, s, 0, null, -1, false);
		Node x = tuple.x;
		Node rightNode = tuple.rightNode;
		int rightNodeDepth = tuple.rightNodeDepth;
		boolean upperRight = tuple.upperRight;
		if (x == null || x.val == null) {
			if (x != null && x.mid != null) {
				return s.substring(1) + getMin(x.mid);
			} else if (rightNode != null) {
				if (upperRight) {
					return s.substring(1, rightNodeDepth) + rightNode.ch + getMin(rightNode.mid);
				}
				return s.substring(1, rightNodeDepth) + getMin(rightNode.right);
			} else {
				return null;
			}
		}
		return key;
	}

	private class TupleCeil {
		public TupleCeil(Node x, Node rightNode, int rightNodeDepth, boolean upperRight) {
			this.x = x;
			this.rightNode = rightNode;
			this.rightNodeDepth = rightNodeDepth;
			this.upperRight = upperRight;
		}

		public Node x;
		public Node rightNode;
		public int rightNodeDepth;
		public boolean upperRight;
	}

	// this is for ceiling
	private TupleCeil goDown(Node x, String s, int d, Node rightNode, int rightNodeDepth,
			boolean upperRight) {
		if (x == null)
			return new TupleCeil(null, rightNode, rightNodeDepth, upperRight);
		char ch = s.charAt(d);
		if (ch > x.ch) {
			return goDown(x.right, s, d, rightNode, rightNodeDepth, upperRight);
		} else if (ch < x.ch) {
			rightNode = x;
			rightNodeDepth = d;
			upperRight = true;
			return goDown(x.left, s, d, rightNode, rightNodeDepth, upperRight);
		}
		if (d == s.length() - 1)
			return new TupleCeil(x, rightNode, rightNodeDepth, upperRight);
		if (x.right != null) {
			rightNode = x;
			rightNodeDepth = d;
			upperRight = false;
		}
		return goDown(x.mid, s, d + 1, rightNode, rightNodeDepth, upperRight);
	}

	public int size() {
		return root.size;
	}

	public boolean contains(String key) {
		Node x = getNode("$" + key);
		return x != null && x.val != null;
	}

	// private methods
	// for prefix
	private void collectRecursive(Node x, String soFar, ArrayDeque<String> Q) {
		if (x == null)
			return;
		collectRecursive(x.left, soFar, Q);
		if (x.val != null)
			Q.add(soFar + x.ch);
		collectRecursive(x.mid, soFar + x.ch, Q);
		collectRecursive(x.right, soFar, Q);
	}

	// for matches
	private void collectRecursive(Node x, String pattern, String soFar, ArrayDeque<String> Q, int d) {
		if (x == null || d >= pattern.length())
			return;
		if (d == pattern.length() - 1 && x.val != null) {
			Q.add(soFar + x.ch);
		}
		char ch = pattern.charAt(d);
		if (ch == '.') {
			collectRecursive(x.left, pattern, soFar, Q, d);
			collectRecursive(x.mid, pattern, soFar + x.ch, Q, d + 1);
			collectRecursive(x.right, pattern, soFar, Q, d);
		} else {
			if (ch > x.ch) {
				collectRecursive(x.right, pattern, soFar, Q, d);
			} else if (ch < x.ch) {
				collectRecursive(x.left, pattern, soFar, Q, d);
			} else {
				collectRecursive(x.mid, pattern, soFar + x.ch, Q, d + 1);
			}
		}
	}

	private int goDownRecursive(Node x, int soFar, String key, int d) {
		char ch = key.charAt(d);
		if (ch < x.ch) {
			if (x.left == null)
				return soFar;
			return goDownRecursive(x.left, soFar, key, d);
		} else if (ch > x.ch) {
			if (x.right == null)
				return soFar;
			return goDownRecursive(x.right, soFar, key, d);
		}
		if (x.val != null)
			soFar = d;
		if (x.mid == null)
			return soFar;
		else if (d == key.length() - 1)
			return soFar;
		return goDownRecursive(x.mid, soFar, key, d + 1);
	}

	private int goDown(String key) {
		Node x = root;
		key = "$" + key;
		int count = -1;
		for (int i = 0; i < key.length(); ++i) {
			char ch = key.charAt(i);
			if (ch < x.ch) {
				if (x.left == null)
					return count;
				x = x.left;
				--i;
			} else if (ch > x.ch) {
				if (x.right == null)
					return count;
				x = x.right;
				--i;
			} else {
				if (x.val != null)
					count = i;
				if (x.mid == null)
					return count;
				x = x.mid;
			}
		}
		return count;
	}

	private Node getRecursiveHelper(Node x, String key, int d) {
		if (x == null)
			return null;
		char ch = key.charAt(d);
		if (ch < x.ch) {
			return getRecursiveHelper(x.left, key, d);
		} else if (ch > x.ch) {
			return getRecursiveHelper(x.right, key, d);
		} else if (d < key.length() - 1) {
			return getRecursiveHelper(x.mid, key, d + 1);
		}
		return x;
	}

	private Node getNode(String key) {
		Node x = root;
		for (int i = 0; i < key.length(); ++i) {
			char ch = key.charAt(i);
			if (ch < x.ch) {
				if (x.left == null)
					return null;
				x = x.left;
				--i;
			} else if (ch > x.ch) {
				if (x.right == null)
					return null;
				x = x.right;
				--i;
			} else if (i < key.length() - 1) {
				if (x.mid == null)
					return null;
				x = x.mid;
			}
		}
		return x;
	}

	private Node putRecursiveHelper(Node x, String key, Value val, int d) {
		if (x == null)
			x = new Node(key.charAt(d));
		char ch = key.charAt(d);
		if (ch < x.ch) {
			x.left = putRecursiveHelper(x.left, key, val, d);
		} else if (ch > x.ch) {
			x.right = putRecursiveHelper(x.right, key, val, d);
		} else {
			if (d == key.length() - 1) {
				x.val = val;
			} else {
				x.mid = putRecursiveHelper(x.mid, key, val, d + 1);
			}
		}
		++x.size;
		return x;
	}

	private Node deleteRecursiveHelper(Node x, String key, int d) {
		if (x.size == 1)
			return null;
		char ch = key.charAt(d);
		if (ch < x.ch) {
			x.left = deleteRecursiveHelper(x.left, key, d);
		} else if (ch > x.ch) {
			x.right = deleteRecursiveHelper(x.right, key, d);
		} else {
			x.mid = deleteRecursiveHelper(x.mid, key, d + 1);
		}
		--x.size;
		return x;
	}
}

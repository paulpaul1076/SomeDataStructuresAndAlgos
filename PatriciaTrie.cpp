#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <stack>
#include <random>
#include <chrono>

using namespace std;

class PatriciaTrie {
private:
	int m_size = 0;
	string* lastLowerBound = nullptr;
	string* lastUpperBound = nullptr;
	struct Node {
		Node(const string& label) {
			isWord = false;
			this->label = label;
		}
		vector<Node*> next;
		string label;
		bool isWord;
	};
	Node* m_root = new Node("$");
	int matchConsecutive(const string& s1, const string& s2) {
		int N = min(s1.size(), s2.size());
		int count = 0;
		for (size_t i = 0; i < N; i++) {
			if (s1[i] == s2[i]) count++;
			else break;
		}
		return count;
	}
	Node* put(Node* n, const string& key) {
		int matchN = matchConsecutive(n->label, key);
		if (matchN == key.size() && matchN == n->label.size()) {
			if (!n->isWord) {
				m_size++;
				n->isWord = true;
			}
		} else if (matchN == key.size() && n->label.size() > key.size()) {
			string leftPart = n->label.substr(0, matchN);
			string rightPart = n->label.substr(matchN, n->label.size() - matchN);
			Node* newNode = new Node(leftPart); // new Node(key)
			m_size++;
			newNode->isWord = true;
			n->label = rightPart;
			newNode->next.emplace_back(n);
			n = newNode;
		} else if (matchN == n->label.size() && key.size() > n->label.size()) {
			string rightKeyPart = key.substr(matchN, key.size() - matchN);
			bool isPresent = false;
			for (Node*& x : n->next) {
				if (x->label[0] == rightKeyPart[0]) {
					isPresent = true;
					x = put(x, rightKeyPart);
				}
			}
			if (!isPresent) {
				Node* newNode = new Node(rightKeyPart);
				m_size++;
				newNode->isWord = true;
				n->next.emplace_back(newNode);
			}
		} else {
			string commonPrefix = key.substr(0, matchN);
			Node* newNode = new Node(commonPrefix);
			string restOfLabel = n->label.substr(matchN, n->label.size() - matchN);
			string restOfKey = key.substr(matchN, key.size() - matchN);
			n->label = restOfLabel;
			newNode->next.emplace_back(n);
			Node* keyNode = new Node(restOfKey);
			m_size++;
			keyNode->isWord = true;
			newNode->next.emplace_back(keyNode);
			n = newNode;
		}
		return n;
	}
	void collect(Node* n, string soFar, vector<string>& words) {
		for (Node* x : n->next) {
			if (x->isWord) words.emplace_back(soFar + x->label);
			collect(x, soFar + x->label, words);
		}
	}
	Node* remove(Node* n, const string& key) {
		int matchN = matchConsecutive(n->label, key);
		if (matchN == n->label.size() && matchN == key.size()) {
			m_size--;
			if (n->next.size() == 0) {
				delete n;
				n = nullptr;
			} else {
				n->isWord = false;
			}
		} else if (matchN == n->label.size() && n->label.size() < key.size()) {
			string restOfKey = key.substr(matchN, key.size() - matchN);
			bool isPresent = false;
			int i = 0;
			for (; i < n->next.size(); ++i) {
				if (n->next[i]->label[0] == restOfKey[0]) {
					isPresent = true;
					n->next[i] = remove(n->next[i], restOfKey);
					break;
				}
			}
			if (isPresent && n->next[i] == nullptr) {
				n->next.erase(n->next.begin() + i);
			}
		}
		return n;
	}
	bool contains(Node* n, const string& keyPart) {
		if (keyPart.size() < n->label.size()) return false;
		int matchN = matchConsecutive(n->label, keyPart);
		if (matchN == n->label.size() && matchN == keyPart.size()) {
			return n->isWord;
		} else if (matchN >= n->label.size() && keyPart.size() > n->label.size()) {
			string restOfKey = keyPart.substr(matchN, keyPart.size() - matchN);
			bool isPresent = false;
			for (Node* x : n->next) {
				if (x->label[0] == restOfKey[0]) {
					isPresent = true;
					return contains(x, restOfKey);
				}
			}
			if (!isPresent) {
				return false;
			}
		}
		return false;
	}
	string getMax(Node* n) {
		if (n == nullptr) return "";
		string result = n->label;
		Node* crawler = n;
		while (crawler->next.size() > 0) {
			Node* maxLabelNode = nullptr;
			for (Node* x : crawler->next) if (maxLabelNode == nullptr || maxLabelNode->label[0] < x->label[0]) maxLabelNode = x;
			result += maxLabelNode->label;
			crawler = maxLabelNode;
		}
		return result;
	}
	string getMinWrong(Node* n) { // wrong
		if (n == nullptr) return "";
		string result = n->label;
		Node* crawler = n;
		while (crawler->next.size() > 0) {
			Node* minLabelNode = nullptr;
			for (Node* x : crawler->next) if (minLabelNode == nullptr || minLabelNode->label[0] > x->label[0]) minLabelNode = x;
			result += minLabelNode->label;
			crawler = minLabelNode;
		}
		return result;
	}
	string getMin(Node* n) {
		if (n == nullptr) return "";
		string result = n->label;
		Node* crawler = n;
		while (!crawler->isWord) {
			Node* minLabelNode = nullptr;
			for (Node* x : crawler->next) if (minLabelNode == nullptr || minLabelNode->label[0] > x->label[0]) minLabelNode = x;
			crawler = minLabelNode;
			result += crawler->label;
		}
		return result;
	}
	string* lowerBound(Node* n, Node* prevNodeWithASmallerChar, string soFarBeforePrevNode, string prevKey, const string& keyPart, string soFar) {
		string tempPrevKey = prevKey;
		int matchN = matchConsecutive(n->label, keyPart);
		if (n->isWord) prevKey = soFar + n->label;
		if (matchN == keyPart.size() && matchN == n->label.size()) {
			if (n->isWord) return new string(prevKey.substr(1, prevKey.size() - 1));
			else if (prevKey.size() > soFarBeforePrevNode.size()) return new string(prevKey.substr(1, prevKey.size() - 1));
			else if (prevNodeWithASmallerChar != nullptr) return new string(soFarBeforePrevNode.substr(1, soFarBeforePrevNode.size() - 1) + getMax(prevNodeWithASmallerChar));
		} else if (matchN == n->label.size() && keyPart.size() > n->label.size()) {
			string restOfKey = keyPart.substr(matchN, keyPart.size() - matchN);
			Node* lowerBoundNode = nullptr;
			for (Node* x : n->next) if (x->label[0] < restOfKey[0] && (lowerBoundNode == nullptr || lowerBoundNode->label[0] < x->label[0])) lowerBoundNode = x;
			if (lowerBoundNode != nullptr) {
				prevNodeWithASmallerChar = lowerBoundNode;
				soFarBeforePrevNode = soFar + n->label;
			}
			for (Node* x : n->next) if (x->label[0] == restOfKey[0]) return lowerBound(x, prevNodeWithASmallerChar, soFarBeforePrevNode, prevKey, restOfKey, soFar + n->label);
			if (prevKey.size() > soFarBeforePrevNode.size()) return new string(prevKey.substr(1, prevKey.size() - 1)); // might be incorrect
			else if (prevNodeWithASmallerChar != nullptr) return new string(soFarBeforePrevNode.substr(1, soFarBeforePrevNode.size() - 1) + getMax(prevNodeWithASmallerChar));
		} else if (matchN == keyPart.size() && n->label.size() > keyPart.size()) {
			if (tempPrevKey.size() > soFarBeforePrevNode.size()) return new string(tempPrevKey.substr(1, tempPrevKey.size() - 1));
			else if (prevNodeWithASmallerChar != nullptr) return new string(soFarBeforePrevNode.substr(1, soFarBeforePrevNode.size() - 1) + getMax(prevNodeWithASmallerChar));
		} else {
			char fromLabel = n->label[matchN];
			char fromKeyPart = keyPart[matchN];
			if (fromLabel < fromKeyPart) {
				Node* nodeWithMaxLabel = nullptr;
				for (Node* x : n->next) if (nodeWithMaxLabel == nullptr || nodeWithMaxLabel->label[0] < x->label[0]) nodeWithMaxLabel = x;
				string temp = soFar + n->label;
				return new string(temp.substr(1, temp.size() - 1) + getMax(nodeWithMaxLabel));
			} else if (prevNodeWithASmallerChar != nullptr) return new string(soFarBeforePrevNode.substr(1, soFarBeforePrevNode.size() - 1) + getMax(prevNodeWithASmallerChar));
		}
		return nullptr;
	}
	string* upperBound(Node* n, Node* prevNodeWithALargerChar, string soFarBeforePrevNode, const string& keyPart, string soFar) {
		int matchN = matchConsecutive(n->label, keyPart);
		if (matchN == n->label.size() && matchN == keyPart.size()) {
			if (n->isWord) {
				string temp = soFar+n->label;
				return new string(temp.substr(1, temp.size() - 1));
			} else if (n->next.size() > 0) {
				string temp = soFar + getMin(n);
				return new string(temp.substr(1, temp.size() - 1));
			} else if (prevNodeWithALargerChar != nullptr) return new string(soFarBeforePrevNode.substr(1, soFarBeforePrevNode.size() - 1) + getMin(prevNodeWithALargerChar));
		} else if (matchN == n->label.size() && keyPart.size() > n->label.size()) {
			string restOfKey = keyPart.substr(matchN, keyPart.size() - matchN);
			Node* upperBoundNode = nullptr;
			for (Node* x : n->next)	if (x->label[0] > restOfKey[0] && (upperBoundNode == nullptr || upperBoundNode->label[0] > x->label[0])) upperBoundNode = x;
			if (upperBoundNode != nullptr) {
				prevNodeWithALargerChar = upperBoundNode;
				soFarBeforePrevNode = soFar + n->label;
			}
			for (Node* x : n->next) if (x->label[0] == restOfKey[0]) return upperBound(x, prevNodeWithALargerChar, soFarBeforePrevNode, restOfKey, soFar + n->label); // must be larger!
			if (prevNodeWithALargerChar != nullptr) {
				if (n->label == "$") soFarBeforePrevNode = "$";
				return new string(soFarBeforePrevNode.substr(1, soFarBeforePrevNode.size() - 1) + getMin(prevNodeWithALargerChar));
			}
		} else if (matchN == keyPart.size() && n->label.size() > keyPart.size()) return new string(soFar.substr(1, soFar.size() - 1) + getMin(n));
		char fromLabel = n->label[matchN];
		char fromKeyPart = keyPart[matchN];
		if (fromLabel > fromKeyPart) return new string(soFar.substr(1, soFar.size() - 1) + getMin(n));
		else if (prevNodeWithALargerChar != nullptr) return new string(soFarBeforePrevNode.substr(1, soFarBeforePrevNode.size() - 1) + getMin(prevNodeWithALargerChar));
		return nullptr;
	}
public:
	void put(const string& key) {
		m_root = put(m_root, "$" + key);
	}
	void remove(const string& key) {
		m_root = remove(m_root, "$" + key);
		if (m_root == nullptr) m_root = new Node("$");
	}
	string* lowerBound(const string& key) {
		delete lastLowerBound;
		lastLowerBound = lowerBound(m_root, nullptr, "", "", "$" + key, "");
		return lastLowerBound;
	}
	string* upperBound(const string& key) {
		delete lastUpperBound;
		lastUpperBound = upperBound(m_root, nullptr, "", "$" + key, "");
		return lastUpperBound;
	}
	bool contains(const string& key) {
		return contains(m_root, "$" + key);
	}
	vector<string> strings() {
		vector<string> result;
		if (m_root->isWord) result.emplace_back("");
		collect(m_root, "", result);
		return result;
	}
	int size() {
		return m_size;
	}
	~PatriciaTrie() {
		Node* x = m_root;
		vector<Node*> nodes;
		stack<Node*> dfsStack;
		dfsStack.push(m_root);
		while (!dfsStack.empty()) {
			Node* pop = dfsStack.top(); dfsStack.pop();
			nodes.emplace_back(pop);
			for (Node* n : pop->next) {
				dfsStack.push(n);
			}
		}
		for (Node* n : nodes) delete n;
		delete lastLowerBound;
		delete lastUpperBound;
	}
};

random_device rd;
mt19937 gen(rd());
uniform_int_distribution<> dis('a', 'z');
uniform_real_distribution<> disReal(0, 1);

string randomString(int size) {
	string s;
	for (size_t i = 0; i < size; i++) {
		s += ((char)dis(gen));
	}
	return s;
}
vector<string> randomStrings(int count, int sizeBound) {
	vector<string> result(count);
	for (size_t i = 0; i < count; i++) {
		int size = 1 + (int)(disReal(gen) * sizeBound);
		result[i] = randomString(size);
	}
	return result;
}
int main() {
	vector<string> words = { "she","sells","seashells","by","the","seashore","the","shells","she","shells","are","surely","seashells" };
	PatriciaTrie pt;
	for (const string& s : words) {
		pt.put(s);
	}
	vector<string> strings = pt.strings();
	sort(strings.begin(), strings.end());
	cout << "sorted strings : " << endl;
	for (const string& s : strings) cout << s << endl;
	cout << "\n\n\n\n\n";
	string* str_ptr = pt.upperBound("surelyy");
	if (str_ptr != nullptr)
		cout << *str_ptr;
	else cout << "no such element";
}
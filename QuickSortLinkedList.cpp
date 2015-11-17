#include <iostream>
#include <utility>
#include <algorithm>
#include <random>

using namespace std;

struct Node {
	Node() {}
	Node(int x)
	{
		data = x;
	}
	int data;
	Node *next, *prev;
};

class LinkedList {
	typedef pair<Node*, Node*> pnn;
private:
	int size = 0;
	Node *first = nullptr, *last = nullptr;
public:
	LinkedList()
	{
	}

	void add_front(int x) {
		if (0 == size) {
			first = last = new Node(x);
			first->next = nullptr;
			first->prev = nullptr;
		}
		else {
			Node *newNode = new Node(x);
			newNode->next = first;
			newNode->prev = nullptr;
			first->prev = newNode;
			first = newNode;
		}
		size++;
	}
	void add_back(int x) {
		if (0 == size) {
			first = last = new Node(x);
			first->next = nullptr;
			first->prev = nullptr;
		}
		else {
			Node *newNode = new Node(x);
			last->next = newNode;
			newNode->prev = last;
			newNode->next = nullptr;
			last = newNode;
		}
		size++;
	}
	Node *getFirst() {
		return first;
	}
	Node *getLast() {
		return last;
	}
	int getSize() {
		return size;
	}
	void sort() {
		pnn p = sort(first, last);
		first = p.first;
		first->prev = nullptr;
		last = p.second;
		last->next = nullptr;
	}

	void sort2() {
		pnn p = sort2(first, last);
		first = p.first;
		last = p.second;
		first->prev = nullptr;
		last->next = nullptr;
	}

	~LinkedList()
	{
		Node *ptr = first;
		while (ptr != nullptr) {
			Node *del = ptr;
			ptr = ptr->next;
			delete del;
		}
	}

private:

	Node *cut_out(Node *cut) {
		Node *next = cut->next;
		Node *prev = cut->prev;
		next->prev = prev;
		prev->next = next;
		cut->next = nullptr;
		cut->prev = nullptr;
		return cut;
	}

	pnn sort(Node *beg, Node *end) {
		if (beg == end) return{ beg,end };
		Node *dum = new Node();
		dum->next = beg;
		beg->prev = dum;
		dum->prev = end;
		end->next = dum;

		int pivot = beg->data;

		Node *ptr = beg;
		while (ptr != dum) {
			Node *cut = ptr;
			ptr = ptr->next;
			if (pivot > cut->data) {
				cut = cut_out(cut);
				Node *next = dum->next;
				cut->next = next;
				next->prev = cut;
				dum->next = cut;
				cut->prev = dum;
			}
		}

		Node *left = dum->next;
		Node *right = dum->prev;

		left->prev = nullptr;
		right->next = nullptr;

		delete dum;

		pnn l({ beg,beg }), r({ beg,beg });

		if (beg != left) l = sort(left, beg->prev);
		if (beg != right) r = sort(beg->next, right);

		if (l.second != r.first) {
			l.second->next = beg;
			beg->prev = l.second;
			r.first->prev = beg;
			beg->next = r.first;
		}

		return{ l.first,r.second };
	}

	pnn sort2(Node *beg, Node *end) {
		if (beg == end) return{ beg,end };
		Node *leftEq = beg, *rightEq = beg;
		Node *ptr = beg->next;

		Node *dum = new Node();
		end->next = dum;
		dum->prev = end;
		beg->prev = dum;
		dum->next = beg;

		int pivot = beg->data;

		while (ptr != dum) {
			Node *cut = ptr;
			ptr = ptr->next;
			if (pivot > cut->data) {
				cut = cut_out(cut);
				Node *next = dum->next;
				cut->next = next;
				next->prev = cut;
				dum->next = cut;
				cut->prev = dum;
			}
			else if (pivot == cut->data) {
				rightEq = cut;
			}
		}

		Node *left = dum->next;
		left->prev = nullptr;
		Node *right = dum->prev;
		right->next = nullptr;

		delete dum;

		pnn l({ beg,beg }), r({ beg,beg });

		if (leftEq != left) l = sort(left, leftEq->prev);
		if (rightEq != right) r = sort(rightEq->next, right);

		if (l.second != r.first) {
			l.second->next = leftEq;
			leftEq->prev = l.second;
			r.first->prev = rightEq;
			rightEq->next = r.first;
		}

		return{ l.first, r.second };
	}
};

random_device rd;
mt19937 gen(rd());
uniform_int_distribution<> dis(1, 500);

int m_rand() {
	return dis(gen);
}

LinkedList *randomList(int count) {
	LinkedList *result = new LinkedList();
	for (int i = 0; i < count; ++i) {
		if (i % 2 == 0) result->add_front(m_rand());
		else result->add_back(m_rand());
	}
	return result;
}

int main() {
	LinkedList *ll = randomList(1);
	cout << "Size : " << ll->getSize() << endl;

	cout << "Unsorted:\n\n";
	for (Node *ptr = ll->getFirst(); ptr != nullptr; ptr = ptr->next) {
		cout << ptr->data << endl;
	}
	
	ll->sort2();
	cout << "Sorted:\n\n";
	for (Node *ptr = ll->getFirst(); ptr != nullptr; ptr = ptr->next) {
		cout << ptr->data << endl;
	}
	delete ll;
}
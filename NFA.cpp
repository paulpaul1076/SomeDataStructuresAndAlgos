#include <iostream>
#include <vector>
#include <string>
#include <stack>
#include <algorithm>
#include <unordered_map>
#include <unordered_set>
#include <assert.h>
#include <map>
#include <set>

using namespace std;

class Digraph {
private:
    vector<int> *adjList;
    int v;
    int e;
public:
    Digraph(int N)
            : v(N), e(0) {
        adjList = new vector<int>[N];
    }

    int V() const {
        return v;
    }

    int E() const {
        return e;
    }

    void add(int v, int w) {
        e++;
        adjList[v].push_back(w);
    }

    const vector<int> &adj(int v) const {
        return adjList[v];
    }

    ~Digraph() {
        delete[] adjList;
    }
};

class DirectedDFS {
private:
    vector<bool> marked;

    void dfs(const Digraph &G, int v) {
        marked[v] = true;
        for (int x : G.adj(v)) {
            if (!marked[x]) dfs(G, x);
        }
    }

public:
    DirectedDFS(const Digraph &G, int v)
            : marked(G.V()) {
        dfs(G, v);
    }

    DirectedDFS(const Digraph &G, const vector<int> &vertices)
            : marked(G.V()) {
        for (int x : vertices) {
            if (!marked[x]) dfs(G, x);
        }
    }

    bool is_marked(int v) const {
        return marked[v];
    }
};

class FULLNFA {
private:
public:
    string regex;
    int end;
    Digraph *G = nullptr;
    map<int, set<char>> charSets;
    int groupCount = -1;
    map<int, set<char>> charSets1;

    void add_to_set(int pos, const string &charGroup) {
        set<char> result;
        int startPos = 1;
        if (charGroup[startPos] == '^') startPos++;
        for (int i = startPos; i < charGroup.size() - 1; ++i) {
            if (i < charGroup.size() - 2 && charGroup[i + 1] == '-') {
                char start = charGroup[i];
                char end = charGroup[i + 2];
                for (char ch = start; ch <= end; ++ch) {
                    result.insert(ch);
                }
                i += 2;
            } else {
                result.insert(charGroup[i]);
            }
        }
        charSets[++groupCount] = result;
    }

    string int_to_string(int i) {
        string result;
        if (i == 0) {
            result.push_back('0');
            return result;
        }
        while (i > 0) {
            int rem = i % 10;
            result.push_back('0' + rem);
            i /= 10;
        }
        reverse(result.begin(), result.end());
        return result;
    }

    string remove_groups(const string &s) {
        string result;
        for (int i = 0; i < s.size(); ++i) {
            if (s[i] == '[') {
                string charGroup;
                int ptr = i;
                while (s[ptr] != ']') {
                    charGroup.push_back(s[ptr++]);
                }
                charGroup.push_back(']');
                add_to_set(i, charGroup);
                if (charGroup[1] == '^') {
                    result += "(~" + int_to_string(groupCount) + ")";
                } else {
                    result += "(`" + int_to_string(groupCount) + ")";
                }
                i = ptr;
            } else {
                result.push_back(s[i]);
            }
        }
        return result;
    }

    string generate_repeat(int howMany, const string &subExp) {
        string result;
        for (int i = 0; i < howMany; ++i) {
            result += subExp;
        }
        return result;
    }

    string generate_or_repeat(int from, int to, const string &subExp) {
        string result = "(";
        string curRep;
        for (int i = 0; i < from; ++i) {
            curRep += subExp;
        }
        result += curRep;
        curRep += subExp;
        for (int j = 0; j < to - from; ++j) {
            result += ("|" + curRep);
            curRep += subExp;
        }
        result.push_back(')');
        return result;
    }

    pair<int, int> get_range(const string &range) {
        int x = 0;
        int y = 0;
        int ptr = 1;
        for (; range[ptr] != '-' && range[ptr] != '}'; ++ptr) {
            x += (range[ptr] - '0');
            x *= 10;
        }
        x /= 10;
        if (range[ptr] == '-') ptr++;
        for (; range[ptr] != '}'; ++ptr) {
            y += (range[ptr] - '0');
            y *= 10;
        }
        y /= 10;
        return {x, y};
    }

    string remove_repeats(const string &s) {
        string temp = s;
        while (true) {
            int ptr = temp.size() - 1;
            string rightPart;
            for (; ptr >= 0; --ptr) {
                if (temp[ptr] == '}') {
                    break;
                }
                rightPart.push_back(temp[ptr]);
            }
            if (ptr == -1) break;
            string range;
            while (temp[ptr] != '{') {
                range.push_back(temp[ptr--]);
            }
            range.push_back('{');
            reverse(range.begin(), range.end());
            pair<int, int> s_f_p = get_range(range);
            int start = s_f_p.first;
            int finish = s_f_p.second;
            string subExp;
            ptr--;
            if (temp[ptr] != ')') {
                subExp.push_back(temp[ptr]);
            } else {
                int parenCount = 0;
                while (parenCount != 1 || temp[ptr] != '(') {
                    if (temp[ptr] == ')') parenCount++;
                    else if (temp[ptr] == '(') parenCount--;
                    subExp.push_back(temp[ptr]);
                    ptr--;
                }
                subExp.push_back('(');
            }
            reverse(subExp.begin(), subExp.end());
            string repeat;
            if (finish == 0) {
                repeat = generate_repeat(start, subExp);
            } else {
                repeat = generate_or_repeat(start, finish, subExp);
            }
            reverse(rightPart.begin(), rightPart.end());
            temp = temp.substr(0, ptr) + repeat + rightPart;
        }
        return temp;
    }

    string remove_question_marks(const string &s) {
        string result;
        for (int i = 0; i < s.size(); ++i) {
            if (s[i] == '?') {
                result += "{0-1}";
            } else {
                result.push_back(s[i]);
            }
        }
        return result;
    }

    int extract_number(const string &s) {
        int ans = 0;
        int ptr = 0;
        while (ptr != s.size() && s[ptr] < '0' || s[ptr] > '9') {
            ptr++;
        }
        while (ptr != s.size() && (s[ptr] >= '0' && s[ptr] <= '9')) {
            ans = ans * 10 + (s[ptr] - '0');
            ptr++;
        }
        return ans;
    }

    string transform_group_numbers(const string &s) {
        string result;
        for (int i = 0; i < s.size(); ++i) {
            if (s[i] == '(' && (s[i + 1] == '~' || s[i + 1] == '`')) {
                int ptr = i;
                string exp;
                while (s[ptr] != ')') {
                    exp.push_back(s[ptr++]);
                }
                int whichGroup = extract_number(exp);
                int newGroupN = result.size();
                charSets1[newGroupN] = charSets[whichGroup];
                result.push_back(s[i + 1]);
                i += exp.size();
            } else {
                result.push_back(s[i]);
            }
        }
        return result;
    }

    void set_and_transform_regex(string input) {
        input = remove_groups(input);
        input = remove_question_marks(input);
        input = remove_repeats(input);
        regex = transform_group_numbers(input);
//        cout << "regex=" << regex << endl;
    }

public:
    FULLNFA(const string &regex_) {
        set_and_transform_regex(regex_);
        end = regex.size();
        G = new Digraph(end + 1);
        stack<int> ops;
        for (int i = 0; i < regex.size(); ++i) {
            int lp = i;
            if (regex[i] == '(' || regex[i] == '|') {
                ops.push(i);
            } else if (regex[i] == ')') {
                vector<int> pipePositions;
                int _or = ops.top();
                ops.pop();
                while (regex[_or] == '|') {
                    pipePositions.push_back(_or);
                    _or = ops.top();
                    ops.pop();
                }
                lp = _or;
                for (int pipePos : pipePositions) {
                    G->add(lp, pipePos + 1);
                    G->add(pipePos, i);
                }
            }
            if (i < regex.size() - 1 && regex[i + 1] == '*') {
                G->add(lp, i + 1);
                G->add(i + 1, lp);
            }
            else if (i < regex.size() - 1 && regex[i + 1] == '+') {
                G->add(i + 1, lp);
            }
            if (regex[i] == '*' || regex[i] == ')' || regex[i] == '(' || regex[i] == '+') {
                G->add(i, i + 1);
            }
        }
    }

    bool recognizes(const string &word) {
        vector<int> reachable;
        DirectedDFS *dfs = new DirectedDFS(*G, 0);
        for (int i = 0; i < G->V(); ++i) {
            if (dfs->is_marked(i)) reachable.push_back(i);
        }
        for (int i = 0; i < word.size(); ++i) {
            vector<int> matched;
            for (int x : reachable) {
                if (x < end &&
                    (regex[x] == word[i] || regex[x] == '.' ||
                     (regex[x] == '~' && charSets1[x].find(word[i]) == charSets1[x].end()) ||
                     (regex[x] == '`' && charSets1[x].find(word[i]) != charSets1[x].end()))) {
                    matched.push_back(x + 1);
                }
            }
            delete dfs;
            dfs = new DirectedDFS(*G, matched);
            reachable.clear();
            for (int j = 0; j < G->V(); ++j) {
                if (dfs->is_marked(j)) reachable.push_back(j);
            }
        }
        delete dfs;
        for (int x : reachable) if (x == end) return true;
        return false;
    }

    ~FULLNFA() {
        delete G;
    }
};

int main() {
    FULLNFA nfa("(tes(([^1-9abc]{2-4}){1-2})*){3}");
    cout << boolalpha;
    cout << nfa.recognizes("tesddddddtesddddddtesddddddddddd");

}
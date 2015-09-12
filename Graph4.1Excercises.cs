using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Numerics;
using System.Reflection;

class Graph
{
	private List<int>[] adjList;
	private int v;
	private int e;
	public Graph(int size)
	{
		v = size;
		adjList = new List<int>[size];
		for(int i = 0; i < size; ++i)
		{
			adjList[i] = new List<int>();
		}
	}
	private Graph(List<int>[] adjList, int v, int e)
	{
		this.v = v;
		this.e = e;
		this.adjList = adjList;
	}
	public void AddEdge(int v, int w)
	{
		adjList[v].Add(w);
		adjList[w].Add(v);
		++e;
	}
	public int E => e;
	public int V => v;
	public IEnumerable<int> Adj(int v) => adjList[v];

	public Graph CloneExcludingEdge(int v, int w)
	{
		//Console.WriteLine("V = " + V);
		List<int>[] newAdjList = new List<int>[V];
		for (int i = 0; i < V; ++i) newAdjList[i] = new List<int>();
		bool edgeFound = false;
		for(int i = 0; i < V; ++i)
		{
			foreach (int x in Adj(i))
			{
				if (i == v && x == w || i == x && i == w)
				{
					edgeFound = true;
					continue;
				}
				newAdjList[i].Add(x);
			}
		}
		return new Graph(newAdjList, V, (edgeFound ? e - 1 : e)); // WROTE v INSTEAD OF V BEFORE!!!!!!!!!!!!!!!!!!!! DIST[].LENGTH = 0 AND INDEX OUT OF RANGE EXCEPTION.... IF v = 0!!!!!!!!!!!!!!!!!!!!
	}
	public void PrintLists()
	{
		for(int i = 0; i < adjList.Length; ++i)
		{
			Console.Write(i + ": ");
			for(int j = 0; j < adjList[i].Count; ++j)
			{
				Console.Write(adjList[i][j] + ", ");
			}
			Console.WriteLine();
		}
	}
}

class DFS
{
	private bool[] marked;
	private int count;
	public DFS(Graph g, int source)
	{
		marked = new bool[g.V];
		Dfs(g, source);
	}

	private void Dfs(Graph g, int v)
	{
		marked[v] = true;
		foreach(var x in g.Adj(v))
		{
			if (!marked[x])
			{
				count++;
				Dfs(g, x);
			}
		}
	}
	public bool IsConnected(int v) => marked[v];
	public int Count => count;
}

class BFS
{
	private bool[] marked;
	private int[] dist;
	private int count;
	private int smallestCycle = -1;
	private int source;
	private Graph g;
	public BFS(Graph g, int source)
	{
		marked = new bool[g.V];
		dist = new int[g.V];
		this.source = source;
		this.g = g;
		for (int i = 0; i < g.V; ++i) dist[i] = 777;
		Bfs(g, source);
	}
	public int SmallestCycle()
	{
		if(smallestCycle == -1)
		{
			smallestCycle = 777;
			for (int i = 0; i < g.V; ++i) // check distances and find smallest
			{
				if (dist[i] == 1)
				{
					Console.WriteLine("Distance between {0} and {1} is 1", source, i);
					Graph gCopy = g.CloneExcludingEdge(source, i);
					BFS temp = new BFS(gCopy, source);
					smallestCycle = smallestCycle < temp.DistTo(i) + 1 ? smallestCycle : temp.DistTo(i) + 1;
				}
			}
		}
		return smallestCycle;
	}

	private void Bfs(Graph g, int v)
	{
		Queue<int> q = new Queue<int>();
		q.Enqueue(v);
		Console.WriteLine("dist.length = " + dist.Length);
		Console.WriteLine("v = " + v);
		dist[v] = 0;// An unhandled exception of type 'System.IndexOutOfRangeException' 
		marked[v] = true;
		while (q.Count != 0)
		{
			int deqqed = q.Dequeue();
			Console.WriteLine("\nDeqqed: {0}", deqqed);
			Console.Write("marked[]: ");
			foreach (bool x in marked) Console.Write(x? "1,": "0,");
			Console.Write("\ndist[]: ");
			foreach (int x in dist) Console.Write(x + ", ");
			foreach(int x in g.Adj(deqqed))
			{
				if (!marked[x])
				{
					marked[x] = true;
					dist[x] = dist[deqqed] + 1;
					count++;
					q.Enqueue(x);
				}
			}
			Console.WriteLine();
		}
		Console.WriteLine("dist[] after bfs:");
		foreach (int x in dist) Console.Write(x + ", ");
	}
	public bool IsConnected(int v) => marked[v];
	public int Count => count;
	public int DistTo(int v)
	{
		return dist[v];
	}
	public int MaxDist => dist.Max();
	public int MinDist => dist.Min();

}

class UF
{
	private int[] id;
	private int[] sz;
	private int count;
	public UF(int size)
	{
		id = new int[size];
		sz = new int[size];
		for(int i = 0; i < size; ++i)
		{
			id[i] = i;
			sz[i] = 1;
		}
		count = size;
	}
	public void Union(int x, int y)
	{
		int xid = Find(x);
		int yid = Find(y);
		if (xid == yid) return;

		if(sz[xid] > sz[yid])
		{
			sz[xid] += sz[yid];
			id[yid] = xid;
		}
		else
		{
			sz[yid] += sz[xid];
			sz[xid] = yid;
		}

		--count;
	}
	public int Find(int x)
	{
		while (x != id[x]) x = id[x];
		return x;
	}
	public bool Connected(int x, int y) => Find(x) == Find(y);
	public int Count() => count;
	public int Count(int v) => sz[Find(v)];
}

class UFSearch
{
	private UF ufObj;
	private int s;
	public UFSearch(Graph g, int source)
	{
		s = source;
		UF uf = new UF(g.V);
		Queue<int> q = new Queue<int>();
		q.Enqueue(source);
		while (q.Count != 0)
		{
			int deqqed = q.Dequeue();
			foreach(int x in g.Adj(deqqed))
			{
				if(!uf.Connected(deqqed, x))
					uf.Union(deqqed, x);
			}
		}
		ufObj = uf;
	}
	public bool IsConnected(int v) => ufObj.Connected(s, v);
	public int Count() => ufObj.Count(s);
}

class GraphProperties
{
	private int radius;
	private int center;
	private int diameter;
	private int wienerIndex;
	private int girth;
	public GraphProperties(Graph g)
	{
		int size = g.V;
		radius = int.MaxValue;
		girth = 777;
		for (int i = 0; i < size; ++i)
		{
			BFS temp = new BFS(g, i);
			radius = (radius < temp.MinDist ? radius : temp.MinDist);
			diameter = (diameter > temp.MaxDist ? diameter : temp.MaxDist);
			for(int j = i + 1; j < size; ++j)
			{
				wienerIndex += temp.DistTo(j);
			}
			girth = girth < temp.SmallestCycle() ? girth : temp.SmallestCycle();
		}
		for(int i = 0; i < size; ++i)
		{
			BFS temp = new BFS(g, i);
			if(temp.MaxDist == radius)
			{
				center = i;
				break;
			}
		}
	}
	public int Diameter => diameter;       // max eccentricity of any vertex
	public int Radius => radius;           // smallest eccentricity of any vertex
	public int Center => center;           // a vertex whose eccentricity is the radius
	public int WienerIndex => wienerIndex; // sum of the lengths of the shortest paths between all pairs of verteces
	public int Girth => girth;             // the length of the shortest cycle, is acyclic, then infinite (-1)
}

class CC
{
	private int[] id;
	private bool[] marked;
	private int count;

	public CC(Graph g)
	{
		int size = g.V;
		id = new int[size];
		marked = new bool[size];
		for(int i = 0; i < size; ++i)
			if (!marked[i])
			{
				Dfs(g, i);
				count++;
			}
	}
	private void Dfs(Graph g, int v)
	{
		marked[v] = true;
		id[v] = count;
		foreach(int x in g.Adj(v))
			if (!marked[x])
				Dfs(g, x);
	}
	public bool IsConnected(int v, int w) => id[v] == id[w];
	public int Count => count;
	public int Id(int v) => id[v];
}

class CycleFinding
{
	private bool hasCycle = false;
	private bool[] marked;
	private int callNum = 0;
	public CycleFinding(Graph g)
	{
		marked = new bool[g.V];
		for(int i = 0; i < g.V && !hasCycle; ++i) // might have disconnected components, so gotta iterate over all verteces
		{
			if (!marked[i])
			{
				Dfs(g, i, i);
			}
		}
	}
	private void Dfs(Graph g, int current, int father)
	{
		Console.WriteLine("callNum = {0}", callNum++);
		marked[current] = true;
		Console.WriteLine("current = {0}, father = {1}", current, father);
		foreach(int x in g.Adj(current))
		{
			if (!marked[x]) Dfs(g, x, current);
			else if (x != father)
			{
				Console.WriteLine("father = {0}, current = {1}, x = {2}, ", father, current, x);
				hasCycle = true;
			}
		}
	}
	public bool HasCycle => hasCycle;
}

class BipartiteFinding
{
	private bool[] marked;
	private bool isBipartite;
	private bool[] color;
	public BipartiteFinding(Graph g)
	{
		isBipartite = true;
		marked = new bool[g.V];
		color = new bool[g.V];
		for (int i = 0; i < g.V; ++i)
			if (!marked[i])
				Dfs(g, i);
	}

	private void Dfs(Graph g, int v)
	{
		foreach(int x in g.Adj(v))
		{
			if (!marked[x])
			{
				color[x] = !color[v];
				Dfs(g, x);
			}
			else
			{
				if (color[x] == color[v]) isBipartite = false;
			}
		}
	}
	public bool IsBipartite => isBipartite;
}

class SymbolGraph
{
	private Dictionary<string, int> st;
	private string[] inverseSt;
	private Graph g;
	public SymbolGraph(List<string> p, string sp)
	{
		st = new Dictionary<string, int>();
		for(int i = 0; i < p.Count; ++i)
		{
			string[] names = p[i].Split(sp.ToArray());
			for(int j = 0; j < names.Length; ++j)
				if (!st.ContainsKey(names[j]))
					st.Add(names[j], st.Count);
		}
		inverseSt = new string[st.Count];
		foreach (KeyValuePair<string, int> kvp in st)
			inverseSt[kvp.Value] = kvp.Key;
		g = new Graph(st.Count);
		for (int i = 0; i < p.Count; ++i)
		{
			string[] names = p[i].Split(sp.ToArray());
			int v = st[names[0]];
			for(int j = 1; j < names.Length; ++j)
			{
				g.AddEdge(v, st[names[j]]);
			}
		}
	}
	public int Index(string name) => st[name];
	public string Name(int index) => inverseSt[index];
}

class BFSPaths
{
	private bool[] marked;
	private int[] parent;
	private int source;
	public BFSPaths(Graph g, int source)
	{
		this.source = source;
		marked = new bool[g.V];
		parent = new int[g.V];
		for (int i = 0; i < g.V; ++i) parent[i] = -1;
		Bfs(g, source);
	}
	private void Bfs(Graph g, int source)
	{
		Queue<int> q = new Queue<int>();
		q.Enqueue(source);
		marked[source] = true;
		parent[source] = source;
		while(q.Count != 0)
		{
			int deqqed = q.Dequeue();
			foreach(int x in g.Adj(deqqed))
			{
				if (!marked[x])
				{
					marked[x] = true;
					parent[x] = deqqed;
					q.Enqueue(x);
				}
			}
		}
	}
	public bool HasPathTo(int v)
	{
		return marked[v];
	}
	public IEnumerable<int> PathTo(int v)
	{
		Stack<int> stack = new Stack<int>();
		while(v != parent[v])
		{
			stack.Push(v);
			v = parent[v];
		}
		stack.Push(source);
		return stack;
	}
}

class DFSPaths
{
	private bool[] marked;
	private int[] parent;
	private int source;
	public DFSPaths(Graph g, int source)
	{
		marked = new bool[g.V];
		parent = new int[g.V];
		this.source = source;
		for (int i = 0; i < g.V; ++i) parent[i] = -1;
		parent[source] = source;
		Dfs(g, source);
	}
	private void Dfs(Graph g, int v)
	{
		marked[v] = true;
		foreach(int x in g.Adj(v))
		{
			if (!marked[x])
			{
				parent[x] = v;
				Dfs(g, x);
			}
		}
	}
	public bool HasPathTo(int v) => marked[v];
	public IEnumerable<int> PathTo(int v)
	{
		Stack<int> stack = new Stack<int>();
		while(v != parent[v])
		{
			stack.Push(v);
			v = parent[v];
		}
		stack.Push(source);
		return stack;
	}
}

class DegreesOfSeparationDFS
{
	private int[] degree;
	private bool[] marked;
	private int source;
	private const int INF = 777;
	public DegreesOfSeparationDFS(Graph g, int source)
	{
		this.source = source;
		degree = new int[g.V];
		marked = new bool[g.V];
		for (int i = 0; i < g.V; ++i) degree[i] = INF;
		degree[source] = 0;
		Dfs(g, source);
	}
	private void Dfs(Graph g, int v)
	{
		marked[v] = true;
		foreach(int x in g.Adj(v))
		{
			if (!marked[x])
			{
				degree[x] = degree[v] + 1;
				Dfs(g, x);
			}
		}
	}
	public int Degree(int v)
	{
		return degree[v];
	}
}

class DegreesOfSeparationBFS
{
	private int[] degree;
	private bool[] marked;
	private int source;
	private const int INF = 777;
	public DegreesOfSeparationBFS(Graph g, int source)
	{
		this.source = source;
		degree = new int[g.V];
		marked = new bool[g.V];
		for(int i = 0; i < g.V; ++i) degree[i] = INF;
		degree[source] = 0;
		Bfs(g, source);
	}
	private void Bfs(Graph g, int v)
	{
		Queue<int> q = new Queue<int>();
		q.Enqueue(v);
		marked[v] = true;
		while (q.Count != 0)
		{
			int deqqed = q.Dequeue();
			foreach(int x in g.Adj(deqqed))
			{
				if (!marked[x])
				{
					marked[x] = true;
					degree[x] = degree[deqqed] + 1;
					q.Enqueue(x);
				}
			}
		}
	}
	public int Degree(int v)
	{
		return degree[v];
	}
}

class ModifiedCycle 
{
	private bool[] marked;
	private bool hasCycle = false;
	private bool[,] neighbors;
	public ModifiedCycle(Graph g)
	{
		marked = new bool[g.V];
		neighbors = new bool[g.V, g.V];
		for(int i = 0; i < g.V && !hasCycle; ++i)
		{
			if (!marked[i])
				Dfs(g, i, i);
		}
	}
	private void Dfs(Graph g, int current, int parent)
	{ 
		marked[current] = true;
		foreach(int x in g.Adj(current))
		{
			if (marked[x] && x != parent)						hasCycle = true;
			if (marked[x] && x == current)					hasCycle = true; // self loop
			if (marked[x] && neighbors[current, x]) hasCycle = true; // parallel edge
			if (!marked[x])
			{
				neighbors[current, x] = true;
				Dfs(g, x, current);
			}
		}
	}
	public bool HasCycle => hasCycle;
}

class Task
{
	public void Solve(InputReader In, StreamWriter Out)
	{
		Graph g = new Graph(3);
		g.AddEdge(0, 1);
		g.AddEdge(1, 2);
		//g.AddEdge(2, 1);
		g.PrintLists();
		Console.WriteLine(new CycleFinding(g).HasCycle);
	}
}
#region junk
class Program
{
	static void Main()
	{
		Task t = new Task();
		t.Solve(In, Out);
		Out.Flush();
		Out.Close();
		In.Close();
	}
#if FILE_INPUT
	static string path = Directory.GetParent(Directory.GetCurrentDirectory()).Parent.FullName;
	static InputReader In = new InputReader(path + @"/Input.txt");
	static StreamWriter Out = new StreamWriter(path + @"/Output.txt");
#else
	static InputReader In = new InputReader();
	static StreamWriter Out = new StreamWriter(Console.OpenStandardOutput());
#endif
}

class InputReader : IDisposable
{
	private StreamReader sr;
	private StringBuilder sb = new StringBuilder();
	public InputReader()
	{
		sr = new StreamReader(Console.OpenStandardInput());
	}
	public InputReader(string path)
	{
		sr = new StreamReader(path);
	}
	private char[] buffer = new char[1024];
	private int bufLen = -1;
	private int bufPtr = 0;
	public int NextToken()
	{
		if (bufLen == 0) throw new IOException();
		if (bufPtr >= bufLen)
		{
			bufLen = sr.Read(buffer, 0, 1024);
			if (bufLen == 0) return -1;
			bufPtr = 0;
		}
		return buffer[bufPtr++];
	}
	public int NextInt()
	{
		return int.Parse(Next());
	}
	public double NextDouble()
	{
		return double.Parse(Next());
	}
	public string Next()
	{
		sb.Clear();

		int ch = NextToken();
		while (ch != -1 && char.IsWhiteSpace((char)ch)) ch = NextToken();
		if (ch == -1) throw new IOException();
		while (ch != -1 && !char.IsWhiteSpace((char)ch))
		{
			sb.Append((char)ch);
			ch = NextToken();
		}

		return sb.ToString();
	}
	public char NextChar()
	{
		int ch = NextToken();
		while (ch != -1 && char.IsWhiteSpace((char)ch)) ch = NextToken();
		if (ch == -1) throw new IOException();
		return (char)ch;
	}
	public string NextLine()
	{
		sb.Clear();
		int ch = NextToken();
		while (ch != -1 && char.IsWhiteSpace((char)ch)) ch = NextToken();
		if (ch == -1) throw new IOException();
		while (ch != -1 && ch != '\n')
		{
			sb.Append((char)ch);
			ch = NextToken();
		}
		return sb.ToString().Trim();
	}
	public string[] ReadAllStrings()
	{
		string result = sr.ReadToEnd();
		return result.Split(new char[] { ' ', '\t', '\r', '\n' }, StringSplitOptions.RemoveEmptyEntries);
	}
	public string[] ReadAllLines()
	{
		string result = sr.ReadToEnd();
		return result.Split(new char[] { '\r', '\n' }, StringSplitOptions.RemoveEmptyEntries);
	}
	public void Close()
	{
		sr.Close();
	}
	public void Dispose()
	{
		sr.Close();
	}
}
#endregion
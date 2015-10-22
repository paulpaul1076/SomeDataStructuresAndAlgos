using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Numerics;
using System.Reflection;

class Task
{
	public void Solve(InputReader In, StreamWriter Out)
	{
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
		if(bufLen == 0) throw new IOException();
		if(bufPtr >= bufLen)
		{
			bufLen = sr.Read(buffer, 0, 1024);
			if(bufLen == 0) return -1;
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
		while(ch != -1 && char.IsWhiteSpace((char)ch)) ch = NextToken();
		if(ch == -1) throw new IOException();
		while(ch != -1 && !char.IsWhiteSpace((char)ch))
		{
			sb.Append((char)ch);
			ch = NextToken();
		}

		return sb.ToString();
	}
	public char NextChar()
	{
		int ch = NextToken();
		while(ch != -1 && char.IsWhiteSpace((char)ch)) ch = NextToken();
		if(ch == -1) throw new IOException();
		return (char)ch;
	}
	public string NextLine()
	{
		sb.Clear();
		int ch = NextToken();
		while(ch != -1 && char.IsWhiteSpace((char)ch)) ch = NextToken();
		if(ch == -1) throw new IOException();
		while(ch != -1 && ch != '\n')
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
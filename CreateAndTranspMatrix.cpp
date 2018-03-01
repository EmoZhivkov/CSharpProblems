#include "stdafx.h"
#include <iostream>
using namespace std;

void CreateAndTranspMatrix(int m, int n)
{
	int **matrix = new int*[m];
	for (int i = 0; i < m; i++)
	{
		matrix[i] = new int[n];
	}

	for (int i = 0; i < m; i++)
	{
		for (int j = 0; j < n; j++)
		{
			int h;
			cin >> h;
			*(*(matrix + i) + j) = h;
		}
	}
	cout << endl;

	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < m; j++)
		{
			
			cout << *(*(matrix + j) + i) << " ";
		}
		cout << endl;
	}
	
	delete[] matrix;
}




int main()
{
	int m, n;
	cin >> m >> n;
	cout << endl;

	CreateAndTranspMatrix(m, n);
	
    return 0;
}


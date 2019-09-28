#include <stdio.h>
#include <stdlib.h>
#include <direct.h>
#include <iostream>
#include <string>

using namespace std;

int main()
{
	char *work;
	if((work = getcwd(NULL, 0)) == NULL)
	{
		perror("获取运行目录失败");
	}
	else
	{
        printf("工作在：%s\n", work);
	}

	string workDir=work;
	string java="\\java\\bin\\java.exe -jar ";
	string program="\\dogename.jar\"";
	
	//string maincommand="start ";
	//string command=maincommand+"\""+workDir+java+workDir+program;

	string command="\""+workDir+java+workDir+program;

	cout<< command <<endl;

	system(command.c_str());

	return 0;
}
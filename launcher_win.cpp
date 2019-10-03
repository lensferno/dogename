#include <stdio.h>
#include <stdlib.h>
#include <direct.h>
#include <iostream>
#include <string>
#include <unistd.h>

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
        printf("working in：%s\n", work);
	}

	string workDir=work;
	
	string program=workDir+"\\java\\bin\\java.exe";
	string jarFile=workDir+"\\dogename.jar";

	const char *args[]={"java.exe","-jar",jarFile.data(),NULL};

	execv(program.data(),args);

	return 0;
}
#include <iostream>
#include <vector>
#include <string>
#include <direct.h>
#include <windows.h>
#include <windows.h>
#include <winnt.h>
#include <cstdio>

using namespace std;

int main()
{

	char *work;
	if((work = _getcwd(NULL, 0)) == NULL)
	{
        MessageBoxW(GetForegroundWindow(),TEXT("出错啦！"),TEXT("获取运行目录失败 :("),MB_ICONQUESTION);
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
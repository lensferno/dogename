#include <iostream>
#include <stdio.h>
#include <string>
#include <intrin.h>
#include <c++/4.8.3/fstream>

inline int CopyFile(char *SourceFile, char *NewFile)
{
    std::ifstream in;
    std::ofstream out;

    try
    {
        in.open(SourceFile, std::ios::binary);//打开源文件
        if (in.fail())//打开源文件失败
        {
            std::cout << "备份文件失败" << std::endl;
            in.close();
            out.close();
            return 0;
        }
        out.open(NewFile, std::ios::binary);//创建目标文件
        if (out.fail())//创建文件失败
        {
            std::cout << "备份文件失败" << std::endl;
            out.close();
            in.close();
            return 0;
        }
        else//复制文件
        {
            out << in.rdbuf();
            out.close();
            in.close();
            return 1;
        }
    }
    catch (std::exception e)
    {
    }
}

int main(int argc, char* argv[]) {


    for (int i = 0; i < argc; i++) {
        std::cout << "[get]:argument " << i << " = " << argv[i] << std::endl;
    }
    const char* javaPid=argv[1];
    const char* compressCmd=argv[2];
    const char* jarPath=argv[3];
    const char* backupPath=argv[4];
    CopyFile(jarPath,backupPath);

    const char* killCmd="taskkill ";
    char commandTemp[20];
    strcpy(commandTemp,killCmd);
    strcat(commandTemp,javaPid);
    killCmd=commandTemp;
    std::cout << "Command:" <<killCmd<<"\n";
    system(killCmd);



    std::cout << "-------------释放升级文件----------"<<std::endl;
    system(compressCmd);

    std::cout << "----------------------\n\n\n\n结束\n如果不能正常启动，请将文件"<<backupPath<<"重命名为\"dogename.jar\"";
    system("pause");
    return 0;
}

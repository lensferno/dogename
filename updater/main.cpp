#include <iostream>
#include <stdio.h>
#include <string>
#include <intrin.h>

int main(int argc, char* argv[]) {

    const char* javaPid=argv[1];
    const char* killCmd="taskkill ";
    char commandTemp[20];
    strcpy(commandTemp,killCmd);
    strcat(commandTemp,javaPid);
    std::cout << "Command:" <<commandTemp<<"\n";
    //system(commandTemp);
    for (int i = 0; i < argc; i++) {
        std::cout << "argument " << i << " = " << argv[i] << std::endl;
    }

    std::cout << "Hello, World!" << std::endl;
    std::cout << "";
    system("pause");
    return 0;
}

#include <iostream>
#include <stdio.h>
#include <string>
#include <intrin.h>
#include <c++/4.8.3/fstream>
#include <combaseapi.h>
#include <shobjidl.h>
#include <shlguid.h>
#include <shlobj.h>
#include <rpcdce.h>

int main(int argc, char* argv[]) {


    for (int i = 0; i < argc; i++) {
        std::cout << "[get]:argument " << i << " = " << argv[i] << std::endl;
    }
    const char* javaPid=argv[1];
    const char* compressCmd=argv[2];
    const char* recoverBatDir=argv[3];



    const char* killCmd="taskkill ";
    char commandTemp[20];
    strcpy(commandTemp,killCmd);
    strcat(commandTemp,javaPid);
    killCmd=commandTemp;
    std::cout << "Command:" <<killCmd<<"\n";
    system(killCmd);



    std::cout << "-------------释放升级文件----------"<<std::endl;
    std::cout << "Do:"<<compressCmd<<std::endl;
    system(compressCmd);

    //---------创图标------------
    HRESULT hr = CoInitialize( NULL );
    if ( SUCCEEDED( hr ) )
    {
        IShellLink *pisl;
        hr = CoCreateInstance( CLSID_ShellLink, NULL,
                               CLSCTX_INPROC_SERVER, IID_IShellLink, (void * *) &pisl );
        if ( SUCCEEDED( hr ) )
        {
            IPersistFile* pIPF;

            pisl->SetPath( recoverBatDir);
            hr = pisl->QueryInterface( IID_IPersistFile, (void * *) &pIPF );
            if ( SUCCEEDED( hr ) )
            {LPITEMIDLIST pidl;
                LPMALLOC pShellMalloc;
                char szDir[200];
                if (SUCCEEDED(SHGetMalloc(&pShellMalloc)))
                {
                    if (SUCCEEDED(SHGetSpecialFolderLocation(NULL, CSIDL_DESKTOP, &pidl))) {
                        // 如果成功返回true
                        SHGetPathFromIDListA(pidl, szDir);
                        pShellMalloc->Free(pidl);
                    }
                    pShellMalloc->Release();
                }

                const char* desktopDir=szDir;
                std::string lnkName="回滚dogename的版本.lnk";
                std::string n_str;
                n_str=desktopDir;
                n_str+=lnkName;
                std::cout<<"desktop loca:"<<n_str<<std::endl;
                pIPF->Save(reinterpret_cast<LPCOLESTR>(n_str.data()), FALSE );
                pIPF->Release();
            }
            pisl->Release();
        }
        CoUninitialize();
    }


    std::cout << "----------------------\n\n\n\n结束\n如果不能正常启动，请运行桌面上的“回滚dogename的版本”";
    system("pause");
    return 0;
}

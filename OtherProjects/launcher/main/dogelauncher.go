package main

import (
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"time"

	"github.com/axgle/mahonia"
)

var workDir string

func main() {
	//welcome message
	fmt.Println("\n\n ")
	fmt.Println("－－－－－Ｄｏｇｅｎａｍｅ启动程序－－－－－")
	fmt.Println("\nＣｒｅａｔｅｄ　ｂｙ　Ｈｅ．ｔ．ｙ　　\n ")
	fmt.Println("－－－－－－－－－－－－－－－－－－－－－－")
	fmt.Println("version：0.0.2；build with golang 1.14,windows/amd64")

	var err error = nil
	workDir, err = filepath.Abs(filepath.Dir(os.Args[0]))

	if err != nil {
		fmt.Println("获取运行目录失败！")
		pause()
		return
	}
	fmt.Println("\n-→当前程序运行在：" + workDir + "")

	var mainProgramFile string = workDir + "\\dogename.jar"

	fileOK, programFileStat := checkMainProgramFile(mainProgramFile)

	if !fileOK {
		fmt.Println("\n-→主程序文件\"dogename.jar\"状态：" + programFileStat)
		fmt.Println("\n－－－－－－－－－－－－－－－－－－－－－－")
		fmt.Println("出错了，溜了溜了")
		fmt.Println("")
		pause()
		//pause
		return
	}

	fmt.Println("-→" + programFileStat)

	//--------------------check java runtime--------------------------------

	var installedJavaPath string = workDir + "\\java1.8\\"
	var javaRuntimePath string = installedJavaPath + "bin\\java.exe"

	runtimeOK, runtimeStat := findJavaRuntime(javaRuntimePath)

	fmt.Println("查找内置Java环境：")
	//if find java in work dir not ok
	if !runtimeOK {
		fmt.Println("-→" + runtimeStat)
		fmt.Println("")

		fmt.Println("查找已安装Java环境：")
		//find installed java ok
		runtimeOK, runtimeStat, installedJavaPath = findInstalledJava()

		//if find installed java not ok
		if !runtimeOK {
			fmt.Println("-→" + runtimeStat)
			pause()
			//pause
			return
		}

		//if find insatlled java ok
		fmt.Println("-→" + runtimeStat + installedJavaPath)

		javaRuntimePath = installedJavaPath

	} else {
		//find java in workdir ok
		fmt.Println("-→" + runtimeStat)
	}

	//run java
	runDogename(workDir, javaRuntimePath, mainProgramFile)
	fmt.Println("15秒后自动关(bào)闭(zhà)ssss...")
	time.Sleep(15 * time.Second)
}

//--------------------------checking-----------------------------------------------------------
func checkMainProgramFile(filePath string) (bool, string) {
	_, err := os.Stat(filePath)
	if err == nil {
		return true, "文件存在。"
	}
	if os.IsNotExist(err) {
		return false, "主程序文件不存在，请前往本程序的工作目录检查，若dogename.jar文件丢失，请从github.com/eatenid/dogename/releases下载并将其放置在：" + workDir + " 目录中。"
	}
	return false, "未知错误，请前往本程序的工作目录检查。"
}

func findJavaRuntime(javaPath string) (bool, string) {
	_, err := os.Stat(javaPath)
	if err == nil {
		return true, "内置Java环境找到，优先使用此环境启动......"
	}
	if os.IsNotExist(err) {
		return false, "找不到内置的Java运行环境，尝试查找此电脑上的Java安装信息......"
	}
	return false, "未知错误，尝试查找此电脑上的Java安装信息......"
}

func findInstalledJava() (bool, string, string) {

	javaRunPath, err := exec.LookPath("java")

	if err != nil {
		return false, "", javaRunPath
	}
	return true, "查找到，位于：", javaRunPath

}

//------------------------------------------------------------------------------------------

func pause() {
	fmt.Println("可以关了，呜呜呜QAQ")
	time.Sleep(240 * time.Second)
}

//--------------------------runner------------------------------------------------------------------
func runDogename(workDir string, javaRuntimePath string, mainProgramFile string) {
	fmt.Println("\n如果您不需要命令行输出信息，可以使用\"dogename_hide.exe\"代替本程序。\n ")
	cmd := exec.Command(javaRuntimePath, "-jar", mainProgramFile)
	cmd.Dir = workDir
	//err := cmd.Run()
	output, err := cmd.CombinedOutput()

	if err != nil {
		fmt.Println("\n程序运行失败：" + err.Error())
		enc := mahonia.NewDecoder("gb18030")
		utfStr := enc.ConvertString(string(output))
		fmt.Println(utfStr)

		pause()
		return
	}

	enc := mahonia.NewDecoder("gb18030")
	utfStr := enc.ConvertString(string(output))
	fmt.Println(utfStr)

}

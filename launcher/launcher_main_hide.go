package main

import (
	"fmt"
	"os"
	"os/exec"
	"path/filepath"

	"github.com/axgle/mahonia"
	termbox "github.com/nsf/termbox-go"
	registry "golang.org/x/sys/windows/registry"
)

var workDir string

func main() {
	//welcome message
	fmt.Println("\n\n ")
	fmt.Println("－－－－－Ｄｏｇｅｎａｍｅ启动程序－－－－－")
	fmt.Println("\nＣｒｅａｔｅｄ　ｂｙ　Ｈｅ．ｔ．ｙ　　\n ")
	fmt.Println("－－－－－－－－－－－－－－－－－－－－－－")
	fmt.Println("version：0.0.1；build with golang 1.14,windows/amd64")

	var err error = nil
	workDir, err = filepath.Abs(filepath.Dir(os.Args[0]))

	if err != nil {
		fmt.Println("获取运行目录失败！")
		pause()
		return
	}
	fmt.Println("\n信息：当前程序运行在：" + workDir + "")

	var mainProgramFile string = workDir + "\\dogename.jar"

	fileOK, programFileStat := checkMainProgramFile(mainProgramFile)

	if !fileOK {
		fmt.Println("\n检测主程序文件\"dogename.jar\"状态：" + programFileStat)
		fmt.Println("－－－－－－－－－－－－－－－－－－－－－－")
		fmt.Println("Ｅｘｉｔ　ｗｉｔｈ　ｅｒｒｏｒ")
		fmt.Println("")
		pause()
		//pause
		return
	}

	fmt.Println("检测主程序文件\"dogename.jar\"状态：" + programFileStat)

	//--------------------check java runtime--------------------------------

	var javaHome string = workDir + "\\java1.8\\"
	var javaRuntimePath string = javaHome + "bin\\javaw.exe"

	runtimeOK, runtimeStat := findJavaRuntime(javaRuntimePath)

	if !runtimeOK {
		fmt.Println("\n查找内置Java环境：" + runtimeStat)
		fmt.Println("")

		//find java from reg
		runtimeOK, runtimeStat, javaHome = findJavaRuntimeFromReg()

		if !runtimeOK {
			fmt.Println("查找已安装的Java环境：" + runtimeStat)
			pause()
			//pause
			return
		}

		//find java in reg ok
		fmt.Println("查找已安装的Java环境：" + runtimeStat + javaHome)

		javaRuntimePath = javaHome + "\\bin\\javaw.exe"

	} else {
		//find java in workdir ok
		fmt.Println("\n查找内置Java环境：" + runtimeStat)
	}

	//run java
	runDogename(workDir, javaRuntimePath, mainProgramFile)

}

//--------------------------checking-----------------------------------------------------------
func checkMainProgramFile(filePath string) (bool, string) {
	_, err := os.Stat(filePath)
	if err == nil {
		return true, "\n文件存在。"
	}
	if os.IsNotExist(err) {
		return false, "\n主程序文件不存在，请前往本程序的工作目录检查，若dogename.jar文件丢失，请从github.com/eatenid/dogename/releases下载并将其放置在：" + workDir + " 目录中。"
	}
	return false, "\n未知错误，请前往本程序的工作目录检查。"
}

func findJavaRuntime(javaPath string) (bool, string) {
	_, err := os.Stat(javaPath)
	if err == nil {
		return true, "\n找到，优先使用此环境启动......"
	}
	if os.IsNotExist(err) {
		return false, "\n内置Java运行环境不存在，尝试查找此电脑上的Java安装信息......"
	}
	return false, "\n未知错误，尝试查找此电脑上的Java安装信息......"
}

func findJavaRuntimeFromReg() (bool, string, string) {
	key, err := registry.OpenKey(registry.LOCAL_MACHINE, "SOFTWARE\\JavaSoft\\Java Runtime Environment\\1.8", registry.QUERY_VALUE)
	if err != nil {
		return false, "\n无法查询到此电脑上Java的安装信息，请前往Java.com下载（Java版本应为1.8版本），或下载附带Java运行环境的dogename。", "nil"
	}

	defer key.Close()

	javaHome, _, err := key.GetStringValue("JavaHome")
	if err != nil {
		return false, "\n到此电脑上Java的安装信息有错误，请前往Java.com下载重新安装（Java版本应为1.8版本），或下载附带Java运行环境的dogename。", "nil"
	}

	return true, "\n查找到，位于：", javaHome

}

//------------------------------------------------------------------------------------------
func init() {
	if err := termbox.Init(); err != nil {
		panic(err)
	}
	termbox.SetCursor(0, 0)
	termbox.HideCursor()
}

func pause() {
	fmt.Println("\n请按任意键按退出。")
Loop:
	for {
		switch ev := termbox.PollEvent(); ev.Type {
		case termbox.EventKey:
			break Loop
		}
	}
}

//--------------------------runner------------------------------------------------------------------
func runDogename(workDir string, javaRuntimePath string, mainProgramFile string) {
	fmt.Print("\n启动主程序中......")
	fmt.Println("\n如果您不需要命令行输出信息，可以使用\"launcher_hide.exe\"代替本程序。\n ")
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

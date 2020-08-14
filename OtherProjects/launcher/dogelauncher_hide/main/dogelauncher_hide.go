package main

import (
	"errors"
	"fmt"
	"io/ioutil"
	"os"
	"os/exec"
	"path/filepath"
	"strconv"
)

func init() {
	myPID := fmt.Sprint(os.Getpid())
	tmpDir := os.TempDir()

	if err := ProcExsit(tmpDir); err == nil {
		pidFile, _ := os.Create(tmpDir + "\\dogelauncher.pid")
		defer pidFile.Close()

		pidFile.WriteString(myPID)
		fmt.Print("My PID:" + myPID)
	} else {

		os.Exit(1)
	}
}

// 判断进程是否启动
func ProcExsit(tmpDir string) (err error) {
	myPIDFile, err := os.Open(tmpDir + "\\dogelauncher.pid")
	defer myPIDFile.Close()

	if err == nil {
		filePid, err := ioutil.ReadAll(myPIDFile)
		if err == nil {
			pidStr := fmt.Sprintf("%s", filePid)
			pid, _ := strconv.Atoi(pidStr)
			_, err := os.FindProcess(pid)
			if err == nil {
				//fmt.Print("PID " + pid + " has already exsit.")
				return errors.New("已经在运行了！")
			}
		}
	}

	return nil
}

var workDir string

func main() {

	var err error = nil
	workDir, err = filepath.Abs(filepath.Dir(os.Args[0]))

	if err != nil {
		return
	}
	var mainProgramFile string = workDir + "\\dogename.jar"

	fileOK, programFileStat := checkMainProgramFile(mainProgramFile)

	if !fileOK {
		return
	}

	fmt.Println("-→" + programFileStat)

	//--------------------check java runtime--------------------------------

	var installedJavaPath string = workDir + "\\java1.8\\"
	var javaRuntimePath string = installedJavaPath + "bin\\javaw.exe"

	runtimeOK, _ := findJavaRuntime(javaRuntimePath)

	//if find java in work dir not ok
	if !runtimeOK {
		//find installed java ok
		runtimeOK, _, installedJavaPath = findInstalledJava()

		//if find installed java not ok
		if !runtimeOK {
			return
		}

		//if find insatlled java ok

		javaRuntimePath = installedJavaPath

	} else {
		//find java in workdir ok
	}

	//run java
	runDogename(workDir, javaRuntimePath, mainProgramFile)
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

	javaRunPath, err := exec.LookPath("javaw")

	if err != nil {
		return false, "", javaRunPath
	}
	return true, "查找到，位于：", javaRunPath

}

//------------------------------------------------------------------------------------------

func pause() {
}

//--------------------------runner------------------------------------------------------------------
func runDogename(workDir string, javaRuntimePath string, mainProgramFile string) {
	cmd := exec.Command(javaRuntimePath, "-jar", mainProgramFile)
	cmd.Dir = workDir
	//err := cmd.Run()
	_, err := cmd.CombinedOutput()

	if err != nil {
		fmt.Print("Run program finish with error.")
		return
	}

	fmt.Print("Run program finish.")

}

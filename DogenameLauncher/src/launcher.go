package main

import (
	"fmt"
	"os"
	"os/exec"

	"launcher/checkers"
	"launcher/config"
	"launcher/utils"

	"github.com/sqweek/dialog"
)

var workDir string

func main() {

	var err error = nil
	workDir, err = utils.GetDir(os.Args[0])

	if err != nil {
		os.Exit(1)
	}

	var launcherConfig config.LauncherConfig

	launcherConfig = config.GetEffectiveConfig()

	dogenameDir, _ := utils.GetDir(launcherConfig.DogenameLocation)

	// solve existent dogename process
	pidFilesExist, pids := utils.FindDogeProcess(dogenameDir)
	if pidFilesExist == true {
		killDogeProcess := dialog.Message("发现可能有至少一个dogename在运行，要把它关掉吗？").Title("DogenameLauncher - 诶嘿！").YesNo()
		if killDogeProcess == false {
			os.Exit(0)
		} else {
			for _, pid := range pids {
				fmt.Println("killing:", pid)
				exec.Command("taskkill", "/PID", pid).Start()
				os.Remove(workDir + "/process/" + pid)
			}
		}
	}

	// check necessary files

	// check main program
	programExists, programLocation := checkers.SearchDoge(launcherConfig.DogenameLocation)
	fmt.Println("programLocation: ", programLocation)
	fmt.Println("programExists: ", programExists)
	if programExists != true {

		dialog.Message("主程序文件“dogename.jar”不见惹，没办法运行啦。\n请检查主程序文件是否存在或访问，若不存在请前往https://github.com/lensferno/dogename下载主程序文件，并放置在" + dogenameDir + "下。").Title("DogenameLauncher - 诶嘿！").Error()

		exec.Command("rundll32", "url.dll,FileProtocolHandler", "https://github.com/lensferno/dogename").Start()

		os.Exit(1)
	}

	// check runtime
	runtimeExists, runtimeLocation := checkers.SearchRuntime(launcherConfig.RuntimeLocation)

	fmt.Println("RuntimeLocation: ", runtimeLocation)
	fmt.Println("RuntimeExists: ", runtimeExists)
	if runtimeExists != true {
		dialog.Message("Java运行环境不见惹，没法运行啦！请确认是否安装Java。\n若您知道哪里有正确的Java，请编辑" + workDir + "下的“launcher.config”文件：）").Title("DogenameLauncher - 诶嘿！").Error()
		os.Exit(1)
	}

	// try to run program
	cmd := exec.Command(runtimeLocation, "-jar", programLocation)
	cmd.Dir = dogenameDir

	runError := cmd.Run()

	fmt.Println("runError:", runError)

	if runError != nil {
		dialog.Message("程序已经找到并且试图运行，但好像出了点错误，他托我告诉你一下（嗯？）\n大概是这样的：" + runError.Error() + "\n不过我把具体信息给忘了（懒得说了）（嗯？）\n这边建议您去https://github.com/lensferno/dogename那里重新下载一个呢：）").Title("DogenameLauncher - 诶嘿！").Info()
	}

}

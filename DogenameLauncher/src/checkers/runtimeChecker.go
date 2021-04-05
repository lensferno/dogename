package checkers

import (
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
)

func SearchRuntime(runtimeLocation string) (bool, string) {

	var err error
	runtimeLocation, err = filepath.Abs(runtimeLocation)

	if err != nil {
		return false, ""
	}

	//try to find javaw.exe
	javawExists, javawLocation := FindJava(runtimeLocation, "javaw")

	if javawExists == true {
		fmt.Println("[RuntimeChecker]javawExists:", javawExists)
		fmt.Println("[RuntimeChecker]javawLocation:", javawLocation)
		return true, javawLocation
	}

	//try to find java.exe
	javaExists, javaLocation := FindJava(runtimeLocation, "java")

	if javaExists == true {
		fmt.Println("[RuntimeChecker]javaExists:", javaExists)
		fmt.Println("[RuntimeChecker]javaLocation:", javaLocation)
		return true, javaLocation
	} else {
		return false, ""
	}
}

func FindJava(runtimeLocation string, runtimeName string) (bool, string) {
	fmt.Println("[RuntimeChecker]Finding:", runtimeLocation+"/"+runtimeName+".exe")
	_, err := os.Stat(runtimeLocation + "/" + runtimeName + ".exe")

	if err == nil {
		return true, runtimeLocation + "/" + runtimeName
	}

	installedJavaExists, installedJavaLocation := FindInstalledRuntime(runtimeName)

	if installedJavaExists == true {
		return true, installedJavaLocation
	} else {
		return false, ""
	}
}

func FindInstalledRuntime(program string) (bool, string) {
	findResult, err := exec.LookPath(program)

	if err == nil {
		return true, findResult
	} else {
		return false, ""
	}
}

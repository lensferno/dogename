package config

import (
	"fmt"
	"os"

	"gopkg.in/ini.v1"
)

const DEFAULT_MAIN_PROGRAM_LOCATION = "./dogename_program.jar"
const DEFAULT_RUNTIME_LOCATION = "./runtime/java8/bin"

func GetEffectiveConfig() LauncherConfig {

	launcherConfig := &LauncherConfig{ConfigVersion: 1, DogenameLocation: DEFAULT_MAIN_PROGRAM_LOCATION, RuntimeLocation: DEFAULT_RUNTIME_LOCATION}

	_, err := os.Stat("./launcher.conf")

	if err != nil {
		fmt.Println("[ConfigLoader]Config file doesn't exist,use default value.")
		return *launcherConfig
	}

	config, _ := ini.Load("./launcher.conf")
	config.MapTo(launcherConfig)

	if launcherConfig.DogenameLocation == "<@DEFAULT>" {
		fmt.Println("[ConfigLoader]Use default program location.")
		launcherConfig.DogenameLocation = DEFAULT_MAIN_PROGRAM_LOCATION
	}

	if launcherConfig.RuntimeLocation == "<@DEFAULT>" {
		fmt.Println("[ConfigLoader]Use default runtime location.")
		launcherConfig.RuntimeLocation = DEFAULT_RUNTIME_LOCATION
	}

	return *launcherConfig
}

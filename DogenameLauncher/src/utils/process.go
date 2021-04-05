package utils

import (
	"fmt"
	"path/filepath"
)

func FindDogeProcess(dogePath string) (bool, []string) {
	pids, _ := filepath.Glob(dogePath + "/process/*")

	if pids == nil {
		return false, nil
	} else {
		for i := 0; i < len(pids); i++ {
			pids[i] = filepath.Base(pids[i])
		}
		fmt.Println("[Process]found pids:", pids)
		return true, pids
	}

}

package utils

import (
	"path/filepath"
)

func GetDir(filePath string) (string, error) {
	absResult, err := filepath.Abs(filepath.Dir(filePath))
	if err != nil {
		return "", err
	} else {
		return filepath.ToSlash(absResult), err
	}

}

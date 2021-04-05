package checkers

import (
	"os"
	"path/filepath"
)

func SearchDoge(dogeLocation string) (bool, string) {

	var err error
	dogeLocation, err = filepath.Abs(dogeLocation)

	if err != nil {
		return false, ""
	}

	_, err1 := os.Stat(dogeLocation)

	if err1 != nil {
		return false, ""
	} else {
		return true, dogeLocation
	}

}

set GOARCH=386
go build -o dogename_win32_hide.exe -ldflags "-H windowsgui"
set GOARCH=amd64
go build -o dogename_win64_hide.exe -ldflags "-H windowsgui"
pause

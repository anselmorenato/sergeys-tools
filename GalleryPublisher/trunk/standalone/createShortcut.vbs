'
'	borrowed at http://stackoverflow.com/questions/346107/creating-a-shortcut-for-a-exe-from-a-batch-file
'
' usage:
' cscript createLink.vbs "C:\Documents and Settings\%USERNAME%\Desktop\Program1 shortcut.lnk" "c:\program Files\App1\program1.exe"
' cscript createLink.vbs "C:\Documents and Settings\%USERNAME%\Start Menu\Programs\Program1 shortcut.lnk" "c:\program Files\App1\program1.exe"

set objWSHShell = CreateObject("WScript.Shell")
set objFso = CreateObject("Scripting.FileSystemObject")

If wscript.arguments.count < 4 then
  WScript.Echo "usage: createShortcut.vbs shortcutPath targetPath"
  WScript.Quit
end If

' command line arguments
' TODO: error checking
sShortcut = objWSHShell.ExpandEnvironmentStrings(WScript.Arguments.Item(0))
sTargetPath = objWSHShell.ExpandEnvironmentStrings(WScript.Arguments.Item(1))
sWorkingDirectory = objFso.GetAbsolutePathName(sShortcut)

set objSC = objWSHShell.CreateShortcut(sShortcut)

objSC.TargetPath = sTargetPath
objSC.WorkingDirectory = sWorkingDirectory

objSC.Save
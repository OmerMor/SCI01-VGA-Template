echo off
cls
if "%1" == "" goto usage
if not exist %1\nul goto noLocate
echo If the error "Unable to create directory" appears
echo onscreen, Please disregard message. Thank-you.
echo �
md %1\sierra
md %1\sierra\QG2demo
echo �
echo Copying and Extracting files, please wait ...
copy demo.exe %1\sierra\QG2demo
copy QG2demo.bat %1\sierra
copy exists.com %1\sierra
%1
cd \sierra\QG2demo
demo
cd..
cls
echo �
echo Finished Copying and extracting.
echo �
echo To play the Quest for Glory II demo,
echo     type: QG2demo
echo �
echo If you have not run the install program.
echo Typing "QG2demo" will automatically run the install.
echo then play the demo.
echo �
echo NOTE: The last step of the install, when it states:
echo �
echo           ������(ESC or C through Z)����ͼ
echo �
echo       press the ESC key.
goto end
:noLocate
echo ERROR: Illegal Drive %1
:usage
type read.me
:end


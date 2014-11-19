@echo off
:recompile
cls
javac *.java && java Game
set /P restart=Recompile Game? (yes) 
If /I "%restart%"=="yes" goto recompile
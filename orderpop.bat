@echo off


set DIRNAME=%~dp0

set CP="%DIRNAME%unicenta_remotedisplay.jar"

start /B javaw -cp %CP% com.unicenta.orderpop.OrderPop
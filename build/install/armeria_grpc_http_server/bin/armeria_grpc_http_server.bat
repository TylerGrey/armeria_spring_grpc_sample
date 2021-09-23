@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  armeria_grpc_http_server startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and ARMERIA_GRPC_HTTP_SERVER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\armeria_grpc_http_server.jar;%APP_HOME%\lib\armeria-grpc-1.11.0.jar;%APP_HOME%\lib\armeria-grpc-protocol-1.11.0.jar;%APP_HOME%\lib\armeria-1.11.0.jar;%APP_HOME%\lib\grpc-services-1.40.1.jar;%APP_HOME%\lib\grpc-protobuf-1.40.1.jar;%APP_HOME%\lib\grpc-stub-1.40.1.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.40.1.jar;%APP_HOME%\lib\grpc-core-1.40.1.jar;%APP_HOME%\lib\grpc-api-1.40.1.jar;%APP_HOME%\lib\protobuf-java-util-3.17.2.jar;%APP_HOME%\lib\guava-30.1-android.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\reactor-core-3.4.7.jar;%APP_HOME%\lib\slf4j-simple-1.7.31.jar;%APP_HOME%\lib\slf4j-api-1.7.32.jar;%APP_HOME%\lib\jackson-annotations-2.12.4.jar;%APP_HOME%\lib\jackson-databind-2.12.4.jar;%APP_HOME%\lib\protobuf-jackson-1.2.0.jar;%APP_HOME%\lib\jackson-core-2.12.4.jar;%APP_HOME%\lib\micrometer-core-1.7.0.jar;%APP_HOME%\lib\netty-codec-http2-4.1.65.Final.jar;%APP_HOME%\lib\netty-codec-haproxy-4.1.65.Final.jar;%APP_HOME%\lib\netty-resolver-dns-native-macos-4.1.65.Final-osx-x86_64.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.65.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.65.Final-linux-x86_64.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.65.Final-linux-x86_64.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.65.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.65.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.65.Final.jar;%APP_HOME%\lib\netty-handler-4.1.65.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.65.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.65.Final.jar;%APP_HOME%\lib\netty-codec-4.1.65.Final.jar;%APP_HOME%\lib\netty-transport-4.1.65.Final.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\netty-tcnative-boringssl-static-2.0.40.Final.jar;%APP_HOME%\lib\brotli4j-1.6.0.jar;%APP_HOME%\lib\proto-google-common-protos-2.0.1.jar;%APP_HOME%\lib\protobuf-java-3.17.2.jar;%APP_HOME%\lib\HdrHistogram-2.1.12.jar;%APP_HOME%\lib\LatencyUtils-2.0.3.jar;%APP_HOME%\lib\netty-buffer-4.1.65.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.65.Final.jar;%APP_HOME%\lib\netty-common-4.1.65.Final.jar;%APP_HOME%\lib\gson-2.8.6.jar;%APP_HOME%\lib\annotations-4.1.1.4.jar;%APP_HOME%\lib\perfmark-api-0.23.0.jar;%APP_HOME%\lib\error_prone_annotations-2.4.0.jar;%APP_HOME%\lib\jakarta.annotation-api-1.3.5.jar;%APP_HOME%\lib\byte-buddy-1.10.19.jar;%APP_HOME%\lib\grpc-context-1.40.1.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\checker-compat-qual-2.5.5.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar


@rem Execute armeria_grpc_http_server
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %ARMERIA_GRPC_HTTP_SERVER_OPTS%  -classpath "%CLASSPATH%" example.armeria.server.blog.Main %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable ARMERIA_GRPC_HTTP_SERVER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%ARMERIA_GRPC_HTTP_SERVER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega

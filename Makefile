JAVAC ?= javac
JAVA  ?= java

SOURCES := $(wildcard *.java)
CLASSES := $(SOURCES:.java=.class)

# 默认目标：编译所有 .java
all: $(CLASSES)

%.class: %.java
	$(JAVAC) $<

# 可选：运行服务端与客户端（本地测试用）
run-server:
	$(JAVA) CalculatorServer 1099 Calculator

run-client:
	$(JAVA) CalculatorClient 127.0.0.1 1099 Calculator

clean:
	rm -f *.class
.PHONY: all run-server run-client clean

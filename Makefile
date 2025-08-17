JAVAC ?= javac
JAVA  ?= java

SOURCES := $(wildcard *.java)
CLASSES := $(SOURCES:.java=.class)

# Default target: compile all .java files
all: $(CLASSES)

%.class: %.java
	$(JAVAC) $<

# Optional: run server and client (for local testing)
run-server:
	$(JAVA) CalculatorServer 1099 Calculator

run-client:
	$(JAVA) CalculatorClient 127.0.0.1 1099 Calculator

clean:
	rm -f *.class
.PHONY: all run-server run-client clean

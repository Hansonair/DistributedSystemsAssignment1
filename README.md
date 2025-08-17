# Assignment 1 — Java RMI Calculator

## Build
javac *.java

## Run (Shared stack — default, required)
# Terminal A
java CalculatorServer 1099 Calculator

# Terminal B (single client demo)
java CalculatorClient 127.0.0.1 1099 Calculator

## Multi-client (Shared stack, 2-phase safe test)
javac MultiClientTest.java
java MultiClientTest 127.0.0.1 1099 Calculator

---

## Bonus: Per-client stacks (via factory sessions)
# Start server (factory is bound as "CalculatorFactory")
java CalculatorServer 1099 Calculator

# Run per-client concurrent test (each has its own stack)
javac MultiClientPerClient.java
java MultiClientPerClient 127.0.0.1 1099

---

## Remote methods
- pushValue(int)
- pushOperation(String)  // "min" | "max" | "gcd" | "lcm"
- pop()
- isEmpty()
- delayPop(int millis)

## Notes
- Server methods are synchronized to ensure thread safety.
- In shared-stack mode, `pushOperation` reduces **all current values** on the stack.
- Per-client mode is implemented using an RMI factory that issues dedicated `Calculator` session objects.

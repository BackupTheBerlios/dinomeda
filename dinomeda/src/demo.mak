all:
	$(JAVAC) $(JCFLAGS) src/demo/*.java

clean:
	rm -f src/demo/*~


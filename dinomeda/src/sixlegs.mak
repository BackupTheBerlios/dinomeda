all:
	$(JAVAC) $(JCFLAGS) src/com/sixlegs/image/png/*.java

clean:
	rm -f src/com/sixlegs/image/png/*~


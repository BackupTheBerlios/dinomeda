all:
	$(JAVAC) $(JCFLAGS) src/$(PACKAGEBASE)/dinomeda/*.java

clean:
	rm -f src/$(PACKAGEBASE)/dinomeda/*~


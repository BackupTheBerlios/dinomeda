all:
	$(JAVAC) $(JCFLAGS) src/$(PACKAGEBASE)/*.java

clean:
	rm -f src/$(PACKAGEBASE)/*~


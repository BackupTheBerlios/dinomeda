export JAVAC=javac
export JCFLAGS=-sourcepath src -d classes
export PACKAGEBASE=org/dinopolis/utils/metadata

all: classdir sixlegs base dinomeda demo

classdir:
	test -e classes || mkdir classes

sixlegs:
	$(MAKE) -f src/sixlegs.mak

base:
	$(MAKE) -f src/base.mak

dinomeda:
	$(MAKE) -f src/dinomeda.mak

demo:
	$(MAKE) -f src/demo.mak

clean:
	$(MAKE) -f src/base.mak clean
	$(MAKE) -f src/dinomeda.mak clean
	$(MAKE) -f src/demo.mak clean
	rm -rf classes
	mkdir classes

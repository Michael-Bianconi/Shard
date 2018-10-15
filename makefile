# compiler
JDE := javac

# interpreter
JVM := java

# documentation generator
JAVADOC := javadoc -d docs -subpackages shard -html5

# path to file with main()
ENTRY := shard/test/MemoryTest.java

# executable
EXE := shard.test.MemoryTest

# remove all class files
CLEAN_COMMAND := find . -name "*.class" -type f -delete

all:
	$(MAKE) clean --no-print-directory
	$(MAKE) compile --no-print-directory
	$(MAKE) run --no-print-directory

compile:
	${JDE} ${ENTRY}

run:
	${JVM} ${EXE}

clean:
	${CLEAN_COMMAND}

document:
	${JAVADOC}
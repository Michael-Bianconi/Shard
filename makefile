# compiler
JDE := javac

# interpreter
JVM := java

# extension
EXT := .java

# documentation generator
JAVADOC := javadoc -d docs -subpackages shard -html5

# test jde path
ENTRY := shard/test/

# test jvm path
EXE := shard.test.

TEST := TEST_NOT_SPECIFIED

# remove all class files
CLEAN_COMMAND := find . -name "*.class" -type f -delete

all:
	$(MAKE) clean --no-print-directory
	$(MAKE) compile --no-print-directory
	$(MAKE) run --no-print-directory

compile:
	${JDE} ${ENTRY}${TEST}${EXT}

run:
	${JVM} ${EXE}${TEST}

clean:
	${CLEAN_COMMAND}

document:
	${JAVADOC}
# compiler
JDE := javac

# interpreter
JVM := java

# extension
EXT := .java

# documentation generator
JAVADOC := javadoc -d docs -subpackages shard -html5

# executable
ENTRY := Shard

# remove all class files
CLEAN_COMMAND := find . -name "*.class" -type f -delete

all:
	$(MAKE) clean --no-print-directory
	$(MAKE) compile --no-print-directory
	$(MAKE) run --no-print-directory

compile:
	${JDE} ${ENTRY}${EXT}

run:
	${JVM} ${ENTRY}

clean:
	${CLEAN_COMMAND}

document:
	${JAVADOC}
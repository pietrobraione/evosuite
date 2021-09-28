
# Build Evosuite-sushi integration in Eclipse

- clone the repository from https://github.com/gdenaro73/evosuite.git
- init git submodules by executing
	git submodule init
	git submodule update
- compile submodules with gradle by executing
	cd sushi-runtime
- import evosuite as "existing maven project" in Eclipse	
- [if needed] import sushi-runtime as "existing Gradle project" in Eclipse
	

- build the shaded jar by executing Maven Build directly in Eclipse with goals: 
	package -DskipTests
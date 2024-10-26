ActivityReader CLI
==================

ActivityReader is a command-line application for interacting with GitHub activity.

For more details, you can refer to the project roadmap [here](https://roadmap.sh/projects/github-user-activity).

Requirements
------------
To run this project, you need:
- Java 17
- Maven 3.9

How to Compile and Run the Project
----------------------------------

Step 1: Compile the Project
---------------------------
To compile the project, navigate to the root directory of the project (where `pom.xml` is located) and run:
```sh
mvn compile
```

Step 2: Build the Project
-------------------------
To build the project and package it, run the following command:
```sh
mvn clean install
```

Step 3: Run the Project
-----------------------
You can run the application using either the `java` command, Maven, or an IDE configuration.

Option 1: Using `java` Command
------------------------------
After compiling, use the following command to run the application:
```sh
java -cp "target/classes;target/dependency/*" com.github.cli.Main {USER}
```

Option 2: Using Maven Command
-----------------------------
You can also use Maven to run the main class with arguments:
```sh
mvn exec:java -Dexec.mainClass="com.github.cli.Main" -Dexec.args="{USER}"
```

Option 3: Run with IDE Configuration
------------------------------------
If you are using an IDE like IntelliJ IDEA, you can create a run configuration as follows:
- Set the main class to `com.github.cli.Main`.
- Set the working directory to the root directory of your project.
- Add the required argument (`{USER}` in this case) in the program arguments field.
- Run the configuration to execute the project.

Replace `{USER}` with the desired argument for your use case.


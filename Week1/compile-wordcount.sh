javac -cp $(hadoop classpath) $1.java
jar cf $1.jar *.class

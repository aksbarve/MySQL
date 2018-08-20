

Transfile consist of all transaction code and following is the description of each transcation code

1 'eid' : Deletes the employee having 'eid' and return the number of row deleted.
2 'eid' 'name' 'salary' 'mid' : It inserts a new row with eid name salary and mid. Mid is optional and it returns done on success and error on failure.
3 	: Returns the average salary of all employees.
4 'mid' : Returns name of all employees that work under manager directly or indirectly with the given 'mid'.
5 'mid' : Returns average salary of employees that work under manager having the given 'mid'.
6 'mid' : Returns employees who has more than one manager having 'mid'. If there exists no such employee it gives "no employees with more than one manager".

config.properties file includes all the information needed to make a JDBC connection like dbusername, dbport, dbpassword, dbserver and dbname.

Open the source code files in eclipse or similar IDE. Go to the project directory and build path by right-click main java directory. In build path , select configure buildpath.
In buildpath, select Add jar files and select the "mysql-connector-java-5.1.29-bin" from lib directory.
Once done, the program is ready to run and will give the output in console corresponding to given transfile.
Make sure you run as Java application.
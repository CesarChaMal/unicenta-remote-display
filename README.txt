14th January 2017 
Release: Beta1
=====================================================================

uniCenta Remote Display Beta comprises 3 JavaFX app's
Configuration; the main app; and stand-alone Order List pop-up 

**** IMPORTANT ****
No installer yet
Tested only on Windows 10
MySQL only supported at this stage
App' uses Hibernate framework
Automatically creates the required Orders table in the assigned MySQL database schema

NOT integrated within uniCenta oPOS app' at this time except its database schema
Can be run completely independent of uniCenta oPOS
Possible use with other app's that support MySQL and can pass data into Orders table

Source Code WILL NOT be available until production release
NO User Guide available
Support ONLY via online Forum

DO NOT use in production environment!

REQUIREMENTS
Windows XP minimum
MySQL 5.6 minimum
Java JRE-8u111 minimum

INSTALLATION
Unzip to your chosen folder then run:

	CONFIGURE
	Set environment settings & properties
	Run configure.bat

	REMOTE DISPLAY
	Main app'
	Run start.bat

	ORDER POP-UP
	Stand-alone Order List app'
	Run orderpop.bat

SAMPLE DATA
To populate with sample data use MySQL-createSampleData.sql script
Change 'unicentaopos' database reference where required

Enjoy!

Jack
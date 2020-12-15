# SNMP Scanner
https://github.com/1TTO/SNMP-Scanner

![User Interface](UI.png)

## Installing:
Open your project in Intellij and run the command: maven package
Make sure that in the folder of the artifact you put a folder named data with a mib.csv and oid.csv where
you insert your mibs and oids.
Execute your artifact in the command prompt with java -jar (Artifactname).
Make sure to use java 8 and having included it in the path-variables.

## Functionalities:
- Scan network with netmask
- Scan network range
- Scan single address
- Customize MIBs
- Customize OIDs
- Change Public/Private
- Change Get/GetNext

## ToDo:
- Traps/Informs
- Finish UI
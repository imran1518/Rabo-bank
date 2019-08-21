Steps to run the application:
1.	Clone the project Rabo-bank (spring boot project).
     git clone https://github.com/imran1518/Rabo-bank
2.	Import the project in STS and do maven ->update project
3.	Check csv file and xml is present inside resource folder
4.	Start the application by restarting embedded server.
5.	service url to test application: http://localhost:8050/v1/rabo/failed/transaction
6.	Hit the above URL in browser or Postman/SoapUi tool.
7.	The input file will be validated based on two condition mentioned in the problem statment.(validation condition mentioned in expected output section)
      o	Duplicate Reference number check,
      o	End balance calculation check. (end balance = start balance +(or) – mutation)
8.	Finally invalid records will be getting as web service response.

Logic:
1.	Only CSV and Xml files can be used. Both files will be read one by one. 
2.	If any one of the file is missing in the resource folder then exception will be thrown as Error response.
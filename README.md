# IpTracker Service - ObservePoint Coding Challenge

This repository contains the code for the IpTracker Service, which was created as part of the ObservePoint Coding Challenge. The challenge involved building a service that could track the number of times an IP address was requested.

I have solved this challenge and deployed the service as an AWS Lambda function. The source code for the service is available in this repository. The repository also contains a test class that includes some end-to-end and load tests.

### Testing the Service

The test class, [IpTrackerTest](https://github.com/cararax/IpTrackerService/blob/master/src/test/java/com/carara/IpTrackerTest.java), is a JUnit test class used to test the functionality of the IpTracker service. The class contains three test methods:

`testRequestHandleWithGeneratedIPs()`: Tests the request-handled endpoint by sending HTTP POST requests with generated IP addresses and verifying that the responses have a status code of 200.

`testTop100WithGeneratedIPs()`: Tests the top100 endpoint by sending an HTTP GET request and verifying that the response contains a JSON array with a size of 99.

`testClear()`: Tests the clear endpoint by sending an HTTP GET request and verifying that the response has a status code of 200. The method then sends another HTTP GET request to the top100 endpoint to verify that the response is either null or an empty JSON array.

To test the service, a Postman collection is available [here](https://www.postman.com/avionics-participant-80841886/workspace/iptracker-opservepoint-challenge/request/27166362-3d3decfe-111a-494c-a139-4353812618b1).

Please note that a test with 20 million IP addresses was not performed due to cost considerations.
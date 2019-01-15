Installations: 
 - JDK8
 - Scala 2.11.8
 - Docker

Clone the project from github

cd project_dir/src/main/docker

RUN: 
    
    docker-compose up
This will spin up cassandra and kafka. Cassandra Keyspaces and Tables will be created by the init script specified in docker-compose


Load the project in IntelliJ

- Run src/main/scala/kafka/Producer.scala    
- This will put randomly generated messages in Kafka Topic called "leads"


- Run src/main/scala/spark/Streaming.scala
- This will consume messages from Kafka topic "leads" and populate 3 cassandra tables:

  lead_count -> Hourly and Daily Count of leads 
  lead_loc_count -> Hourly and Daily Count of leads per location
  master -> This is the master table with all the messages written as is 
 
- Run src/main/scala/api/Main.scala
- This will start a http server on port 9001 with the following api's exposed:
   
   http://localhost:9001/lastHourCount : Number of leads in last hour

   http://localhost:9001/dayCount : Number of leads in the current day
   
   http://localhost:9001/topLocationsHour : Top 10 Locations for last hour with the count of leads 
   
   http://localhost:9001/topLocationsDay : Top 10 Locations for current day with the count of leads 
   
   http://localhost:9001/getAll : All the above results combined
   
   
   
   
  



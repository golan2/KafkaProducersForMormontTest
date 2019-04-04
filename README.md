# KafkaProducersForMormontTest

## Overview
This is a java application that will populate messages to Kafka.
It allows you to simulate situation when devices send data to Kafka.
You define the time period and how many objects; you can also control how many messages for each object during that time period (hence TPS).  
See below the JobFile for more info.

## Command Line Arguments:
Following arguments need to be provided when running the java program:

 - `bootstrap.servers`    (for Kafka)
 - `topic`                (for Kafka)
 - `job.file`             (full path to the job file; details below)
 - `organization`
 - `project`
 - `environment`
 - `envUuid`
 
The `org/proj/env/envUuid` are the context; each time we run this tool we inject data to a single ORG/PROJ/ENV.

## Job File:
A job file contains a list of bulks.
Each bulk is about a set of objects that report in a certain time period.
A single bulk may look as follows:
```
    {
      "from": "2018-01-01 16:00:00",
      "to": "2018-01-01 17:00:00",
      "messages": 10,
      "devices": 12,
      "devicePrefix": "device_"
    }
```
In the example above we want 12 objects to report 10 messages in the hour between 4pm and 5pm.
Since we said we want 10 messages (per object) then eventually we will have a message every 6 minutes for each object.
The object id will be {`device_001`, `device_002`, ``, ...}

#### Multi Bulk
The job file may contain several bulks with overlapping time periods. 
Bulks will be processed by an ascending order of the `from` value (if same `from` then shorter first) 
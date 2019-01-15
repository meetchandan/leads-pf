CREATE KEYSPACE IF NOT EXISTS  leads WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE leads;

CREATE TYPE IF NOT EXISTS time_range (
           start timestamp,
           end timestamp
        );

CREATE TABLE IF NOT EXISTS lead_count
      (window frozen<time_range>, count bigint,
      PRIMARY KEY (window));


CREATE TABLE IF NOT EXISTS lead_loc_count
      (window frozen<time_range>, location text, count bigint,
      PRIMARY KEY (window, location));


CREATE TABLE IF NOT EXISTS master
        (name text, email text, location text, datetime timestamp,
        PRIMARY KEY (datetime, email));


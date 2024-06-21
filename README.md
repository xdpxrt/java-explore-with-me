# Explore With Me
It's a place where you can create events, share information about them and find event to go as well as leave comments.
Stack: Java, Spring (Boot, Data), WebClient, PostgreSQL, REST API, Docker, Postman, Lombok

<img width="1440" alt="68747470733a2f2f70696374757265732e73332e79616e6465782e6e65742f7265736f75726365732f5331395f30392d325f313637343535383734382e706e67" src="https://github.com/xdpxrt/java-explore-with-me/assets/106801220/699ca5c0-d47f-4c19-b2c6-152281cae3ef">

## About project: 

main-service (all buisness logic)
  - unauthorized users:
    - search for events
    - see details of event
    - see compilations of events
  - authorized users:
    - manage your events
    - submit a request for participation
    - leave comments
  - admins:
    - manage categories and compilations of events
    - moderate events
      
stats-service (to store statistics of events)
  -  collect information about the number of requests to the event

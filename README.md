I want to be able to use https://bikemapper.org in a way which lets me dowload the cycling directions in a fit file format by garmin.  This will allow me to put my phone away while I ride and use my cycling computer.

## Initial Approach
Initially I took the approach of trying to write my own fit file encoder that would take the geoJSON data from bikemapper's back end and convert that to a fit file.  This was a bit too complicated, and I was having problems doing it on the front end.

## New Approach
I have decided to take another approach, which is to write a small backend service that will 
1. Grab the route from the bikemapper backend
2. Convert that data to a fit course file via the garmin SDK
3. Return that data so that the file can be downloaded via the front end

## Tech stack
I am going to use the following
- Kotlin (https://kotlinlang.org/)
- Javalin (https://javalin.io/)
- Garmin SDK (https://developer.garmin.com/fit/download/)
- ktor-client (https://ktor.io/docs)

## General Data Flow
Basically What I'm doing is taking the exact query params that are passed to our backend from the web app and passing them to this fit file creator.
The only difference is we now can pass an additional `path` parameter that tells the fit file server creator which path to use when creating the .fit file.

Here is a silly graph
```
   ┌─────────┐         ┌─────────┐
   │         │   get*  │         │
   │ Browser ├────────►│ Fit Svr │
   │         │         │         │
   └─────────┘         └─┬───────┘
       ▲                 │
       │ .fit file       │ sans (path param)
       │                 ▼
       │       ┌─────────────────┐
       │       │                 │
       └───────┤    Bikehopper   │
               │                 │
               └─────────────────┘

    * Matches with params sent to bikehopper backend
```
## Using
To run this you must pull down this git repo and then make sure you have Java version `17.0.5-tem` installed.
Launch the app via intelli-j run or via cmd line `./gradlew run`

Once launched the app listens on `http://localhost:9001` via the `/fit/` route.

An example request is as such.

`localhost:9001/fit?locale=en-US&elevation=true&useMiles=false&layer=OpenStreetMap&profile=pt&optimize=true&pointsEncoded=false&pt.earliest_departure_time=2022-11-01T02:31:44.439Z&pt.connecting_profile=bike2&pt.arrive_by=false&details=cycleway&details=road_class&details=street_name&point=37.78306,-122.45867&point=37.78516,-122.46238&path=0`


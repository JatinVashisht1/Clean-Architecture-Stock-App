# Clean Architecture Stock App
- This is made by following youtube tutorial of [Philipp Lackner](https://www.youtube.com/c/philipplackner).
- Click [here](https://www.youtube.com/watch?v=uLs2FxFSWU4) to watch the tutorial.
- Click [here](https://www.alphavantage.co/) to know about API used in this app and get your own API key also.
- This app is build upon Clean Architecture guidelines.
- Dividing app on based on features and dividing each feature on based on different layers is a good option
    - this makes our app much more scalable

## Project Structure
- In clean architecture we divide our project into three layers, i.e, data, domain and presentation
### Data Layer
- This layer do all data related work
- for example retrofit, room, csv parsing, json parsing and all other things related to data, files, preferences belong to data layer
### Domain Layer
- This layer contains our business rules
- This is also the innermost layer in clean architecture
    - every other layer is allowed to access it but it is not allowed to access any other layer
- This layer defines some rules for example how we are going to filter specific entries,
    - which data we will provide to UI depending upon different situations
- Domain level models/entities are usually mapped from DTOs from data layer
- Entities and Models are also kept in domain layer
    - For example: a company info object that contains info about single company will belong to domain layer
- All other apps should be able to access **domain** layer but domain layer should not be able to access any other layers
### Presentation Layer
- This layer will contain code related to UI
- UI, states, screens, View Models all belong to presentation layer.

### Repository class
- we make interface of our repository in `domain layer` and implement it in the `data layer`
- It is because `viewmodel` should only be able to access classes from domain layer
- So we define interface of our repository in domain package and construct a concrete class in data layer
  - And then with `dependency injection` (hilt in our case) will construct the instance of our the `repository implementation class`
  - that instance is then provided to `viewmodel`
- Also keep in mind that `domain layer` is the `innermost` layer in the architecture and every other layer is allowed to access it

## How we will show data
- **Before** we even load from the **database** we want to tell viewmodel to show progress indicator
  - so we will emit a `loading resource` to viewmodel
- If we retrieve the data from `local cache` we will emit the `success resource` with the data in form of list of CompanyListingModel
  - And at the same time we will **request new data** from the api if we want to, for example to update our cache or to populate the cache first time
 - To represent these continuously changing states we use `Flow` from `Kotlin Coroutines` 


## Let's flow through the app!
### Project Structure
### Data Layer
### Domain Layer
#### Resource Class
### Presentation Layer
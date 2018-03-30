# tempelhof-tour

App to browse reviews about the tour to Tempelhof Airport in Berlin


======= Technical considerations

Native android app developed in java
Min SDK 21

Libraries:

Dagger 2: Creates, manages and inject data access classes in the application
Retrofit 2: Manages the communication with server
Jackson: Parse json from server
Room: To persist local data
Paging: To paginate data loading from local and remote
Butterknife: Bind views
Stetho: To debugging communication between app and server

======= Main classes

DataRepository: This class manage the other two (NetworkApi and LocalDatabase) and provide data to the application.

ReviewsViewModel: It's the responsible to communication between Data Repository and View layer

ReviewsActivity: It's responsible for shows the review list. It's a passive view and only react on the data changes.
AddReviewFragment: It's responsible collect data in a form to create a new review.

AppConfig: Some config constants as page size (for local and remote pagination), endpoints URLs and date formats.


======= Considerations

- The data access is paginated, locally and remotely, making possible the user navigate in a huge amount of data without stuck the ui.
- The data writing happen first locally and later remotely, making possible the user navigate and interact with his data without waiting the server response.
- The payload of the "save_review" endpoint would be a Review entity (the same we receive in the review list endpoint), but without review_id. And the response would be the same object with the review_id filled by the server.
- As we don't have endpoint to save reviews, the behavior is being the same of would happen in production if the server were down. The review shows in the review list ( with a delete option) and a toast is shown with a 404 message.

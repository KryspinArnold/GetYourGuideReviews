# GetYourGuideReviews
A sample Android project for browsing reviews of a GetYourGuide tour.

The app lets you load reviews from a specific tour and view them in a list (RecyclerView). The reviews can be filtered by Rating or Language and are cached in the SharedPreferences for offline use. 

New reviews are created using a mock Retrofit client and they don't affect live data.

# Tools and Libraries
- Android Studio 2.0
- Retrofit 1.9
- Dagger 2.2
- Gson
- JUnit

# Tests
There are some simple JUnit tests to check the string formatting of the list items.

# Limitations
- The language filter values are currently hard coded.
- New reviews are not added to live data.
- Only Title and Message can be added when creating new reviews.
- The specific tour is hard coded.


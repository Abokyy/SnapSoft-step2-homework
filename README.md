# SnapSoft Interview Step 2: Homework
## Created by Botond Varsányi

This is my solution for the second step of the interview. I spent ~5 hours on development and about 1 hour on research and investigating the API.

The application starts with the list of trending movies, showing their title and budget. The user can search for movies, once they hit search the results are listed also with the titles and budgets. The user can click on an item for more details about the movie.

On start a guest session is generated but the user has the option to login. Once the user provides the adequate credentials, the request token is validated and a new session is generated.

The first challenge I faced during development was that after I performed a query, in the API response there was no movie budget, only movie ID. So in order to retrieve the budget of each movie I had to get every detail for every movie one by one and then show the result to the user.

I also spent more time on authentication than expected, because I didn't really understand why is it necessary. I could show the requested movies with budgets without any login. So after the login button is clicked, the new request token and session ID is shown in a toast message (but nothing else) in order to demonstrate that the authentication is implemented.

# Summary

The application connects to TMDB and displays the response in a frequently used RecyclerView with the required image and budget (if the budget is known). The user can search for movies, and also click on a search result for more details about it. The application's minimum SDK version is 21 (Android 5.0) and was written in Kotlin with Gradle build tool.

The core of my solution was based on this article: https://dev.to/paulodhiambo/kotlin-and-retrofit-network-calls-2353

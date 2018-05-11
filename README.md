# UAND-2-Popular-Movies
**For the Udacity reviewer**: In order for the application to compile, please create a secrets.properties file in the app folder. The file needs to contain the following line

     API_KEY=<please insert the actual key here>
     
The build.gradle reads the API_KEY property from that file.
Link for reference on how to load API Keys and other secrets from properties file using gradle - https://gist.github.com/curioustechizen/9f7d745f9f5f51355bd6

# Project Overview
Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing. We will split the development of this app in two stages. First, let's talk about stage 1.

In this stage you’ll build the core experience of your movies app.

You app will:

* Present the user with a grid arrangement of movie posters upon launch.
* Allow your user to change sort order via a setting:
  * The sort order can be by most popular or by highest-rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
  * original title
  * movie poster image thumbnail
  * A plot synopsis (called overview in the api)
  * user rating (called vote_average in the api)
  * release date
  
# Why this Project?
To become an Android developer, you must know how to bring particular mobile experiences to life. Specifically, you need to know how to build clean and compelling user interfaces (UIs), fetch data from network services, and optimize the experience for various mobile devices. You will hone these fundamental skills in this project.

By building this app, you will demonstrate your understanding of the foundational elements of programming for Android. Your app will communicate with the Internet and provide a responsive and delightful user experience.

# What Will I Learn After Stage 1?
You will fetch data from the Internet with theMovieDB API.
You will use adapters and custom list layouts to populate list views.
You will incorporate libraries to simplify the amount of code you need to write.

# Required Tasks
* Build a UI layout for multiple Activities.
* Launch these Activities via Intent.
* Fetch data from themovieDB API

# Submission and Evaluation
Your project will be evaluated by a Udacity Code Reviewer according to [this rubric](https://review.udacity.com/#!/rubrics/66/view) . Be sure to review it thoroughly before you submit. All criteria must "meet specifications" in order to pass.

**Note:** Please make sure you **clean** your project before creating a zip file or pushing code to a GitHub repository. You can clean your project by following the instructions given in [this](https://d17h27t6h515a5.cloudfront.net/topher/2016/June/5769c116_1000-files-tutorial/1000-files-tutorial.pdf) link.

If you are using GitHub to host your projects, please make sure the code you want to submit for review is in the master branch of your repository.

**IMPORTANT:** If you're submitting via a public Github repository, please make sure any external API key that you utilize, has been removed from your code. It's highly unsafe (and often breaks the Terms of Service) to include API keys in public repos, so you need to remove yours. You can add a note in a README file where a reviewer should go to insert their API key. Reviewers have been trained to expect this situation.

**IMPORTANT: Make sure not to forget to move all the hardcoded Strings in your project to the strings.xml file. If you are unsure how to do this, see [this documentation](https://developer.android.com/distribute/best-practices/launch/localization-checklist#manage-strings).**

If you have any problems submitting your project, please email us at android-project@udacity.com. Due to the high volume of submissions, the turnaround for your project can take up to a week.

Each Android project will be reviewed against the [Common Project Requirements](http://udacity.github.io/android-nanodegree-guidelines/core.html), in addition to its project-specific rubric.

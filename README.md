#Android PopularMovie 
===================================

Popular Movies uses [themoviedb](https://www.themoviedb.org/) API to retrieve the information. This app let the user to filter between 
- Most Popular
- Highest Rated 
- Favorite Movies 

Favorite movies can be accessed even without internet as it will be stored in local Database. Responsive Design is accomplished. Depending on single plane or two pane, the UI changes to fit more information on the screen. 

##Libraries
-----------
- Picasso has been used to load images.

- Butterknife package to reduce lines of code.

- Retrofit to obtain json string easily.


##Run it on your machine
---------------
- **Clone the project**:
  Go to any directory in your machine and run the below command on the terminal:
  ```
  git clone https://github.com/abilaashsai/Android-PopularMovie.git
  ```
- **Get themoviedb API key**:
  - Go to [themoviedb](https://www.themoviedb.org/) and get the key. It's totally free.
  - In [build.gradle(Module:app)](https://github.com/abilaashsai/Android-PopularMovie/blob/145dec9ff1fe5cf4f8d7f818acc1e83db3c8ab44/app/build.gradle) replace `[YOUR_API_KEY_HERE]` with your _API_KEY_ on line 21.
   ```
   it.buildConfigField 'String', 'OPEN_MOVIE_DB_API_KEY', '"[YOUR_API_KEY_HERE]"'
   ```

- **Run the project**:
  I use Android Studio. Here you have to _Import_ the project and _Run_ on _mobile_ ot _tablet_.

- **Playing with Functionality**
  User can change the sort order between most popular/ highest rated/ favorite movies in 
  Settings -> General -> Sort Order 

##Contribution
-------
Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub.

##Screenshot
-------

One Pane

![Alt text](https://github.com/abilaashsai/Popular-Movies-2/blob/master/MainUI.png "Main UI")

Two Pane

![Alt text](https://github.com/abilaashsai/Popular-Movies-2/blob/master/TwoPane.png "Main UI")


License
-------
The contents of this repository are covered under the [MIT License](https://github.com/abilaashsai/Android-PopularMovie/blob/master/LICENSE).

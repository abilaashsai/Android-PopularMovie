Popular-Movies

Udacity Project 2: Popular Movies 2

Popular Movies app let the user to view the most popular/ highest rated/ favourite movies depending on the user's choice.


To run this application, provide themoviedb.org API in

buid.gradle(Module:app) -> 

buildTypes.each {
        it.buildConfigField 'String', 'OPEN_MOVIE_DB_API_KEY', '"[YOUR_API_KEY_HERE]"'
}    

User can change the sort order between most popular/ highest rated/ favourite movies in

Settings -> General -> Sort Order 

SCREENSHOTS

One Pane

![Alt text](https://github.com/abilaashsai/Popular-Movies-2/blob/master/app/src/main/res/drawable/MainUI.png "Main UI")

Two Pane

![Alt text](https://github.com/abilaashsai/Popular-Movies/blob/master/img_3.png "Main UI")



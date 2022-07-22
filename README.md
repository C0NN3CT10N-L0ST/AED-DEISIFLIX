# AED-DEISIFLIX

## Project Description
The goal of this project is to develop an application that can perform queries over a movie database where the focus is efficiency.
The movie database is composed of files in CSV format.

## Database Files (Input)
The program input is split into 4 distinct CSV files.
Each line in all of the 4 files contains data about a specific movie.
File Name | Description | Line Format
:---------: | ----------- | -----------
`deisi_movies.txt` | Contains basic movie data | `<Movie_ID>`,`<Title>`,`<Duration>`,`<Budget>`,`<Date>`
`deisi_movie_votes.txt` | Contains data related to movie votes according to the IMDB platform | `<Movie_ID>`,`<Average_Votes>`,`<Total_Votes>`
`deisi_people.txt` | Contains data about movie related people | `<Person_Type>`,`<Person_ID>`,`<Name>`,`<Gender>`,`<Movie_ID>`
`deisi_genres.txt` | Contains data about movie genres | `<Genre_Name>`,`<Movie_ID>`

### **Components:** `'deisi_movies.txt'`
Component | Type
 :---: | ---
`Movie_ID` | Non-negative Integer
`Title` | String
`Duration` | Real Number
`Budget` | Integer
`Date` | String with format 'DD-MM-YYYY'

### File format example
```
603,The Matrix,136,63000000,30-03-1999
10428,Hackers,107,20000000,14-09-1995
76341,Mad Max: Fury Road,150000000,120,13-05-2015
999999,ULHT Movie School,0,0,01-01-2001,0
770,Gone with the Wind, 238,4000000,15-12-1939
6978,Big Trouble in Little China,99, 25000000,30-05-1986
1995,Lara Croft: Tomb Raider, 100,115000000 ,11-06-2001
```

### **Components:** `'deisi_movie_votes.txt'`
Component | Type
 :---: | ---
`Movie_ID` | Non-negative Integer
`Average_Votes` | Real Number
`Total_Votes` | Non-negative Integer
### File format example
```
6978,7.1,727
603,7.9,9079
10428,6.2,406
770,7.7,995
76341,7.3,9629
1995,5.7,2235
```

### **Components:** `'deisi_people.txt'`
Component | Type
 :---: | ---
`Person_Type` | String ('ACTOR' or 'DIRECTOR')
`Person_ID` | Non-negative Integer
`Name` | String
`Gender` | Char ('M' - Male, 'F' - Female, '-' - N/A)
`Movie_ID` | Non-negative Integer
### File format example
```
ACTOR,11701,Angelina Jolie,F,1995
ACTOR,6384,Keanu Reeves,M,603
ACTOR,11701,Angelina Jolie,F,10428
ACTOR,6856,Kurt Russell,M,6978
ACTOR,2109,Kim Cattrall,F,6978
DIRECTOR,12786,Simon West,-,1995
DIRECTOR,9339,Lilly Wachowski,-,603
DIRECTOR,9340,Lana Wachowski,-,603
DIRECTOR,1978,Iain Softley,-,10428
DIRECTOR,96372,Tom Clegg,M,69787
```

### **Components:** `'deisi_genres.txt'`
Component | Type
 :---: | ---
`Genre_Name` | String
`Movie_ID` | Non-negative Integer
### File format example
```
Action,603
Science Fiction,603
Action,6978
Romance,770
Adventure,76341
Science Fiction,76341
Thriller,76341
Drama,10428
Crime,10428
Thriller,10428
Action,10428
Comedy,6978
```

### ***NOTE:***
If for each line of any of the files the number of components of that line does not correspond to the expected number,
then that line should be ignored.</br>
Additionally, each line may contain extra spaces between the components, these should be ignored (i.e. they should be
treated as if they did not exist).

## Database Queries
Below are all the queries that can be performed on the movie database.

Query | Description
:---: | ---
***COUNT_MOVIES_ACTOR*** `<Actor_Full_Name>` | Returns the number of movies an actor has participated in. If the actor does not exist `0` is returned.
***GET_MOVIES_ACTOR_YEAR*** `<Actor_Full_Name>` `<Year>` | Returns the movies in which a given actor took part in a certain year in descending order.</br>**Output format:** `<Movie_Title> <Date>`.</br>**Note:** Date format should be 'YYYY-MM-DD'.
***COUNT_MOVIES_WITH_ACTORS*** `<Actor1_Full_Name>`;`<Actor2_Full_Name>`;`...` | Returns the number of movies in which all the given actors took part in.
***COUNT_ACTORS_3_YEARS*** `<Year1>` `<Year2>` `<Year3>` | Returns the number of actors which participated in movies in all 3 given years.
***TOP_MOVIES_WITH_GENDER_BIAS*** `<Output_Amount> <Year>` | Returns the N (given number) movies from year Y (given year) with the greatest percentual discrepancy by descending order.</br>**Note:** Only movies with more than 10 actors/actresses should be taken into account.</br>**Output Format:** `<Movie_Title>;<Gender>;<Percentual_Discrepancy>`.
***GET_RECENT_TITLES_SAME_AVG_VOTES_ONE_SHARED_ACTOR*** `<Movie_ID>` | Returns the titles of the movies with the same average votes as the given one that were made after it and have at least 1 common actor.</br>**Output Format:** `<Movie1_Title>\|\|<Movie2_Title>\|\|<Movie3_Title>...`.
***GET_TOP_N_YEARS_BEST_AVG_VOTES*** `<Output_Amount>` | Returns the top N (given number) years with the best average votes (from all movies from each year).</br>**Output Format:** `<Year>:<Average_Votes>`.</br>**Note:** The output average votes should have no more than 2 decimal places.
***DISTANCE_BETWEEN_ACTORS*** `<Actor1_Full_Name>` `<Actor2_Full_Name>` | Returns the distance level between 2 actors. If the both actors took part in the same movie the level is `0`. If they do not have a shared movie but there is a third actor that has been part of movies with actor1 and actor2, the level should be `1`. In all other cases the return should be `:(`.
***GET_TOP_N_MOVIES_RATIO*** `<Output_Amount>` `<Year>` | Returns the top N movies with from the given year with the best ratio (Average Votes / Number of Actors).</br>**Output Format:** `<Title>:<Ratio>`. If there are no results the return value should be `zerop`.
***TOP_6_DIRECTORS_WITHIN_FAMILY*** `<Year1>` `<Year2>` | Returns 6 directors that directed the most movies with family members in the given year range. If there are no results, it should return `NEM UM`.</br>**Output Format:** `<Director_Name>:<Number_of_Family_Directions>`</br>**Note:** Are considered family directions whenever the last name of at least 2 directors from the same movie are equal.
***GET_TOP_ACTOR_YEAR*** `<Year>` | Returns the actor with most movie appearances for the given year.</br>**Output Format:** `<Actor_Name>:<Number_of_Movies>`.
***INSERT_ACTOR*** `<ID>`;`<Name>`;`<Gender>`;`<Movie_ID>` | Inserts an actor. The inserted actor should be taken into account in future queries.</br>Returns `OK` on ***success*** and `Erro` on ***error***.
***REMOVE_ACTOR*** `<Actor_ID>` | Removes an actor. The removed actor must no longer appear in future queries.</br>Returns `OK` on ***success*** and `Erro` on ***error***.
***GET_DUPLICATE_LINES_YEAR*** `<Year>` | Returns all the duplicate lines from `deisi_people.txt` file for movies from the given year. If there are no results for the query it must return an empty string.</br>**Output Format:** `<Line_Number>:<Person_ID>:<Movie_ID>`.</br>**Note:** Its considered a duplicate line every time the same actor ID appears linked to the same movie more than 1 time.
***TOP_10_MOST_EXPENSIVE_MOVIES_YEAR*** `<Year>` | Returns the top 10 most expensive movies from the given year.</br>**Output Format:** `<Movie_Title> - Budget: $<Movie_Budget>`.

## Program - UX
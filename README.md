# AED-DEISIFLIX

## Project Description
The goal of this project is to develop an application that can perform queries over a movie database where the focus is efficiency.
The movie database is composed of files in CSV format.

## Database Files (Input)
The program input is split into 4 distinct CSV files.
Each line in all of the 4 files contains data about a specific movie.
File Name | Description | Line Format
:---------: | ----------- | -----------
`deisi_movies.txt` | Contains basic movie data | `<Movie ID>`,`<Title>`,`<Duration>`,`<Budget>`,`<Date>`
`deisi_movie_votes.txt` | Contains data related to movie votes according to the IMDB platform | `<Movie ID>`,`<Average Votes>`,`<Total Votes>`
`deisi_people.txt` | Contains data about movie related people | `<Person Type>`,`<Person ID>`,`<Name>`,`<Gender>`,`<Movie ID>`
`deisi_genres.txt` | Contains data about movie genres | `<Genre Name>`,`<Movie ID>`

### **Components:** `'deisi_movies.txt'`
Component | Type
 :---: | ---
`Movie ID` | Non-negative Integer
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
`Movie ID` | Non-negative Integer
`Average Votes` | Real Number
`Total Votes` | Non-negative Integer
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
`Person Type` | String ('ACTOR' or 'DIRECTOR')
`Person ID` | Non-negative Integer
`Name` | String
`Gender` | Char ('M' - Male, 'F' - Female, '-' - N/A)
`Movie ID` | Non-negative Integer
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
`Genre Name` | String
`Movie ID` | Non-negative Integer
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

Query Code | Description | Format
:---: | --- | ---
`COUNT_MOVIES_ACTOR` | Returns the number of movies an actor has participated in. If the actor does not exist 0 is returned. | `COUNT_MOVIES_ACTOR <Full Actor Name\>`
`GET_MOVIES_ACTOR_YEAR` | Returns the 
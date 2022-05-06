# Roborally
Dette Roborally program er udviklet af gruppe 3 på kursus 02362 i forået 2022



## Opsætning af databasetilknytning
Før spillet kan benyttes er det vigtigt at få styr på databasetilknytningen først.
Dette gøres ved først at finde vores connector-klasse. Denne findes ude i højre side ved at vælge “src” -> “main” -> “java” -> “roborally” -> “dal” -> “connector”.

I denne klasse er det vigtigt at man indtaster sine egne informationer under “USERNAME”, “PASSWORD” og “DATABASE”. I nogle tilfælde er “PORT” heller ikke det samme nummer på alle computere, så tjek også denne.

Åbn nu MySQL Workbench og klik på det lille “+”, så der oprettes en ny connection.

Kald gerne denne “roborally connection”, så man ved hvad den bruges til.
Her kan man sikre sig at der under port står det samme tal som i connector-klassen. Dette er i de fleste tilfælde “3306”. Dertil skal man også sikre sig at man skriver det samme username og password, som man indsætter i connector-klassen.


Herefter vælges “test connection” og så skulle man gerne få beskeden “Successfully made the MySQL connection”.

Åbn nu den nye connection og tast “DROP DATABASE IF EXISTS” efterfulgt af dit databasenavn.
Dette sikre at man ikke allerede har en eksisterende database med det navn.
På linje 2 skrives “CREATE DATABASE” efterfulgt af det navn du vælger til databasen. Her skal man også sikre sig at man skriver dette navn inde i connector-klassen.


Tryk nu på det gule lyn ovenfor og tjek at der i bunden er et grønt flueben og at der under message står “ row(s) affected”.



## Start spillet
Nu burde de praktiske ting være på plads og spillet kan nu startes.
For at starte spillet findes StartRoborally- klassen. Denne er lokaliseret under “src” -> “main” -> “java” -> “roborally” -> “StartRoborally”.
Åbn denne klasse og tryk derefter på den lille grønne pil ud for linje 33.

Programmet er nu igangsat.
Vælg nu “file” og derefter har man muligheden for enten at starte et nyt spil “New Game” eller indlæse et tidligere spil “Load Game”.

Dernæst vælges hvor mange spillere der deltager, her kan det vælges i mellem 2 til 6 spillere.

Herefter kan man nu vælge mellem vores 2 boards.
Vi har et almindeligt board/default board som er beregnet til 2-6 spillere.
Dette er vores board med flest feltaktioner.

Derudover har vi vores RaceTrack som er beregnet kun til 2 spillere.
Dette skal fungere som en form for kapløbs bane hvor de 2 spillere dyster mod hinanden om at komme hurtigst rundt om svingene og få deres checkpoints.



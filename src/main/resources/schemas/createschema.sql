/* Need to switch off FK check for MySQL since there are crosswise FK references */
SET FOREIGN_KEY_CHECKS = 0;;

CREATE TABLE IF NOT EXISTS Game (
  gameID int NOT NULL UNIQUE AUTO_INCREMENT,

  name varchar(255),

  phase tinyint,
  step tinyint,
  currentPlayer int NULL,
  board varchar(255),
  PRIMARY KEY (gameID),
  FOREIGN KEY (gameID, currentPlayer) REFERENCES Player(gameID, playerID)
);;

CREATE TABLE IF NOT EXISTS Player (
  gameID int NOT NULL,
  playerID int NOT NULL,

  name varchar(255),
  colour varchar(31),

  positionX int,
  positionY int,
  heading tinyint,
  checkpoints int,

  PRIMARY KEY (gameID, playerID),
  FOREIGN KEY (gameID) REFERENCES Game(gameID)
);;

CREATE TABLE IF NOT EXISTS Card(
    game int,
    player int,
    id int,
    pos int,
    place int,
    PRIMARY KEY(game, player, id, pos, place),
    foreign key (game) references Game(gameID),
    foreign key (game, player) references Player(gameID, playerID)

);;
SET FOREIGN_KEY_CHECKS = 1;;

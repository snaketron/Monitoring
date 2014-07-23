CREATE TABLE "network_t" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "source" INTEGER NOT NULL,
    "parent" INTEGER NOT NULL,
    "connectivity" INTEGER NOT NULL,
    "hops" INTEGER NOT NULL,
    "connectedSince" INTEGER NOT NULL,
    "rssi" INTEGER NOT NULL, 
    "time" TEXT NOT NULL
)

CREATE TABLE "data_t" (
    "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    "origin" INTEGER NOT NULL,
    "humidity" REAL NOT NULL,
    "temperature" REAL NOT NULL,
    "battery" REAL NOT NULL,
    "light" REAL NOT NULL,
    "time" CHAR(50) NOT NULL
)
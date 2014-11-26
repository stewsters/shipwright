
# ShipWright

A library to generate spaceships.

[Example](http://imgur.com/8t5B2jM)

I was originally going to make a FTL clone, but when I sat down to draw a ship, I realized I wont have the time this 
week.  So I decided to make a tool to generate spaceships instead.


# Algorithm
The algorithm was based on David Bollinger's Pixel Spaceships.  My intention is to be able to scale it up to higher
resolution ships using open simplex noise for the random parts.  The hope is that I can generate procedural space ships
 that follow certain design choices, but are varied enough.

## Hull
First we start with blueprints that define areas that must have hull (in black) may have hull (in white) or do not have hull (transparent).

The optional hull is shaped by Open Simplex Noise to have a rounded appearance.  Parts that are not contiguous are trimmed off.


## Rooms
The internal rooms of the spaceship are generated within its superstructure by randomly placing them and pulling them
toward a center until the can no longer fit, then rewind one and stamp them on.

I then use A* pathfinding to cut corridors between the rooms like I did on my Salvage 2014 7drl.


## Paint

Color pallets are generated and we select between them with another offset on the simplex noise.
I then paint any internals that color on the final image.




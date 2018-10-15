# ShardObject

ShardObjects are the superclass of every physical object in Shard. That
includes Guests, Items, and even Rooms. Every ShardObject has a name, and
description, and location.

## Room

A subclass of ShardObject, Rooms are simple containers for other ShardObjects
have little or no function by themselves.

## Item

Items can be placed either in Rooms or on Guests (or the Player). Many are
simple flavor texts, but many more can be interacted with. For example,
tea may be drinken (drunken? drinked? droke?), or the murderer can use a 
Kitchen Knife to do murderery stuff.

## Guest

People within the house (not including the player). With the exception of the
murderer, these guys will mostly just mill around and do various things.
However, they'll be picking up tidbits of information that might help the
player. Watch out though, the murderer will lie about what he or she sees.
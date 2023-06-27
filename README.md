# `WarriorSplits 🏃`
An ingame speedrun timer for the MCC Island game; Parkour Warrior!

## How it works
Pretty simple stuff, it just detects when you start, reset, or finish an obstacle and changes an ingame timer accordingly.
![Timer](https://github.com/SimplyMerlin/WarriorSplits/assets/30577208/3b52e150-0b08-4f6e-bea7-98ab7e70ca14)

## Todo
* Make saving async
* Support months (need to figure out how to detect which month)
* Support daily (Need battle pass lol)
* Allow advanced/expert runs

## Tech stuff
* **InGameHudMixin**
  * Here the UI gets rendered
  * Titles and subtitles get hooked into to detect splits
  * Scoreboard gets hooked into to detect which course you're on (this needs to be fixed this sucks LOL)
* ChatHudMixin
  * chat gets hooked into to detect resets
* command: just a simple timer command to control the Timer
* **Course**: how we distinguish courses from each other, also saving and loading.
* SavableSegment: serializable segment for saving and for storage in course
* **Segment**: the fancy segment, simple stuff
* **Timer**: All the code logic between timing (mostly just Instants and Durations) Home of the Segment
* Utils: basic render logic for Duration, in line with parkour warrior
* WarriorLiterals: stuff i want to put in a config at some point
* WarriorSplits: Entrypoint

Future plans development wise are very much cleaning a lot of this up. I don't like the current state of some things, but I can't figure out a cleaner way to do some things for now.

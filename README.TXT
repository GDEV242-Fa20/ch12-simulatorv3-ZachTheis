Project: foxes-and-rabbits-v2
Authors: Michael Kölling and David J. Barnes

This project is part of the material for chapter 10 of the book

   Objects First with Java - A Practical Introduction using BlueJ
   Sixth edition
   David J. Barnes and Michael Kölling
   Pearson Education, 2016

A predator-prey simulation involving foxes and rabbits in
an enclosed rectangular field.

This is the first version of the simulation. This version
does not use inheritance.

How to start:
    Create a Simulator object.
    Then call one of:
        + simulateOneStep - for a single step.
        + simulate - and supply a number (say 10) for that many steps.
        + runLongSimulation - for a simulation of 500 steps.

This program has been edited by Zach Theis (2020.11.17) to include hawks

My initial assumptions were that hawks would exist in balance with the other animals.
They hunted both foxes and rabbits, had a lower breeding probability,
life span, litter size, and got less food value from hunting than
foxes. However, tweaking the numbers is incredibly difficult. It seems that
either the foxes completely take over the ecosystem, or die out.
However, if they die out, the ecosystem achieves balance between hawks and 
rabbits, seemingly indefinitely.

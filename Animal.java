import java.util.List;
import java.util.Random;
/**
 * A class representing shared characteristics of animals.
 * 
 * @author Zach Theis
 * @version 2020.11.17
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    private int age;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        age = 0;
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Indicate whether the animal is of breeding age.
     * @return Whether or not the animal can breed.
     */
    public boolean canBreed()
    {
        return age >= getBreedingAge();
    }
    
    /**
     * A method to get the breeding age that must be implemented by all subclasses.
     * @return The minimum breeding age of the animal.
     */
    abstract protected int getBreedingAge();
    
    /**
     * Gets the animals current age.
     * @return The int value of the animal's age.
     */
    protected int getAge()
    {
        return age;
    }
    
    /**
     * Set's a new age for the animal.
     * @param newAge The age to be set.
     */
    protected void setAge(int newAge)
    {
        age = newAge;
    }
    
     /**
     * Increase the age.
     * This could result in the animal's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > getMaxAge()) 
        {
            setDead();
        }
    }
    
    /**
     * Get's the animal's maximum age before it dies. This is an abstract
     * method which must be implemented by all subclasses
     * @return The animal's max age.
     */
    abstract protected int getMaxAge();
    
    /**
     * Generates the number of new animals created by an animal of breeding age.
     * This depends on the animal's breeding probability.
     * @return How many new animals are born.
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    /**
     * Creates the new animals as indicated by the breed method.
     * @param newAnimals An empty list that will store the newborn animals.
     */
    protected void giveBirth(List<Animal> newAnimals)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            newAnimals.add(createAnimal(false, field, loc));
        }
    }
    
    /**
     * An abstract method to create a newborn animal. This is implemented by each
     * subclass to create an animal of their type.
     * @param randomAge whether or not the new animal's age is randomized.
     * @param field The field in which the new animal is created.
     * @loc The specific location within the field of the new animal.
     * @return The newborn animal
     */
    abstract protected Animal createAnimal(boolean randomAge, Field field, Location loc);
    
    /**
     * An abstract method to get an animal's maximum number of offspring it can
     * create at one time. Implemented by each subclass.
     * @return The int value of the animal's max litter size.
     */
    abstract protected int getMaxLitterSize();
    
    /**
     * An abstract method to get the likelihood that an animal will produce offspring.
     * Implemented by each subclass
     * @return The percentage chance that the animal will breed.
     */
    abstract protected double getBreedingProbability();
}

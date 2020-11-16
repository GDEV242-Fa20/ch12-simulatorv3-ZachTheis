import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author Zach Theis
 * @version 2020.11.17
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.14;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control starting age.
    private static final Random rand = Randomizer.getRandom();
    

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) 
        {
            setAge(rand.nextInt(MAX_AGE));
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Animal> newRabbits)
    {
        incrementAge();
        if(isAlive()) 
        {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Creates a new rabbit. This is used by Animal to create newborn rabbits.
     * @return The newborn rabbit.
     */
    protected Animal createAnimal(boolean randomAge, Field field, Location loc)
    {
        return new Rabbit(randomAge, field, loc);
    }
        
    /**
     * Gets the minimum age at which a rabbit can breed.
     * @return The int value of the rabbit's min breeding age.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Get's the rabbit's max age.
     * @return The int value of the rabbit's max age
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Gets the rabbit's maximum number of offspring that can be created at a time.
     * @return The int value of the rabbit's max litter size.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Get's the likelihood that the rabbit will breed.
     * @return The percent value that the rabbit will produce offspring.
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
}

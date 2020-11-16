import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a Hawk.
 * Hawkes age, move, eat rabbits and foxes, and die. They have a low breeding 
 * probability, age range, and max litter size. Animals give them less food than
 * foxes, but they have a max value and add their prey's value to their current
 * food level.
 * 
 * @author Zach Theis
 * @version 2020.11.17
 */
public class Hawk extends Animal
{
    // Characteristics shared by all Hawkes (class variables).
    
    // The age at which a Hawk can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a Hawk can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a Hawk breeding.
    private static final double BREEDING_PROBABILITY = 0.041;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. 
    private static final int RABBIT_FOOD_VALUE = 6;
    // The food value of a single fox.
    private static final int FOX_FOOD_VALUE = 4;
    // The total amount of food a hawk can store. In effect, this is the
    // maximum number of steps a Hawk can go before it has to eat again.
    private static final int MAX_FOOD_VALUE = 12;
    // A shared random number generator to control starting age.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The Hawk's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a Hawk. A Hawk can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the Hawk will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hawk(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) 
        {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(MAX_FOOD_VALUE);
        }
        else 
        {
            foodLevel = MAX_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the Hawk does most of the time: it hunts for
     * rabbits and foxes. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHawkes A list to return newly born Hawkes.
     */
    public void act(List<Animal> newHawkes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) 
        {
            giveBirth(newHawkes);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) 
            { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) 
            {
                setLocation(newLocation);
            }
            else 
            {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age. This could result in the Hawk's death.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Make this Hawk more hungry. This could result in the Hawk's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits or foxes adjacent to the current location.
     * Only the first live animal is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) 
            {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel += RABBIT_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }
            }
            else if(animal instanceof Fox)
            {
                Fox fox = (Fox) animal;
                if(fox.isAlive())
                {
                    fox.setDead();
                    foodLevel += FOX_FOOD_VALUE;
                    if(foodLevel > MAX_FOOD_VALUE)
                    {
                        foodLevel = MAX_FOOD_VALUE;
                    }
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Creates a new hawk. This is used by Animal for the giveBirth method.
     * @return The newborn hawk.
     */
    protected Animal createAnimal(boolean randomAge, Field field, Location loc)
    {
        return new Hawk(false, super.getField(), super.getLocation());
    }
    
    /**
     * Gets the minimum age at which a hawk can breed.
     * @return The int value of the hawk's min breeding age.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Gets the hawk's maximum number of offspring that can be created at a time.
     * @return The int value of the hawk's max litter size.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Get's the likelihood that the hawk will breed.
     * @return The percent value that the hawk will produce offspring.
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
}

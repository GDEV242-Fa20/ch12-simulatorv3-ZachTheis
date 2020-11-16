import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author Zach Theis
 * @version 2020.11.16
 */
public class Fox extends Animal
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.087888;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 9;
    //The maximum amount of food a fox can hold. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int MAX_FOOD_VALUE = 17;
    // A shared random number generator to control starting age.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location)
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
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Animal> newFoxes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) 
        {
            giveBirth(newFoxes);            
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
     * Get's the fox's max age.
     * @return The int value of the fox's max age
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
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
            if(animal instanceof Rabbit) {
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
        }
        return null;
    }
    
    /**
     * Creates a new fox. This method is used by Animal to create newborn foxes.
     * @return The newborn Fox
     */
    protected Animal createAnimal(boolean randomAge, Field field, Location loc)
    {
        return new Fox(randomAge, field, loc);
    }
    
    /**
     * Gets the minimum age at which a fox can breed.
     * @return The int value of the fox's min breeding age.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Gets the fox's maximum number of offspring that can be created at a time.
     * @return The int value of the fox's max litter size.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Get's the likelihood that the fox will breed.
     * @return The percent value that the fox will produce offspring.
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
}

package nl.rug.aoop.asteroids.control;

import nl.rug.aoop.asteroids.database.DatabaseManager;
import nl.rug.aoop.asteroids.database.Score;
import nl.rug.aoop.asteroids.model.AsteroidSize;
import nl.rug.aoop.asteroids.model.Game;
import nl.rug.aoop.asteroids.model.gameobjects.Asteroid;
import nl.rug.aoop.asteroids.model.gameobjects.Bullet;
import nl.rug.aoop.asteroids.model.gameobjects.GameObject;
import nl.rug.aoop.asteroids.model.gameobjects.Spaceship;
import nl.rug.aoop.asteroids.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A runnable object which, when started in a thread, runs the main game loop and periodically updates the game's model
 * as time goes on. This class can be thought of as the 'Game Engine', because it is solely responsible for all changes
 * to the game model as a result of user input, and this class also defines the very important game loop itself.
 */
public class GameUpdater implements Runnable {
    /**
     * The refresh rate of the display, in frames per second. Increasing this number makes the game look smoother, up to
     * a certain point where it's no longer noticeable.
     */
    private static final int DISPLAY_FPS = 120;

    /**
     * The rate at which the game ticks (how often physics updates are applied), in frames per second. Increasing this
     * number speeds up everything in the game. Ships react faster to input, bullets fly faster, etc.
     */
    private static final int PHYSICS_FPS = 30;

    /**
     * The number of milliseconds in a game tick.
     */
    public static final double MILLISECONDS_PER_TICK = 1000.0 / PHYSICS_FPS;

    /**
     * The default maximum number of asteroids that may be present in the game when starting.
     */
    private static final int ASTEROIDS_LIMIT_DEFAULT = 7;

    /**
     * Set this to true to allow asteroids to collide with each other, potentially causing chain reactions of asteroid
     * collisions.
     */
    private static final boolean KESSLER_SYNDROME = false;

    /**
     * The number of ticks between asteroid spawns
     */
    private static final int ASTEROID_SPAWN_RATE = 200;

    /**
     * The game that this updater works for.
     */
    private final Game game;

    /**
     * Counts the number of times the game has updated.
     */
    private int updateCounter;

    /**
     * The limit to the number of asteroids that may be present. If the current number of asteroids exceeds this amount,
     * no new asteroids will spawn.
     */
    private int asteroidsLimit;

    /**
     * Number of spaceships alive. Used to determine when are all dead.
     */
    private int alive;

    /**
     * Determines if the game is SinglePlayer or MultiPlayer.
     */
    private String gameMode;

    /**
     * Constructs a new game updater with the given game.
     *
     * @param game The game that this updater will update when it's running.
     */
    public GameUpdater(Game game) {
        this.game = game;
        alive = game.getSpaceshipCollection().size();
        updateCounter = 0;
        asteroidsLimit = ASTEROIDS_LIMIT_DEFAULT;
    }

    private void updateScores() {
        game.getSpaceshipCollection().forEach(spaceship -> {
            Pair<String, Integer> pair = new Pair<>(spaceship.getNickname(), spaceship.getScore());
            game.getScoreMap().put(spaceship.getIdentifier(), pair);
        });
    }

    /**
     * The main game loop.
     * <p>
     * Starts the game updater thread. This will run until the quit() method is called on this updater's game object.
     */
    @Override
    public void run() {
        long previousTime = System.currentTimeMillis();
        long timeSinceLastTick = 0L;
        long timeSinceLastDisplayFrame = 0L;

        final double millisecondsPerDisplayFrame = 1000.0 / DISPLAY_FPS;

        if (!game.isClient()) {
            while ((game.isRunning() && !(alive <= 0))) {
                updateScores();
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - previousTime;
                timeSinceLastTick += elapsedTime;
                timeSinceLastDisplayFrame += elapsedTime;

                if (timeSinceLastTick >= MILLISECONDS_PER_TICK) { // Check if enough time has passed to update the physics.
                    updatePhysics(); // Perform one 'step' in the game.
                    timeSinceLastTick = 0L;
                }
                if (timeSinceLastDisplayFrame >= millisecondsPerDisplayFrame) { // Check if enough time has passed to refresh the display.
                    game.notifyListeners(timeSinceLastTick); // Tell the asteroids panel that it should refresh.
                    timeSinceLastDisplayFrame = 0L;
                }

                previousTime = currentTime;
            }
            increaseLimit();
            updateDatabaseScores();
        }
    }

    private void increaseLimit() {
        if (game.getSpaceShip().getScore() % 5 == 0) {
            asteroidsLimit++;
        }
    }

    private void updateDatabaseScores() {
        if (game.getScoreMap().size() == 1) gameMode = "Single";
        else gameMode = "Multi";
        DatabaseManager databaseManager = new DatabaseManager("test");
        game.getScoreMap().forEach((integer, stringIntegerPair) -> {
            Score score = new Score(stringIntegerPair.getA(), stringIntegerPair.getB(), gameMode);
            databaseManager.addScore(score);
        });
        databaseManager.closeDatabase();
    }

    /**
     * Called every game tick, to update all the game's model objects.
     * <p>
     * First, each object's movement is updated by calling nextStep() on it.
     * Then, if the player is pressing the key to fire the ship's weapon, a new bullet should spawn.
     * Then, once all objects' positions are updated, we check for any collisions between them.
     * And finally, any objects which are destroyed by collisions are removed from the game.
     * <p>
     * Also, every 200 game ticks, if possible, a new random asteroid is added to the game.
     */
    private void updatePhysics() {
        Collection<Bullet> bullets = game.getBullets();
        Collection<Asteroid> asteroids = game.getAsteroids();
        Collection<Spaceship> spaceships = game.getSpaceshipCollection();

        asteroids.forEach(GameObject::nextStep);
        bullets.forEach(GameObject::nextStep);
        spaceships.forEach(Spaceship::nextStep);

        spaceships.forEach(ship -> fireWeapon(ship, bullets));

        checkCollisions();
        removeDestroyedObjects();

        // Every 200 game ticks, try and spawn a new asteroid.
        if (updateCounter % ASTEROID_SPAWN_RATE == 0 && asteroids.size() < asteroidsLimit) {
            addRandomAsteroid();
        }
        updateCounter++;
    }

    private void fireWeapon(Spaceship ship, Collection<Bullet> bullets) {
        if (ship.canFireWeapon()) {
            double direction = ship.getDirection();
            bullets.add(
                    new Bullet(
                            ship.getLocation().getX(),
                            ship.getLocation().getY(),
                            ship.getVelocity().x + Math.sin(direction) * 15,
                            ship.getVelocity().y - Math.cos(direction) * 15,
                            ship.getIdentifier()
                    )
            );
            ship.setFired();
        }
    }

    /**
     * Adds a random asteroid at least 50 pixels away from the player's spaceship.
     */
    private void addRandomAsteroid() {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        Point.Double newAsteroidLocation;
        Point.Double shipLocation = game.getSpaceShip().getLocation();
        double distanceX, distanceY;
        do { // Iterate until a point is found that is far enough away from the player.
            newAsteroidLocation = new Point.Double(rng.nextDouble(0.0, 800.0), rng.nextDouble(0.0, 800.0));
            distanceX = newAsteroidLocation.x - shipLocation.x;
            distanceY = newAsteroidLocation.y - shipLocation.y;
        } while (distanceX * distanceX + distanceY * distanceY < 50 * 50); // Pythagorean theorem for distance between two points.

        double randomChance = rng.nextDouble();
        Point.Double randomVelocity = new Point.Double(rng.nextDouble() * 6 - 3, rng.nextDouble() * 6 - 3);
        AsteroidSize randomSize;
        if (randomChance < 0.333) { // 33% chance of spawning a large asteroid.
            randomSize = AsteroidSize.LARGE;
        } else if (randomChance < 0.666) { // 33% chance of spawning a medium asteroid.
            randomSize = AsteroidSize.MEDIUM;
        } else { // And finally a 33% chance of spawning a small asteroid.
            randomSize = AsteroidSize.SMALL;
        }
        game.getAsteroids().add(new Asteroid(newAsteroidLocation, randomVelocity, randomSize));
    }

    /**
     * Checks all objects for collisions and marks them as destroyed upon collision. All objects can collide with
     * objects of a different type, but not with objects of the same type. I.e. bullets cannot collide with bullets etc.
     */
    private void checkCollisions() {
        Collection<Spaceship> spaceships = game.getSpaceshipCollection();

        // First check collisions between bullets and other objects.
        game.getBullets().forEach(bullet -> {
            game.getAsteroids().forEach(asteroid -> { // Check collision with any of the asteroids.
                if (asteroid.collides(bullet)) {
                    asteroid.destroy();
                    bullet.destroy();
                    increaseScore(bullet, "asteroid");
                }
            });
            spaceships.forEach(ship -> checkBulletCollision(ship, bullet));
        });
        // Next check for collisions between asteroids and the spaceship.
        game.getAsteroids().forEach(asteroid -> {
            spaceships.forEach(ship -> checkAsteroidCollision(ship, asteroid));
            if (KESSLER_SYNDROME) { // Only check for asteroid - asteroid collisions if we allow kessler syndrome.
                game.getAsteroids().forEach(secondAsteroid -> {
                    if (!asteroid.equals(secondAsteroid) && asteroid.collides(secondAsteroid)) {
                        asteroid.destroy();
                        secondAsteroid.destroy();
                    }
                });
            }
        });
        //Next check for collisions between ships
        game.getSpaceshipCollection().forEach(spaceship -> game.getSpaceshipCollection().forEach(spaceship1 -> {
            if (!spaceship.equals((spaceship1)) && spaceship.collides(spaceship1)) {
                spaceship.destroy();
                alive--;
                spaceship1.destroy();
                alive--;
            }
        }));
    }

    private void increaseScore(Bullet bullet, String type) {
        int id = bullet.getIdentifier();
        Spaceship ship = game.getSpaceshipMap().get(id);
        if (Objects.equals(type, "ship")) ship.increaseScore5();
        else ship.increaseScore1();
    }

    private void checkAsteroidCollision(Spaceship ship, Asteroid asteroid) {
        if (asteroid.collides(ship)) {
            asteroid.destroy();
            ship.destroy();
            alive--;
        }
    }

    private void checkBulletCollision(Spaceship ship, Bullet bullet) {
        if (ship.collides(bullet)) { // Check collision with ship.
            bullet.destroy();
            ship.destroy();
            alive--;
            increaseScore(bullet, "ship");
        }
    }

    /**
     * Removes all destroyed objects (those which have collided with another object).
     * <p>
     * When an asteroid is destroyed, it may spawn some smaller successor asteroids, and these are added to the game's
     * list of asteroids.
     */
    private void removeDestroyedObjects() {
        // Avoid reallocation and assume every asteroid spawns successors.
        Collection<Asteroid> newAsteroids = new ArrayList<>(game.getAsteroids().size() * 2);
        game.getAsteroids().forEach(asteroid -> {
            if (asteroid.isDestroyed()) {
                newAsteroids.addAll(asteroid.getSuccessors());
            }
        });
        game.getAsteroids().addAll(newAsteroids);
        // Remove all asteroids that are destroyed.
        game.getAsteroids().removeIf(GameObject::isDestroyed);
        //Remove all the ships that are destroyed
        game.getSpaceshipCollection().removeIf(GameObject::isDestroyed);
        // Remove any bullets that are destroyed.
        game.getBullets().removeIf(GameObject::isDestroyed);
    }
}

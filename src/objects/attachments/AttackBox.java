package objects.attachments;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;

import helpers.Point;
import helpers.Rectangle;
import objects.GameObject;
import objects.images.ImageBuilder;
import objects.images.LayeredImage;
import objects.world.characters.Enemy;

public class AttackBox extends Attachment {
	private LayeredImage image;
	private Collection<Enemy> damagedByThisAttack = new HashSet<>();

	public AttackBox(Rectangle rect) {
		this(rect, null);
	}

	public AttackBox(Rectangle rect, GameObject myObject) {
		super(rect, myObject);

	}

	public AttackBox(Point topLeft, float width, float height) {
		this(new Rectangle(topLeft, width, height));
	}

	public AttackBox(Point topLeft, float width, float height,
			GameObject myObject) {
		this(new Rectangle(topLeft, width, height), myObject);
	}

	@Override
	public LayeredImage getImage() {
		if (image == null) {
			image = ImageBuilder.makeAttackBoxImage(getRectangle());
		}

		return image;
	}

	/**
	 * Checks whether this attack overlaps any enemies. Will fire the
	 * attackEffect... methods on any enemies for which it overlaps.
	 */
	public void checkAttack() {
		Stream<Enemy> enemies = getOverlappingObjectsOfType(Enemy.class);
		// Operate on the stream to apply the effect to all unaffected enemies,
		// and count the number of enemies harmed.
		Optional<Integer> enemiesHarmed = enemies.filter(
				e -> !damagedByThisAttack.contains(e)).map(e -> {
					effectOnEnemy(e);
					damagedByThisAttack.add(e);
					return 1;
				}).reduce((a, b) -> a + b);

		if (enemiesHarmed.isPresent()) {
			// This will happen if at least one enemy is harmed.
			effectOnPlayer();
		}
	}

	/**
	 * Resets the targets of this AttackBox. This will allow this AttackBox to
	 * damage a single enemy multiple times.
	 */
	public void resetTargets() {
		damagedByThisAttack = new HashSet<>();
	}

	/**
	 * This is called once when the AttackBox overlaps with any number of
	 * enemies. Override this to allow the attack to have an effect on the
	 * player if it hits.
	 */
	public void effectOnPlayer() {

	}

	/**
	 * This is called once for every enemy that overlaps with the Attackbox.
	 * This will only be called once on the enemy until resetTargets is called.
	 * Override this to allow the attack to have an effect on the enemies hit.
	 * 
	 * @param e
	 */
	public void effectOnEnemy(Enemy e) {

	}

}

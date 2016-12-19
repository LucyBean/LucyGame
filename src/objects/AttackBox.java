package objects;

import java.util.Collection;
import java.util.HashSet;

import characters.Enemy;
import helpers.Point;
import helpers.Rectangle;
import images.ImageBuilder;
import images.LayeredImage;

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
		Collection<Enemy> enemies = getOverlappingEnemies();
		if (!enemies.isEmpty()) {
			attackEffectOnPlayer();
			enemies.stream().filter(
					e -> !damagedByThisAttack.contains(e)).forEach(e ->
							{
								attackEffectOnEnemy(e);
								damagedByThisAttack.add(e);
							});
		}
	}
	
	/**
	 * Resets the targets of this AttackBox. This will allow this AttackBox to
	 * damage an enemy again.
	 */
	public void resetTargets() {
		damagedByThisAttack = new HashSet<>();
	}

	private Collection<Enemy> getOverlappingEnemies() {
		Rectangle rect = getObject().getCoOrdTranslator().objectToWorldCoOrds(
				getRectangle());
		return getObject().getWorld().getMap().getOverlappingObjectsOfType(rect,
				Enemy.class);
	}

	/**
	 * This is called once when the AttackBox overlaps with any number of
	 * enemies. Override this to allow the attack to have an effect on the
	 * player if it hits.
	 */
	public void attackEffectOnPlayer() {

	}

	/**
	 * This is called once for every enemy that overlaps with the Attackbox.
	 * Override this to allow the attack to have an effect on the enemies hit.
	 * 
	 * @param e
	 */
	public void attackEffectOnEnemy(Enemy e) {

	}

}

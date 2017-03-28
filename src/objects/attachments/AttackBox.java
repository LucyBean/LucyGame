package objects.attachments;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import helpers.Point;
import helpers.Rectangle;
import objects.GameObject;
import objects.images.ImageBuilder;
import objects.images.LayeredImage;
import objects.world.characters.Enemy;
import objects.world.characters.NPC;

public class AttackBox extends Attachment {
	private LayeredImage image;
	private Collection<Enemy> damagedByThisAttack = new HashSet<>();
	private Collection<NPC> npcsHitByThis = new HashSet<>();
	private boolean affectsNPCs = false;

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
		// TODO: Change back to type stream
		Collection<NPC> npcs = getOverlappingObjectsOfType(NPC.class).collect(Collectors.toSet());
		Optional<Integer> npcsHit;
		if (affectsNPCs) {
			npcsHit = npcs.stream().filter(n -> !npcsHitByThis.contains(n)).map(n -> {
				n.oww();
				npcsHitByThis.add(n);
				return 1;
			}).reduce((a, b) -> a + b);
		} else {
			npcsHit = Optional.empty();
		}

		if (enemiesHarmed.isPresent() || npcsHit.isPresent()) {
			// This will happen if at least one enemy or NPC is harmed.
			effectOnPlayer();
		}
	}

	/**
	 * Changes whether this attack will affect NPCs.
	 * 
	 * @param b
	 */
	public void affectsNPCs(boolean b) {
		affectsNPCs = b;
	}

	/**
	 * Resets the targets of this AttackBox. This will allow this AttackBox to
	 * damage a single enemy multiple times.
	 */
	public void resetTargets() {
		damagedByThisAttack = new HashSet<>();
		npcsHitByThis = new HashSet<>();
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
		e.damage(1);
	}

}

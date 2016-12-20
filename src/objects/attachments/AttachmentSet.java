package objects.attachments;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class AttachmentSet {
	private HashMap<Class<? extends Attachment>, Collection<Attachment>> attachments;
	private HashSet<Attachment> all;

	public AttachmentSet() {
		attachments = new HashMap<>();
		all = new HashSet<>();
	}

	public <T extends Attachment> Collection<T> getByType(Class<T> cls) {
		Collection<Attachment> as = getSet(cls);
		Collection<T> byType = new HashSet<T>();
		as.stream().map(a -> cls.cast(a)).forEach(a -> byType.add(a));
		return byType;
	}

	public Collection<Attachment> getAll() {
		return all;
	}

	@SuppressWarnings("unchecked")
	private Collection<Attachment> getSet(Class<? extends Attachment> cls) {
		// get super class if cls is anonymous
		// this will probably happen for attack boxes
		if (cls.isAnonymousClass()) {
			cls = (Class<? extends Attachment>) cls.getSuperclass();
		}
		attachments.putIfAbsent(cls, new HashSet<Attachment>());
		Collection<Attachment> as = attachments.get(cls);
		return as;
	}

	public void add(Attachment a) {
		Collection<Attachment> as = getSet(a.getClass());
		as.add(a);
		all.add(a);
	}

	public void remove(Attachment a) {
		Collection<Attachment> as = getSet(a.getClass());
		as.remove(a);
		all.remove(a);

	}
}

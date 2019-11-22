package de.slag.invest;

import java.util.Comparator;

import de.slag.invest.model.DomainBean;

public class DomainBeanComparator implements Comparator<DomainBean> {

	public int compare(DomainBean o1, DomainBean o2) {
		if (o1 == o2) {
			// also both NULL case
			return 0;
		}

		if (o1 != null && o2 == null) {
			return 1;
		}

		if (o1 == null && o2 != null) {
			return -1;
		}

		final int classCompare = o1.getClass().getName().compareTo(o1.getClass().getName());
		if (classCompare != 0) {
			return classCompare;
		}

		Long id1 = o1.getId();
		Long id2 = o2.getId();

		if (id1 == null && id2 == null) {
			return o1.getUuid().compareTo(o2.getUuid());
		}

		if (id1 != null && id2 == null) {
			return 1;
		}

		if (id1 == null && id2 != null) {
			return -1;
		}
		return id1.compareTo(id2);

	}
}

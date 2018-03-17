package com.pnp.utils;

import java.util.Comparator;

import com.pnp.model.ContactModel;

/**
 * 根据联系人账号进行排序
 * 
 * @author lianhai
 */
public class PinyinComparator implements Comparator<ContactModel> {

	public int compare(ContactModel o1, ContactModel o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}

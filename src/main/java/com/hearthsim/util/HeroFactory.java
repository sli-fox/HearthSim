package com.hearthsim.util;

import com.hearthsim.card.minion.Hero;
import com.hearthsim.exception.HSInvalidHeroException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A utility to convert a hero string to a Hero class instance
 *
 */
public class HeroFactory {
	public static Hero getHero(String heroName) throws HSInvalidHeroException {
		String cleanedString = heroName.trim();
		if (cleanedString.equals("None"))
			return new Hero();
		try {
			Class<?> clazz = Class.forName("com.hearthsim.card.minion.heroes." + cleanedString);
			Constructor<?> ctor = clazz.getConstructor();
			Object object = ctor.newInstance();
			return (Hero)object;
		} catch (ClassNotFoundException e) {
			throw new HSInvalidHeroException("Unknown hero: " + cleanedString);
		} catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
			throw new HSInvalidHeroException("Unknown hero: " + cleanedString);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			throw new HSInvalidHeroException("Unknown hero: " + cleanedString);
		}
	}
}

package com.fuffles.demo.util;

import org.bukkit.Sound;

public class SoundUtil
{
	public static String writeSound(Sound type)
	{
		return type.getKey().toString();
	}
}

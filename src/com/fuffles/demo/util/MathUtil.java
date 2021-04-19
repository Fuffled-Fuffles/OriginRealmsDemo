package com.fuffles.demo.util;

import java.util.function.Consumer;

public class MathUtil 
{
	public static void forAllBut(int min, int max, int range_min, int range_max, Consumer<Integer> consumer)
	{
		for (int i = min; i < max; i++)
		{
			if (!(i >= range_min && i <= range_max))
			{
				consumer.accept(i);
			}
		}
	}
}

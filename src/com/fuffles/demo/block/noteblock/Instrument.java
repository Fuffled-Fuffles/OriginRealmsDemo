package com.fuffles.demo.block.noteblock;

import com.fuffles.demo.Demo;

public enum Instrument 
{
	HARP(0, "harp"),
	BASS(1, "bass"),
	SNARE_DRUM(2, "snare"),
	CLICK(3, "hat"),
	BASS_DRUM(4, "basedrum"),
	BELL(5, "bell"),
	FLUTE(6, "flute"),
	CHIME(7, "chime"),
	GUITAR(8, "guitar"),
	XYLO(9, "xylophone"),
	VIBRA(10, "iron_xylophone"),
	COWBELL(11, "cow_bell"),
	DIDGERI(12, "didgeridoo"),
	BIT(13, "bit"),
	BANJO(14, "banjo"),
	PLING(15, "pling");
	
	
	private final int id;
	private final String instrument;
	
	private Instrument(int id, String instr)
	{
		this.id = id;
		this.instrument = instr;
	}
	
	public String toBlockData()
	{
		return "instrument=" + this.toString();
	}
	
	public int toId()
	{
		return this.id;
	}
	
	@Override
	public String toString()
	{
		return this.instrument;
	}
	
	public static Instrument byId(int id)
	{
		for (Instrument instr : Instrument.values())
		{
			if (instr.toId() == id)
			{
				return instr;
			}
		}
		Demo.LOG.warning("Couldn't find the instrument of id '" + id + "', fallbacking to HARP.");
		return Instrument.HARP;
	}
	
	public static Instrument byName(String name)
	{
		for (Instrument instr : Instrument.values())
		{
			if (instr.toString().equals(name))
			{
				return instr;
			}
		}
		Demo.LOG.warning("Couldn't find the instrument of name '" + name + "', fallbacking to HARP.");
		return Instrument.HARP;
	}
}

package com.fuffles.demo.block.noteblock;

public class NoteBlockData 
{	
	private Instrument instrument = Instrument.HARP;
	private Integer note = 0;
	private Boolean powered = false;
	
	private NoteBlockData() {}
	
	public NoteBlockData withInstrument(Instrument instr)
	{
		this.instrument = instr;
		return this;
	}
	
	public NoteBlockData withNote(int note)
	{
		if (note < 0)
		{
			note = 0;
		}
		else if (note > 24)
		{
			note = 24;
		}
		this.note = note;
		return this;
	}
	
	public NoteBlockData withPowered(boolean state)
	{
		this.powered = state;
		return this;
	}
	
	public String build()
	{
		return "minecraft:note_block[" + this.instrument.toBlockData() + ",note=" + this.note.intValue() + ",powered=" + this.powered.toString() + "]";
	}
	
	public static NoteBlockData builder()
	{
		return new NoteBlockData();
	}
}

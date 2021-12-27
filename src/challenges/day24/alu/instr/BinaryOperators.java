package challenges.day24.alu.instr;

/**
 * Enum of all available binary operands
 * 
 * @author Joris
 */
public enum BinaryOperators {
	Add ("add"),
	Multiply ("mul"),
	Divide ("div"),
	Modulo ("mod"),
	Equals ("eql");
	
	/** The instruction label */
	private final String instr;
	
	/**
	 * Creates a new Binary Operand
	 * 
	 * @param lbl The instruction label
	 */
	private BinaryOperators( final String lbl ) {
		this.instr = lbl;
	}
	
	/**
	 * Determines the binary operand from the instruction label
	 * 
	 * @param instrlbl The instruction label
	 * @return The instruction
	 */
	public static BinaryOperators fromInstruction( final String instrlbl ) {
		for( final BinaryOperators bo : values( ) )
			if( bo.instr.equals( instrlbl ) )
				return bo;
		
		throw new IllegalArgumentException( "Unsupported binary operand" );
	}

}

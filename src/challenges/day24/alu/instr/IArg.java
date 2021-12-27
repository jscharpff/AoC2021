package challenges.day24.alu.instr;

import challenges.day24.alu.mem.ALUMem;
import challenges.day24.alu.mem.MemRegister;

/**
 * Holds an argument to an instruction, either a literal or register value
 * 
 * @author Joris
 */
public class IArg {
	/** The literal value it may represent */
	private final long value;
	
	/** Or the memory register refers to */
	private final MemRegister register;
	
	/**
	 * Creates a new literal instruction argument
	 * 
	 * @param value The literal value
	 */
	public IArg( final long value ) {
		this.value = value;
		this.register = null;
	}
	
	/**
	 * Creates a new memory register argument
	 * 
	 * @param reg The memory register
	 */
	public IArg( final MemRegister reg ) {
		this.value = -1;
		this.register = reg;
	}
	
	/** @return True iff this is a literal argument */
	public boolean isLiteral( ) { return this.register == null; }
	
	/** @return The memory register this argument references */
	public MemRegister getRegister( ) {
		if( isLiteral( ) ) throw new RuntimeException( "Tried to get register of literal argument" );
		return this.register;
	}
	
	/** @return The value of the literal or the value of the register in the memory */
	public long getValue( final ALUMem mem ) {
		return isLiteral( ) ? value : mem.read( register );
	}

	/**
	 * Parses an argument from its string description
	 * 
	 * @param input The string input
	 * @return The parsed argument
	 */
	public static IArg parse( final String input ) {
		// check if this argument refers to a register
		try {
			return new IArg( MemRegister.parse( input ) );
		} catch( IllegalArgumentException ia ) {
			// not a memory register, assume a literal value
			return new IArg( Long.parseLong( input ) );
		}
	}
}

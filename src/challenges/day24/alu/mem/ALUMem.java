package challenges.day24.alu.mem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Register-based memory for the ALU unit
 * 
 * @author Joris
 */
public class ALUMem {
	/** The map of active registers and their current values */
	private final Map<MemRegister, Long> registers;
	
	/** The program input as a FIFO stack of digits */
	private Stack<Integer> in;
	
	/**
	 * Creates a new Memory Register unit for the ALU
	 * 
	 * @param registers The registers that are available to the ALU
	 */
	public ALUMem( final MemRegister... registers ) {
		// initialise the registers to 0, marking them as available
		this.registers = new HashMap<>( registers.length );
		for( final MemRegister r : registers )
			this.registers.put( r, (long)0 );
	}

	/**
	 * Retrieves the value currently stored in the register
	 * 
	 * @param reg The register to load
	 */
	public long read( final MemRegister reg ) {
		if( !registers.containsKey( reg ) ) throw new IllegalArgumentException( "No such register: "+ reg );
		return registers.get( reg );
	}
	
	/**
	 * Writes the value to the register
	 * 
	 * @param register The register to write to
	 * @param value The value to store in the register
	 */
	public void write( final MemRegister reg, final long value ) {
		if( !registers.containsKey( reg ) ) throw new IllegalArgumentException( "No such register: "+ reg );
		registers.put( reg, value );
	}
	
	/**
	 * Stores the program input in memory
	 * 
	 * @param input The program input, as a string of digits
	 */
	public void setInput( final String input ) {
		// map characters 0-9 to their actual numerical value
		// perform this in reverse so the stack acts FIFO
		final Stack<Integer> stack = new Stack<>( );
		for( int i = input.length( ) - 1; i >= 0; i-- )
			stack.add( input.charAt( i ) - '0' );
		in = stack;
	}
	
	/**
	 * @return The next input element
	 */
	public int input( ) {
		if( in.isEmpty( ) ) throw new RuntimeException( "No more input available" );
		return in.pop( );
	}
	
	/** @return A dump of the current memory */
	@Override
	public String toString( ) {
		return registers.toString( );
	}
}

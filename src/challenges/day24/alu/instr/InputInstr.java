package challenges.day24.alu.instr;

/**
 * Instruction that reads a single digit from the program input
 * 
 * @author Joris
 */
public class InputInstr extends Instr {

	/**
	 * Creates a new input instruction
	 * 
	 * @param args The arguments to the instruction
	 */
	public InputInstr( final IArg... args ) {
		super( args, 1 );
	}
	
	/**
	 * Executes the instruction, simply reads a value from the input and writes
	 * it to the register indicated by the argument
	 * 
	 * @return The digit (0-9) read from the program input
	 */
	@Override
	protected long executeInstruction( ) {
		return mem.input( );
	}

}
